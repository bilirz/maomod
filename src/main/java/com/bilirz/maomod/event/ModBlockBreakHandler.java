package com.bilirz.maomod.event;

import com.bilirz.maomod.item.ModItems;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;

public class ModBlockBreakHandler {

    // 注册方块破坏事件处理器
    public static void register() {
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, entity) -> {
            if (player instanceof ServerPlayerEntity serverPlayer) {
                // 检查玩家是否不在创造模式，并且破坏的方块是刷怪笼
                if (serverPlayer.interactionManager.getGameMode() != GameMode.CREATIVE && state.getBlock() == Blocks.SPAWNER) {
                    // 掉落刷怪笼碎片
                    world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ModItems.SPAWNER_SHARD, 1)));
                }
            }
        });
    }
}
