package com.Hiag0.cleardrop.config;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class ClearDropConfig {

    public static final BuilderCodec<ClearDropConfig> CODEC = BuilderCodec
            .builder(ClearDropConfig.class, ClearDropConfig::new)
            .append(new KeyedCodec<Integer>("MinutesExecution", Codec.INTEGER),
                    (config, value, extraInfo) -> config.minutesExecution = value,
                    (config, extraInfo) -> config.minutesExecution)
            .add()
            .append(new KeyedCodec<Integer>("CleanupWarningSeconds", Codec.INTEGER),
                    (config, value, extraInfo) -> config.cleanupWarningSeconds = value,
                    (config, extraInfo) -> config.cleanupWarningSeconds)
            .add()
            .build();

    private int minutesExecution = 1;
    private int cleanupWarningSeconds = 5;

    public ClearDropConfig() {
    }

    public int getMinutesExecution() {
        return minutesExecution;
    }

    public int getCleanupWarningSeconds() {
        return cleanupWarningSeconds;
    }

    public void setMinutesExecution(int minutesExecution) {
        this.minutesExecution = minutesExecution;
    }

    public void setCleanupWarningSeconds(int cleanupWarningSeconds) {
        this.cleanupWarningSeconds = cleanupWarningSeconds;
    }
}
