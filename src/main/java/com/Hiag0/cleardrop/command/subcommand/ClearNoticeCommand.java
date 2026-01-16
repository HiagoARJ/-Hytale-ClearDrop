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

    private final RequiredArg<Integer> secondsArg;

    public ClearNoticeCommand() {
        super("notice", "Set the warning time");
        this.secondsArg = this.withRequiredArg("seconds", "Seconds of warning", ArgTypes.INTEGER);
    }

    @NonNullDecl
    @Override
    protected CompletableFuture<Void> executeAsync(CommandContext ctx) {
        int seconds = secondsArg.get(ctx);
        if (seconds <= 0) {
            ctx.sendMessage(Messages.getMustBePositive().color(Color.RED));
            return CompletableFuture.completedFuture(null);
        }

        ClearDrop.CONFIG.get().setCleanupWarningSeconds(seconds);
        ClearDrop.CONFIG.save();

        ctx.sendMessage(Messages.warningChanged(seconds).color(Color.GREEN));
        return CompletableFuture.completedFuture(null);
    }
}
