package com.bilirz.maomod.client.keybindings;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ModKeyBindings {

    private static KeyBinding toggleHudKey; // 切换HUD显示
    private static boolean isHudVisible = true; // HUD显示状态 (默认为可见)

    // 注册自定义按键绑定的方法
    public static void registerKeyBindings() {
        // 默认按键为 H
        toggleHudKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.maomod.toggle_creeper_hub",
                InputUtil.Type.KEYSYM, // 按键类型
                GLFW.GLFW_KEY_H, // 默认绑定按键为 H 键
                "category.maomod" // 按键所属类别
        ));

        // 注册客户端Tick事件监听器，用于检测按键状态
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleHudKey.wasPressed()) { // 当按键被按下
                isHudVisible = !isHudVisible;
            }
        });
    }

    // 获取当前HUD的显示状态
    public static boolean isHudVisible() {
        return isHudVisible;
    }
}
