package com.bilirz.maomod.event;

public class ModEvents {

    public static void registerModEvents() {
        ModBlockBreakHandler.register();       // 注册方块破坏事件处理器
        EndermanEffectEventHandler.registerEvents(); // 注册潮湿效果相关事件处理器
    }
}
