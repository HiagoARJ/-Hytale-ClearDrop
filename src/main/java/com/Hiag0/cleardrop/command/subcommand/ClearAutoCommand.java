package com.Hiag0.cleardrop.command.subcommand;

import com.Hiag0.cleardrop.ClearDrop;
import com.Hiag0.cleardrop.messages.Messages;
import com.Hiag0.cleardrop.service.CleanupService;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractAsyncCommand;
import java.awt.Color;
import java.util.concurrent.CompletableFuture;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class ClearAutoCommand extends AbstractAsyncCommand {

    private final CleanupService cleanupService;
    private final RequiredArg<Integer> minutesArg;

    public ClearAutoCommand(CleanupService cleanupService) {
        super("auto", "Set the automatic cleanup interval");
        this.cleanupService = cleanupService;
        this.minutesArg = this.withRequiredArg("minutes", "Interval in minutes", ArgTypes.INTEGER);
    }

    @NonNullDecl
    @Override
    protected CompletableFuture<Void> executeAsync(CommandContext ctx) {
        int minutes = minutesArg.get(ctx);
        if (minutes <= 0) {
            ctx.sendMessage(Messages.getMustBePositive().color(Color.RED));
            return CompletableFuture.completedFuture(null);
        }

        ClearDrop.CONFIG.get().setMinutesExecution(minutes);
        ClearDrop.CONFIG.save();
        cleanupService.reschedule();

        ctx.sendMessage(Messages.intervalChanged(minutes).color(Color.GREEN));
        return CompletableFuture.completedFuture(null);
    }
}
