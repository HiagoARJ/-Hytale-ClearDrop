package com.Hiag0.clearlag.command;

import com.Hiag0.clearlag.service.CleanupService;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractWorldCommand;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;

public class ClearCommand extends AbstractWorldCommand {

    private final CleanupService cleanupService;

    public ClearCommand(CleanupService cleanupService) {
        super("limpar", "Força a limpeza de itens");
        this.cleanupService = cleanupService;
    }

    @Override
    protected void execute(CommandContext ctx, World world, Store<EntityStore> store) {
        ctx.sendMessage(Message.translation("Executando limpeza nativa de itens..."));
        cleanupService.realizarRemocaoNoMundo(world);
    }
}
