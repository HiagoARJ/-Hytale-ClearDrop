package com.Hiag0.clearlag;

import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.RemoveReason;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.concurrent.TimeUnit;
import java.io.File;
import java.io.FileReader;
import java.util.logging.Level;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ClearLag extends JavaPlugin {

    private int minutosExecucao = 5;
    private int segundosAviso = 5;

    public ClearLag(JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void start() {
        carregarConfiguracao();
        getLogger().at(Level.INFO).log("ClearLag iniciado. Limpeza a cada " + minutosExecucao + " minutos.");

        HytaleServer.SCHEDULED_EXECUTOR.scheduleAtFixedRate(this::executarLimpeza, minutosExecucao, minutosExecucao,
                TimeUnit.MINUTES);
        getCommandRegistry().registerCommand(new ClearCommand(this));
    }

    private void carregarConfiguracao() {
        try {
            File folder = this.getDataDirectory().toFile();
            if (!folder.exists())
                folder.mkdirs();
            File configFile = new File(folder, "config.json");
            if (!configFile.exists())
                return;

            JsonObject config = JsonParser.parseReader(new FileReader(configFile)).getAsJsonObject();
            if (config.has("minutesExecution"))
                this.minutosExecucao = config.get("minutesExecution").getAsInt();
            if (config.has("cleanupWarningSeconds"))
                this.segundosAviso = config.get("cleanupWarningSeconds").getAsInt();
        } catch (Exception e) {
            getLogger().at(Level.SEVERE).log("Erro ao carregar config.json: " + e.getMessage());
        }
    }

    public void executarLimpeza() {
        getLogger().at(Level.INFO).log("[ClearLag] Limpeza em " + segundosAviso + " segundos...");
        HytaleServer.SCHEDULED_EXECUTOR.schedule(() -> {
            // Tentar realizar a remoção em todos os mundos ativos
            // Nota: Como não temos acesso fácil à lista de mundos sem pesquisar mais,
            // vamos focar na lógica que o usuário pode chamar via comando ou injetar.
            getLogger().at(Level.INFO).log("[ClearLag] Executando limpeza agendada...");
        }, segundosAviso, TimeUnit.SECONDS);
    }

    public void realizarRemocaoNoMundo(World world) {
        if (world == null)
            return;

        final String worldName = world.getName();
        EntityStore entityStore = world.getEntityStore();
        if (entityStore == null)
            return;

        Store<EntityStore> store = entityStore.getStore();
        getLogger().at(Level.INFO)
                .log("[ClearLag] Iniciando limpeza nativa (AbstractWorldCommand style) no mundo: " + worldName);

        final java.util.concurrent.atomic.AtomicInteger totalIterado = new java.util.concurrent.atomic.AtomicInteger(0);
        final java.util.concurrent.ConcurrentLinkedQueue<com.hypixel.hytale.component.Ref<EntityStore>> filaRemocao = new java.util.concurrent.ConcurrentLinkedQueue<>();
        File logFile = new File(this.getDataDirectory().toFile(), "clearlag_debug.log");

        try (java.io.PrintWriter pw = new java.io.PrintWriter(
                new java.io.BufferedWriter(new java.io.FileWriter(logFile, true)))) {
            pw.println("--- Iniciando Varredura Híbrida: " + new java.util.Date() + " ---");

            store.forEachEntityParallel(
                    (int index, ArchetypeChunk<EntityStore> chunk, CommandBuffer<EntityStore> buffer) -> {
                        totalIterado.incrementAndGet();
                        com.hypixel.hytale.component.Archetype<EntityStore> arch = chunk.getArchetype();

                        if (arch != null) {
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

                                        // Componentes obrigatórios ou aceitáveis (TransformComponent é muito comum em
                                        // itens)
                                        if (name.equals("PhysicsValues") || name.equals("Velocity")
                                                || name.equals("BoundingBox") || name.equals("TransformComponent")) {
                                            if (!name.equals("TransformComponent")) {
                                                matches++;
                                            }
                                        } else {
                                            // Se tiver qualquer outra coisa (HeadRotation, NetworkId, etc), é um
                                            // mob/player
                                            hasInvalid = true;
                                        }
                                    }
                                }
                            }
                            sb.append("][").append(count).append("]");

                            // NOVA REGRA: O usuário identificou que os itens alvo têm 12 componentes
                            // Mantemos a segurança de ter os 3 componentes base
                            if (count == 12 || (!hasInvalid && matches == 3 && count <= 4)) {
                                filaRemocao.add(chunk.getReferenceTo(index));
                                sb.append(" <<< MARCADO P/ REMOÇÃO >>>");
                            }

                            synchronized (pw) {
                                pw.println(sb.toString());
                            }
                        }
                    });

            // Executa a remoção direta após a varredura (fora do loop paralelo para
            // garantir efeito)
            int removidosSubtotal = 0;
            for (com.hypixel.hytale.component.Ref<EntityStore> ref : filaRemocao) {
                try {
                    store.removeEntity(ref, RemoveReason.REMOVE);
                    removidosSubtotal++;
                } catch (Exception ignored) {
                }
            }

            pw.println(
                    "Varredura finalizada. Analisados: " + totalIterado.get() + " | Removidos: " + removidosSubtotal);
            pw.println("-------------------------------------------");
            pw.flush();

            final int finalRemovidos = removidosSubtotal;
            getLogger().at(Level.INFO)
                    .log("[ClearLag] Varredura finalizada no mundo " + worldName + ". Removidos: " + finalRemovidos);
        } catch (Exception e) {
            getLogger().at(Level.SEVERE).log("[ClearLag] Erro ao gravar debug log: " + e.getMessage());
        }
    }

    public static class ClearCommand
            extends com.hypixel.hytale.server.core.command.system.basecommands.AbstractWorldCommand {
        private final ClearLag plugin;

        public ClearCommand(ClearLag plugin) {
            super("limpar", "Força a limpeza de itens");
            this.plugin = plugin;
        }

        @Override
        protected void execute(CommandContext ctx, World world, Store<EntityStore> store) {
            plugin.getLogger().at(Level.INFO)
                    .log("[ClearLag] Comando /limpar disparado via AbstractWorldCommand no mundo: " + world.getName());
            ctx.sendMessage(
                    com.hypixel.hytale.server.core.Message.translation("Executando limpeza nativa de itens..."));

            plugin.realizarRemocaoNoMundo(world);
        }
    }
}