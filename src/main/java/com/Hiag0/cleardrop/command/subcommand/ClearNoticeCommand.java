package com.Hiag0.cleardrop.command.subcommand;

import com.Hiag0.cleardrop.ClearDrop;
import com.Hiag0.cleardrop.messages.Messages;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractAsyncCommand;
import java.awt.Color;
import java.util.concurrent.CompletableFuture;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class ClearNoticeCommand extends AbstractAsyncCommand {

    private final RequiredArg<String> args;

    public ClearNoticeCommand() {
        super("notice", "Set warning time or toggle notifications");
        this.args = this.withRequiredArg("value", "Seconds or true/false", ArgTypes.STRING);
    }

    @NonNullDecl
    @Override
    protected CompletableFuture<Void> executeAsync(CommandContext ctx) {
        String value = args.get(ctx);

        try {
            int seconds = Integer.parseInt(value);
            if (seconds <= 0) {
                ctx.sendMessage(Messages.getMustBePositive().color(Color.RED));
                return CompletableFuture.completedFuture(null);
            }

            ClearDrop.CONFIG.get().setCleanupWarningSeconds(seconds);
            ClearDrop.CONFIG.save();

            ctx.sendMessage(Messages.warningChanged(seconds).color(Color.GREEN));
        } catch (NumberFormatException e) {
            if (value.equalsIgnoreCase("true")) {
                ClearDrop.CONFIG.get().setNotificationNoticeEnabled(true);
                ClearDrop.CONFIG.save();
                ctx.sendMessage(Messages.getNoticeEnabled());
            } else if (value.equalsIgnoreCase("false")) {
                ClearDrop.CONFIG.get().setNotificationNoticeEnabled(false);
                ClearDrop.CONFIG.save();
                ctx.sendMessage(Messages.getNoticeDisabled());
            } else {
                ctx.sendMessage(Messages.getUsageNotice());
            }
        }
        return CompletableFuture.completedFuture(null);
    }
}
