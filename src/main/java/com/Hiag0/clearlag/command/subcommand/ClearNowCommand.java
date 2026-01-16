package com.Hiag0.clearlag.command.subcommand;

import com.Hiag0.clearlag.messages.Messages;
import com.Hiag0.clearlag.service.CleanupService;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractAsyncCommand;
import java.awt.Color;
import java.util.concurrent.CompletableFuture;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class ClearNowCommand extends AbstractAsyncCommand {

    private final CleanupService cleanupService;

    public ClearNowCommand(CleanupService cleanupService) {
        super("now", "Force cleanup immediately");
        this.cleanupService = cleanupService;
    }

    @NonNullDecl
    @Override
    protected CompletableFuture<Void> executeAsync(CommandContext ctx) {
        ctx.sendMessage(Messages.getForceCleanup().color(Color.GREEN));
        cleanupService.executarLimpezaGeral();
        return CompletableFuture.completedFuture(null);
    }
}
