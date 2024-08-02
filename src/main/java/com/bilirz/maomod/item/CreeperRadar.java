package com.bilirz.maomod.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class CreeperRadar {
    private final MinecraftServer server;

    public CreeperRadar(MinecraftServer server) {
        this.server = server;
    }

    public void notifyRadarUsers(PlayerEntity user, String action) {
        if (server != null) {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                if (player != user && player.getInventory().contains(ModItems.CREEPER_RADAR.getDefaultStack())) {
                    double distance = Math.sqrt(player.squaredDistanceTo(user));
                    String formattedDistance = String.format("%.1f", distance);
                    player.sendMessage(Text.literal("玩家 " + user.getName().getString() + " " + action + "，距离 " + formattedDistance + " 格"), true);
                }
            }
        }
    }
}
