package com.Hiag0.clearlag;

import net.hytale.api.HytaleServer; // Assuming this is the entry point or similar
import net.hytale.api.plugin.Plugin;
import net.hytale.api.scheduler.Scheduler;
import net.hytale.api.Server;
import net.hytale.api.World;
import net.hytale.api.entities.Entity;
import net.hytale.api.entities.EntityType;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;

public class ClearLag extends Plugin {

    private final Logger logger;
    private final Server server; // Instância do servidor para acessar os mundos
    
    // Variáveis de configuração com valores padrão (caso o arquivo falhe)
    private int minutosExecucao = 5;
    private int segundosAviso = 5;

    public ClearLag(Logger logger, Server server) {
        this.logger = logger;
        this.server = server;
    }

    @Override
    public void onEnable() {
        // 1. Carregar a configuração ANTES de iniciar o timer
        carregarConfiguracao();

        logger.info("Mod iniciado. Limpeza agendada a cada " + minutosExecucao + " minutos.");

        // 2. Usar a variável 'minutosExecucao' lida do arquivo
        server.getScheduler().runTaskTimer(this, this::executarLimpeza, minutosExecucao, minutosExecucao, TimeUnit.MINUTES);
    }

    private void carregarConfiguracao() {
        try {
            // Obtém a pasta de dados do plugin (ex: /server/plugins/ItemCleaner/)
            File folder = this.getDataFolder(); 
            if (!folder.exists()) folder.mkdirs();

            File configFile = new File(folder, "config.json");

            // Se o arquivo não existe, cria um padrão (ou você pode copiar de resources)
            if (!configFile.exists()) {
                logger.warn("Config não encontrada. Usando valores padrão.");
                return; // Usa os valores padrão definidos no topo da classe
            }

            // Lê o arquivo JSON
            JsonObject config = JsonParser.parseReader(new FileReader(configFile)).getAsJsonObject();

            // Atualiza as variáveis
            if (config.has("minutesExecution")) {
                this.minutosExecucao = config.get("minutesExecution").getAsInt();
            }
            if (config.has("cleanupWarningSeconds")) {
                this.segundosAviso = config.get("cleanupWarningSeconds").getAsInt();
            }

        } catch (Exception e) {
            logger.error("Erro ao carregar config.json: " + e.getMessage());
        }
    }

    private void executarLimpeza() {
        // 1. Avisar os jogadores antes de limpar
        server.broadcastMessage("§c[Aviso] §fLimpando itens em " + segundosAviso + " segundos...");

        // For para cada segundo restante e ir diminuindo, contagem regressiva
        for (int i = segundosAviso; i > 0; i--) {
            server.broadcastMessage("§c[Aviso] §fLimpando itens em " + i + " segundos...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        server.getScheduler().runTaskLater(this, () -> {
            int itensRemovidos = 0;

            // 2. Iterar sobre todos os mundos e entidades
            for (World world : server.getWorlds()) {
                // Pega todas as entidades do mundo
                for (Entity entity : world.getEntities()) {
                    
                    // 3. Verificar se é um ITEM dropado (não queremos deletar monstros ou jogadores!)
                    if (entity.getType() == EntityType.DROPPED_ITEM) {
                        entity.remove(); // Remove a entidade
                        itensRemovidos++;
                    }
                }
            }

            // 4. Feedback final
            if (itensRemovidos > 0) {
                server.broadcastMessage("§a[Limpeza] §fForam removidos " + itensRemovidos + " itens do chão.");
                logger.info("Limpeza concluída. Total removido: " + itensRemovidos);
            }
        }, segundosAviso, TimeUnit.SECONDS);
    }
}