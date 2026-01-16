package com.Hiag0.clearlag.service;

import com.Hiag0.clearlag.config.ClearLagConfig;
import com.Hiag0.clearlag.messages.Messages;
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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

public class CleanupService {

    private final ClearLagConfig config;
    private final JavaPlugin plugin;

    public CleanupService(ClearLagConfig config, JavaPlugin plugin) {
        this.config = config;
        this.plugin = plugin;
    }

    public void scheduleCleanup() {
        if (config.getMinutesExecution() <= 0)
            return;

        // Agendar a execução periódica
        HytaleServer.SCHEDULED_EXECUTOR.scheduleAtFixedRate(
                this::iniciarCicloLimpeza,
                config.getMinutesExecution(),
                config.getMinutesExecution(),
                TimeUnit.MINUTES);

        broadcast(Messages.scheduleInfo(config.getMinutesExecution()));
    }

    public void iniciarCicloLimpeza() {
        int segundosAviso = config.getCleanupWarningSeconds();
        broadcast(Messages.warning(segundosAviso));

        HytaleServer.SCHEDULED_EXECUTOR.schedule(() -> {
            broadcast(Messages.CLEANUP_STARTED);
            executarLimpezaGeral();
        }, segundosAviso, TimeUnit.SECONDS);
    }

    private void broadcast(Message message) {
        plugin.getLogger().at(Level.INFO).log(message.toString()); // Fallback text log
        try {
            Universe.get().sendMessage(message);
        } catch (Exception e) {
            // Ignorar erro se o universo não estiver pronto
        }
    }

    public void executarLimpezaGeral() {
        try {
            Universe universe = Universe.get();
            if (universe != null && universe.getWorlds() != null) {
                universe.getWorlds().values().forEach(world -> {
                    world.execute(() -> realizarRemocaoNoMundo(world));
                });
            }
        } catch (Exception e) {
            plugin.getLogger().at(Level.SEVERE).log("[ClearLag] Erro ao executar limpeza agendada: " + e.getMessage());
        }

        broadcast(Messages.nextCleanup(config.getMinutesExecution()));
    }

    public void realizarRemocaoNoMundo(World world) {
        if (world == null)
            return;

        final String worldName = world.getName();
        EntityStore entityStore = world.getEntityStore();
        if (entityStore == null)
            return;

        Store<EntityStore> store = entityStore.getStore();
        broadcast(Messages.manualCleanupStart(worldName));

        final AtomicInteger totalIterado = new AtomicInteger(0);
        final ConcurrentLinkedQueue<com.hypixel.hytale.component.Ref<EntityStore>> filaRemocao = new ConcurrentLinkedQueue<>();
        File logFile = new File(plugin.getDataDirectory().toFile(), "clearlag_debug.log");

        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(logFile, true)))) {
            pw.println("--- Iniciando Varredura Híbrida: " + new Date() + " ---");

            store.forEachEntityParallel(
                    (int index, ArchetypeChunk<EntityStore> chunk, CommandBuffer<EntityStore> buffer) -> {
                        totalIterado.incrementAndGet();
                        com.hypixel.hytale.component.Archetype<EntityStore> arch = chunk.getArchetype();

                        if (arch != null) {
                            processArchetype(index, arch, chunk, filaRemocao, pw);
                        }
                    });

            // Remover entidades marcadas
            int removidosSubtotal = 0;
            for (com.hypixel.hytale.component.Ref<EntityStore> ref : filaRemocao) {
                try {
                    store.removeEntity(ref, RemoveReason.REMOVE);
                    removidosSubtotal++;
                } catch (Exception ignored) {
                }
            }

            final int finalRemovidos = removidosSubtotal;
            pw.println("Varredura finalizada. Analisados: " + totalIterado.get() + " | Removidos: " + finalRemovidos);
            pw.println("-------------------------------------------");
            pw.flush();

            broadcast(Messages.cleanupFinished(worldName, finalRemovidos));

        } catch (Exception e) {
            plugin.getLogger().at(Level.SEVERE).log("[ClearLag] Erro ao gravar debug log: " + e.getMessage());
        }
    }

    private void processArchetype(int index, com.hypixel.hytale.component.Archetype<EntityStore> arch,
            ArchetypeChunk<EntityStore> chunk,
            ConcurrentLinkedQueue<com.hypixel.hytale.component.Ref<EntityStore>> filaRemocao,
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
            filaRemocao.add(chunk.getReferenceTo(index));
            sb.append(" <<< MARCADO P/ REMOÇÃO >>>");
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
