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
            .append(new KeyedCodec<Boolean>("NotificationAutoEnabled", Codec.BOOLEAN),
                    (config, value, extraInfo) -> config.notificationAutoEnabled = value,
                    (config, extraInfo) -> config.notificationAutoEnabled)
            .add()
            .append(new KeyedCodec<Boolean>("NotificationNoticeEnabled", Codec.BOOLEAN),
                    (config, value, extraInfo) -> config.notificationNoticeEnabled = value,
                    (config, extraInfo) -> config.notificationNoticeEnabled)
            .add()
            .append(new KeyedCodec<Boolean>("NotificationCleanupFinishedEnabled", Codec.BOOLEAN),
                    (config, value, extraInfo) -> config.notificationCleanupFinishedEnabled = value,
                    (config, extraInfo) -> config.notificationCleanupFinishedEnabled)
            .add()
            .build();

    private int minutesExecution = 1;
    private int cleanupWarningSeconds = 5;
    private boolean notificationAutoEnabled = true;
    private boolean notificationNoticeEnabled = true;
    private boolean notificationCleanupFinishedEnabled = true;

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

    public boolean isNotificationAutoEnabled() {
        return notificationAutoEnabled;
    }

    public void setNotificationAutoEnabled(boolean notificationAutoEnabled) {
        this.notificationAutoEnabled = notificationAutoEnabled;
    }

    public boolean isNotificationNoticeEnabled() {
        return notificationNoticeEnabled;
    }

    public void setNotificationNoticeEnabled(boolean notificationNoticeEnabled) {
        this.notificationNoticeEnabled = notificationNoticeEnabled;
    }

    public boolean isNotificationCleanupFinishedEnabled() {
        return notificationCleanupFinishedEnabled;
    }

    public void setNotificationCleanupFinishedEnabled(boolean notificationCleanupFinishedEnabled) {
        this.notificationCleanupFinishedEnabled = notificationCleanupFinishedEnabled;
    }
}
