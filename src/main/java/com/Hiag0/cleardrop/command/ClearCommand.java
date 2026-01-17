package com.Hiag0.cleardrop.command;

import com.Hiag0.cleardrop.command.subcommand.ClearAutoCommand;
import com.Hiag0.cleardrop.command.subcommand.ClearCleanupFinishedCommand;
import com.Hiag0.cleardrop.command.subcommand.ClearNoticeCommand;
import com.Hiag0.cleardrop.command.subcommand.ClearNowCommand;
import com.Hiag0.cleardrop.messages.Messages;
import com.Hiag0.cleardrop.service.CleanupService;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractAsyncCommand;
import java.util.concurrent.CompletableFuture;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class ClearCommand extends AbstractAsyncCommand {

    public ClearCommand(CleanupService cleanupService) {
        super("clear", "Main command of ClearDrops");

        this.addSubCommand(new ClearNowCommand(cleanupService));
        this.addSubCommand(new ClearAutoCommand(cleanupService));
        this.addSubCommand(new ClearNoticeCommand());
        this.addSubCommand(new ClearCleanupFinishedCommand());
    }

    @NonNullDecl
    @Override
    protected CompletableFuture<Void> executeAsync(CommandContext ctx) {
        sendHelp(ctx);
        return CompletableFuture.completedFuture(null);
    }

    private void sendHelp(CommandContext ctx) {
        ctx.sendMessage(Messages.getHelpTitle());
        ctx.sendMessage(Messages.getHelpNow());
        ctx.sendMessage(Messages.getHelpAuto());
        ctx.sendMessage(Messages.getHelpNotice());
    }
}
