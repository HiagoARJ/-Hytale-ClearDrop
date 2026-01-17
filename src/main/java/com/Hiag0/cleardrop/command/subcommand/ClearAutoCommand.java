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
    private final RequiredArg<String> args;

    public ClearAutoCommand(CleanupService cleanupService) {
        super("auto", "Set cleanup interval or toggle notifications");
        this.cleanupService = cleanupService;
        this.args = this.withRequiredArg("value", "Minutes or true/false", ArgTypes.STRING);
    }

    @NonNullDecl
    @Override
    protected CompletableFuture<Void> executeAsync(CommandContext ctx) {
        String value = args.get(ctx);

        // Try to parse as integer (minutes)
        try {
            int minutes = Integer.parseInt(value);
            if (minutes <= 0) {
                ctx.sendMessage(Messages.getMustBePositive().color(Color.RED));
                return CompletableFuture.completedFuture(null);
            }

            ClearDrop.CONFIG.get().setMinutesExecution(minutes);
            ClearDrop.CONFIG.save();
            cleanupService.reschedule();

            ctx.sendMessage(Messages.intervalChanged(minutes).color(Color.GREEN));
            return CompletableFuture.completedFuture(null);
        } catch (NumberFormatException e) {
            // Not a number, check for boolean
            if (value.equalsIgnoreCase("true")) {
                ClearDrop.CONFIG.get().setNotificationAutoEnabled(true);
                ClearDrop.CONFIG.save();
                ctx.sendMessage(Messages.getAutoEnabled());
            } else if (value.equalsIgnoreCase("false")) {
                ClearDrop.CONFIG.get().setNotificationAutoEnabled(false);
                ClearDrop.CONFIG.save();
                ctx.sendMessage(Messages.getAutoDisabled());
            } else {
                ctx.sendMessage(Messages.getUsageAuto());
            }
        }
        return CompletableFuture.completedFuture(null);
    }
}
