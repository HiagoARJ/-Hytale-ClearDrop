package com.Hiag0.cleardrop.config;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class LanguageConfig {

        public static final BuilderCodec<LanguageConfig> CODEC = BuilderCodec
                        .builder(LanguageConfig.class, LanguageConfig::new)
                        .append(new KeyedCodec<String>("Prefix", Codec.STRING),
                                        (config, value, extraInfo) -> config.prefix = value,
                                        (config, extraInfo) -> config.prefix)
                        .add()
                        .append(new KeyedCodec<String>("Startup", Codec.STRING),
                                        (config, value, extraInfo) -> config.startup = value,
                                        (config, extraInfo) -> config.startup)
                        .add()
                        .append(new KeyedCodec<String>("ScheduleInfo", Codec.STRING),
                                        (config, value, extraInfo) -> config.scheduleInfo = value,
                                        (config, extraInfo) -> config.scheduleInfo)
                        .add()
                        .append(new KeyedCodec<String>("Warning", Codec.STRING),
                                        (config, value, extraInfo) -> config.warning = value,
                                        (config, extraInfo) -> config.warning)
                        .add()
                        .append(new KeyedCodec<String>("CleanupStarted", Codec.STRING),
                                        (config, value, extraInfo) -> config.cleanupStarted = value,
                                        (config, extraInfo) -> config.cleanupStarted)
                        .add()
                        .append(new KeyedCodec<String>("NextCleanup", Codec.STRING),
                                        (config, value, extraInfo) -> config.nextCleanup = value,
                                        (config, extraInfo) -> config.nextCleanup)
                        .add()
                        .append(new KeyedCodec<String>("ManualCleanupStart", Codec.STRING),
                                        (config, value, extraInfo) -> config.manualCleanupStart = value,
                                        (config, extraInfo) -> config.manualCleanupStart)
                        .add()
                        .append(new KeyedCodec<String>("CleanupFinished", Codec.STRING),
                                        (config, value, extraInfo) -> config.cleanupFinished = value,
                                        (config, extraInfo) -> config.cleanupFinished)
                        .add()
                        .append(new KeyedCodec<String>("ForceCleanup", Codec.STRING),
                                        (config, value, extraInfo) -> config.forceCleanup = value,
                                        (config, extraInfo) -> config.forceCleanup)
                        .add()
                        .append(new KeyedCodec<String>("InvalidNumber", Codec.STRING),
                                        (config, value, extraInfo) -> config.invalidNumber = value,
                                        (config, extraInfo) -> config.invalidNumber)
                        .add()
                        .append(new KeyedCodec<String>("IntervalChanged", Codec.STRING),
                                        (config, value, extraInfo) -> config.intervalChanged = value,
                                        (config, extraInfo) -> config.intervalChanged)
                        .add()
                        .append(new KeyedCodec<String>("WarningChanged", Codec.STRING),
                                        (config, value, extraInfo) -> config.warningChanged = value,
                                        (config, extraInfo) -> config.warningChanged)
                        .add()
                        .append(new KeyedCodec<String>("HelpTitle", Codec.STRING),
                                        (config, value, extraInfo) -> config.helpTitle = value,
                                        (config, extraInfo) -> config.helpTitle)
                        .add()
                        .append(new KeyedCodec<String>("HelpNow", Codec.STRING),
                                        (config, value, extraInfo) -> config.helpNow = value,
                                        (config, extraInfo) -> config.helpNow)
                        .add()
                        .append(new KeyedCodec<String>("HelpAuto", Codec.STRING),
                                        (config, value, extraInfo) -> config.helpAuto = value,
                                        (config, extraInfo) -> config.helpAuto)
                        .add()
                        .append(new KeyedCodec<String>("HelpNotice", Codec.STRING),
                                        (config, value, extraInfo) -> config.helpNotice = value,
                                        (config, extraInfo) -> config.helpNotice)
                        .add()
                        .append(new KeyedCodec<String>("MustBePositive", Codec.STRING),
                                        (config, value, extraInfo) -> config.mustBePositive = value,
                                        (config, extraInfo) -> config.mustBePositive)
                        .add()
                        .append(new KeyedCodec<String>("UsageAuto", Codec.STRING),
                                        (config, value, extraInfo) -> config.usageAuto = value,
                                        (config, extraInfo) -> config.usageAuto)
                        .add()
                        .append(new KeyedCodec<String>("UsageNotice", Codec.STRING),
                                        (config, value, extraInfo) -> config.usageNotice = value,
                                        (config, extraInfo) -> config.usageNotice)
                        .add()
                        .build();

        public String prefix = "[Clear Drop] ";
        public String startup = "ClearDrop started. Cleanup scheduled.";
        public String scheduleInfo = "Cleanup every {minutes} minutes.";
        public String warning = "Cleanup in {seconds} seconds...";
        public String cleanupStarted = "Running scheduled cleanup...";
        public String nextCleanup = "Next cleanup in {minutes} minutes.";
        public String manualCleanupStart = "Starting cleanup in world: {world}";
        public String cleanupFinished = "Cleanup finished in world {world}. Items removed: {count}";
        public String forceCleanup = "Forcing cleanup...";
        public String invalidNumber = "Invalid number.";
        public String intervalChanged = "Cleanup interval changed to {minutes} minutes.";
        public String warningChanged = "Warning time changed to {seconds} seconds.";
        public String helpTitle = "--- ClearDrop Help ---";
        public String helpNow = "/clear now - Force cleanup now";
        public String helpAuto = "/clear auto <min> - Set cleanup interval";
        public String helpNotice = "/clear notice <sec> - Set warning time";
        public String mustBePositive = "The value must be greater than 0.";
        public String usageAuto = "Usage: /clear auto <minutes>";
        public String usageNotice = "Usage: /clear notice <seconds>";

        public LanguageConfig() {
        }
}
