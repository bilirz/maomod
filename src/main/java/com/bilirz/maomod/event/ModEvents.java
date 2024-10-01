package com.bilirz.maomod.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class ModEvents {

    public static void registerModEvents() {
        ModBlockBreakHandler.register();  // 注册方块破坏事件处理器
        ModBlockUseHandler.register();

        // 注册飞船飞行事件处理器
        ServerTickEvents.END_WORLD_TICK.register(world -> {
            // 处理末影人效果相关事件
            world.getPlayers().forEach(EndermanEffectEventHandler::handlePlayerTick);
        });

        //
        BeheadingEventHandler.register();  // 路易十六掉头
        PlunderingEventHandler.register();  // 抢劫附魔
        ExplosiveGlanceEventHandler.register();  // 附魔再多看一眼就会爆炸的物品实体
        EntityLookAtPlayerHandler.register();  // 玩家拥有再多看一眼就会爆炸效果
    }
}