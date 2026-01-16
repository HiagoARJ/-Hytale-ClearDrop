package com.Hiag0.clearlag;

import com.Hiag0.clearlag.command.ClearCommand;
import com.Hiag0.clearlag.config.ClearLagConfig;
import com.Hiag0.clearlag.config.LanguageConfig;
import com.Hiag0.clearlag.service.CleanupService;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;

public class ClearLag extends JavaPlugin {

    public static com.hypixel.hytale.server.core.util.Config<ClearLagConfig> CONFIG;
    public static com.hypixel.hytale.server.core.util.Config<LanguageConfig> LANG_CONFIG;

    private CleanupService cleanupService;

    public ClearLag(JavaPluginInit init) {
        super(init);
        CONFIG = this.withConfig("ClearLag", ClearLagConfig.CODEC);
        LANG_CONFIG = this.withConfig("language", LanguageConfig.CODEC);
    }

    @Override
    protected void start() {
        CONFIG.save(); // Salva/Carrega
        LANG_CONFIG.save();

        // Inicializar Serviço de Limpeza usando o config carregado
        this.cleanupService = new CleanupService(CONFIG.get(), this);

        // Agendar Tarefas
        cleanupService.scheduleCleanup();

        // Registrar Comandos
        getCommandRegistry().registerCommand(new ClearCommand(cleanupService));
    }
}
