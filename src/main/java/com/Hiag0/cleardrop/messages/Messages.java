package com.Hiag0.cleardrop.messages;

import com.Hiag0.cleardrop.ClearDrop;
import com.Hiag0.cleardrop.config.LanguageConfig;
import com.hypixel.hytale.server.core.Message;
import java.awt.Color;

public class Messages {

    private static LanguageConfig getLang() {
        return ClearDrop.LANG_CONFIG.get();
    }

    public static Message getStartup() {
        return Message.raw(getLang().prefix + getLang().startup).color(Color.GREEN);
    }

    public static Message scheduleInfo(int minutes) {
        return Message.raw(getLang().prefix + getLang().scheduleInfo.replace("{minutes}", String.valueOf(minutes)))
                .color(Color.GREEN);
    }

    public static Message warning(int seconds) {
        return Message.raw(getLang().prefix + getLang().warning.replace("{seconds}", String.valueOf(seconds)))
                .color(Color.YELLOW);
    }

    public static Message getCleanupStarted() {
        return Message.raw(getLang().prefix + getLang().cleanupStarted).color(Color.ORANGE);
    }

    public static Message nextCleanup(int minutes) {
        return Message.raw(getLang().prefix + getLang().nextCleanup.replace("{minutes}", String.valueOf(minutes)))
                .color(Color.GREEN);
    }

    public static Message manualCleanupStart(String worldName) {
        return Message.raw(getLang().prefix + getLang().manualCleanupStart.replace("{world}", worldName))
                .color(Color.YELLOW);
    }

    public static Message cleanupFinished(String worldName, int count) {
        return Message.raw(getLang().prefix + getLang().cleanupFinished
                .replace("{world}", worldName)
                .replace("{count}", String.valueOf(count))).color(Color.GREEN);
    }

    public static Message getForceCleanup() {
        return Message.raw(getLang().prefix + getLang().forceCleanup).color(Color.GREEN);
    }

    public static Message getInvalidNumber() {
        return Message.raw(getLang().prefix + getLang().invalidNumber).color(Color.RED);
    }

    public static Message getMustBePositive() {
        return Message.raw(getLang().prefix + getLang().mustBePositive).color(Color.RED);
    }

    public static Message getUsageAuto() {
        return Message.raw(getLang().prefix + getLang().usageAuto).color(Color.RED);
    }

    public static Message getUsageNotice() {
        return Message.raw(getLang().prefix + getLang().usageNotice).color(Color.RED);
    }

    public static Message intervalChanged(int minutes) {
        return Message.raw(getLang().prefix + getLang().intervalChanged.replace("{minutes}", String.valueOf(minutes)))
                .color(Color.GREEN);
    }

    public static Message warningChanged(int seconds) {
        return Message.raw(getLang().prefix + getLang().warningChanged.replace("{seconds}", String.valueOf(seconds)))
                .color(Color.GREEN);
    }

    public static Message getHelpTitle() {
        return Message.raw(getLang().helpTitle).color(Color.YELLOW);
    }

    public static Message getHelpNow() {
        return Message.raw(getLang().helpNow).color(Color.BLUE);
    }

    public static Message getHelpAuto() {
        return Message.raw(getLang().helpAuto).color(Color.BLUE);
    }

    public static Message getHelpNotice() {
        return Message.raw(getLang().helpNotice).color(Color.BLUE);
    }

    public static Message getAutoEnabled() {
        return Message.raw(getLang().prefix + getLang().autoEnabled).color(Color.GREEN);
    }

    public static Message getAutoDisabled() {
        return Message.raw(getLang().prefix + getLang().autoDisabled).color(Color.RED);
    }

    public static Message getNoticeEnabled() {
        return Message.raw(getLang().prefix + getLang().noticeEnabled).color(Color.GREEN);
    }

    public static Message getNoticeDisabled() {
        return Message.raw(getLang().prefix + getLang().noticeDisabled).color(Color.RED);
    }

    public static Message getCleanupFinishedEnabled() {
        return Message.raw(getLang().prefix + getLang().cleanupFinishedEnabled).color(Color.GREEN);
    }

    public static Message getCleanupFinishedDisabled() {
        return Message.raw(getLang().prefix + getLang().cleanupFinishedDisabled).color(Color.RED);
    }
}
