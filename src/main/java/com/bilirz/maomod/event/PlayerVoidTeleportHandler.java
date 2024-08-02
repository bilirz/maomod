package com.bilirz.maomod.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static com.bilirz.maomod.Maomod.MOD_ID;

public class PlayerVoidTeleportHandler {

    private static final Identifier CREEPER_DIMENSION_ID = new Identifier(MOD_ID, "creeper_dimension");

    public static void register() {
        ServerTickEvents.START_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                if (player.getWorld().getRegistryKey().getValue().equals(CREEPER_DIMENSION_ID)) {
                    World world = player.getWorld();
                    if (player.getY() < world.getBottomY()) {
                        BlockPos teleportPos = new BlockPos((int) player.getX(), world.getTopY() - 300, (int) player.getZ());
                        player.teleport((ServerWorld) world, teleportPos.getX(), teleportPos.getY(), teleportPos.getZ(), player.getYaw(), player.getPitch());
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 400, 0));
                    }
                }
            }
        });
    }
}
