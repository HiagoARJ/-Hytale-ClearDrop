package com.Hiag0.cleardrop.service;

import com.Hiag0.cleardrop.config.ClearDropConfig;
import com.Hiag0.cleardrop.messages.Messages;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.RemoveReason;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

import com.Hiag0.cleardrop.ClearDrop;

public class CleanupService {

    private final JavaPlugin plugin;
    private ScheduledFuture<?> scheduledTask;

    public CleanupService(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void scheduleCleanup() {
        if (ClearDrop.CONFIG.get().getMinutesExecution() <= 0)
            return;

        // Schedule periodic execution
        this.scheduledTask = HytaleServer.SCHEDULED_EXECUTOR.scheduleAtFixedRate(
                this::startCleanupCycle,
                ClearDrop.CONFIG.get().getMinutesExecution(),
                ClearDrop.CONFIG.get().getMinutesExecution(),
                TimeUnit.MINUTES);

        if (ClearDrop.CONFIG.get().isNotificationAutoEnabled()) {
            broadcast(Messages.scheduleInfo(ClearDrop.CONFIG.get().getMinutesExecution()));
        }
    }

    public void cancelTask() {
        if (scheduledTask != null && !scheduledTask.isCancelled()) {
            scheduledTask.cancel(false);
            scheduledTask = null;
        }
    }

    public void reschedule() {
        cancelTask();
        scheduleCleanup();
    }

    public void startCleanupCycle() {
        int warningSeconds = ClearDrop.CONFIG.get().getCleanupWarningSeconds();
        if (ClearDrop.CONFIG.get().isNotificationNoticeEnabled()) {
            broadcast(Messages.warning(warningSeconds));
        }

        HytaleServer.SCHEDULED_EXECUTOR.schedule(() -> {
            if (ClearDrop.CONFIG.get().isNotificationNoticeEnabled()) {
                broadcast(Messages.getCleanupStarted());
            }
            executeFullCleanup();
        }, warningSeconds, TimeUnit.SECONDS);
    }

    private void broadcast(Message message) {
        plugin.getLogger().at(Level.INFO).log(message.toString()); // Fallback text log
        try {
            Universe.get().sendMessage(message);
        } catch (Exception e) {
            // Ignore error if universe is not ready
        }
    }

    public void executeFullCleanup() {
        try {
            Universe universe = Universe.get();
            if (universe != null && universe.getWorlds() != null) {
                universe.getWorlds().values().forEach(world -> {
                    world.execute(() -> performWorldRemoval(world));
                });
            }
        } catch (Exception e) {
            plugin.getLogger().at(Level.SEVERE).log("[ClearDrop] Error executing scheduled cleanup: " + e.getMessage());
        }

        if (ClearDrop.CONFIG.get().isNotificationAutoEnabled()) {
            broadcast(Messages.nextCleanup(ClearDrop.CONFIG.get().getMinutesExecution()));
        }
    }

    public void performWorldRemoval(World world) {
        if (world == null)
            return;

        final String worldName = world.getName();
        EntityStore entityStore = world.getEntityStore();
        if (entityStore == null)
            return;

        Store<EntityStore> store = entityStore.getStore();
        if (ClearDrop.CONFIG.get().isNotificationCleanupFinishedEnabled()) {
            broadcast(Messages.manualCleanupStart(worldName));
        }

        final AtomicInteger totalAnalyzed = new AtomicInteger(0);
        final ConcurrentLinkedQueue<com.hypixel.hytale.component.Ref<EntityStore>> removalQueue = new ConcurrentLinkedQueue<>();
        File logFile = new File(plugin.getDataDirectory().toFile(), "cleardrop_debug.log");

        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(logFile, true)))) {
            pw.println("--- Starting Hybrid Scan: " + new Date() + " ---");

            store.forEachEntityParallel(
                    (int index, ArchetypeChunk<EntityStore> chunk, CommandBuffer<EntityStore> buffer) -> {
                        totalAnalyzed.incrementAndGet();
                        com.hypixel.hytale.component.Archetype<EntityStore> arch = chunk.getArchetype();

                        if (arch != null) {
                            processArchetype(index, arch, chunk, removalQueue, pw);
                        }
                    });

            // Remove marked entities
            int removedSubtotal = 0;
            for (com.hypixel.hytale.component.Ref<EntityStore> ref : removalQueue) {
                try {
                    store.removeEntity(ref, RemoveReason.REMOVE);
                    removedSubtotal++;
                } catch (Exception ignored) {
                }
            }

            final int finalRemoved = removedSubtotal;
            pw.println("Scan finished. Analyzed: " + totalAnalyzed.get() + " | Removed: " + finalRemoved);
            pw.println("-------------------------------------------");
            pw.flush();

            if (ClearDrop.CONFIG.get().isNotificationCleanupFinishedEnabled()) {
                broadcast(Messages.cleanupFinished(worldName, finalRemoved));
            }

        } catch (Exception e) {
            plugin.getLogger().at(Level.SEVERE).log("[ClearDrop] Error writing debug log: " + e.getMessage());
        }
    }

    private void processArchetype(int index, com.hypixel.hytale.component.Archetype<EntityStore> arch,
            ArchetypeChunk<EntityStore> chunk,
            ConcurrentLinkedQueue<com.hypixel.hytale.component.Ref<EntityStore>> removalQueue,
            PrintWriter pw) {
        StringBuilder sb = new StringBuilder();
        sb.append("Index ").append(index).append(" [");

        int count = arch.count();
        int matches = 0;
        boolean hasInvalid = false;

        for (int j = 0; j < count; j++) {
            com.hypixel.hytale.component.ComponentType<EntityStore, ?> ct = arch.get(j);
            if (ct != null) {
                Class<?> compClass = ct.getTypeClass();
                if (compClass != null) {
                    String name = compClass.getSimpleName();
                    sb.append(name).append(" ");

                    if (isAllowedComponent(name)) {
                        if (!name.equals("TransformComponent")) {
                            matches++;
                        }
                    } else {
                        hasInvalid = true;
                    }
                }
            }
        }
        sb.append("][").append(count).append("]");

        if ((count >= 11 && count <= 13) || (!hasInvalid && matches == 3 && count <= 4)) {
            removalQueue.add(chunk.getReferenceTo(index));
            sb.append(" <<< MARKED FOR REMOVAL >>>");
        }

        synchronized (pw) {
            pw.println(sb.toString());
        }
    }

    private boolean isAllowedComponent(String name) {
        return name.equals("PhysicsValues") ||
                name.equals("Velocity") ||
                name.equals("BoundingBox") ||
                name.equals("TransformComponent");
    }
}
