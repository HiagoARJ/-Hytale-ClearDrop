package com.Hiag0.cleardrop.command.subcommand;

import com.Hiag0.cleardrop.ClearDrop;
import com.Hiag0.cleardrop.messages.Messages;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractAsyncCommand;
import java.util.concurrent.CompletableFuture;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class ClearCleanupFinishedCommand extends AbstractAsyncCommand {

    private final RequiredArg<Boolean> valueArg;

    public ClearCleanupFinishedCommand() {
        super("cleanupfinished", "Toggle cleanup finished notification");
        this.valueArg = this.withRequiredArg("enabled", "true/false", ArgTypes.BOOLEAN);
    }

    @NonNullDecl
    @Override
    protected CompletableFuture<Void> executeAsync(CommandContext ctx) {
        boolean enabled = valueArg.get(ctx);

        ClearDrop.CONFIG.get().setNotificationCleanupFinishedEnabled(enabled);
        ClearDrop.CONFIG.save();

        if (enabled) {
            ctx.sendMessage(Messages.getCleanupFinishedEnabled());
        } else {
            ctx.sendMessage(Messages.getCleanupFinishedDisabled());
        }

        return CompletableFuture.completedFuture(null);
    }
}
