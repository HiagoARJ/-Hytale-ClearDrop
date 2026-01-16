package com.Hiag0.cleardrop;

import com.Hiag0.cleardrop.command.ClearCommand;
import com.Hiag0.cleardrop.config.ClearDropConfig;
import com.Hiag0.cleardrop.config.LanguageConfig;
import com.Hiag0.cleardrop.service.CleanupService;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;

public class ClearDrop extends JavaPlugin {

    public static com.hypixel.hytale.server.core.util.Config<ClearDropConfig> CONFIG;
    public static com.hypixel.hytale.server.core.util.Config<LanguageConfig> LANG_CONFIG;

    private CleanupService cleanupService;

    public ClearDrop(JavaPluginInit init) {
        super(init);
        CONFIG = this.withConfig("ClearDrop", ClearDropConfig.CODEC);
        LANG_CONFIG = this.withConfig("language", LanguageConfig.CODEC);
    }

    @Override
    protected void start() {
        CONFIG.save(); // Save/Load
        LANG_CONFIG.save();

        // Initialize Cleanup Service using loaded config
        this.cleanupService = new CleanupService(CONFIG.get(), this);

        // Schedule Tasks
        cleanupService.scheduleCleanup();

        // Register Commands
        getCommandRegistry().registerCommand(new ClearCommand(cleanupService));
    }
}
