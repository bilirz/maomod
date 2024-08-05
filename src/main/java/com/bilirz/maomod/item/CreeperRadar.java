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

    // 通知拥有雷达的玩家关于指定玩家的行动
    public void notifyRadarUsers(PlayerEntity user, String action) {
        if (server != null) {
            // 遍历所有玩家，查找拥有 CreeperRadar 的玩家
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                if (player != user && player.getInventory().contains(ModItems.CREEPER_RADAR.getDefaultStack())) {
                    double distance = Math.sqrt(player.squaredDistanceTo(user)); // 计算玩家之间的距离
                    String formattedDistance = String.format("%.1f", distance); // 格式化距离为小数点后一位
                    // 向雷达用户发送消息
                    player.sendMessage(Text.literal("玩家 " + user.getName().getString() + " " + action + "，距离 " + formattedDistance + " 格"), true);
                }
            }
        }
    }
}
