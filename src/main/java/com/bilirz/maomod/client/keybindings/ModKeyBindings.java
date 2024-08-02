package com.bilirz.maomod.client.keybindings;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ModKeyBindings {

    private static KeyBinding toggleHudKey;
    private static boolean isHudVisible = true;

    public static void registerKeyBindings() {
        toggleHudKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.maomod.toggle_creeper_hub",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_H,
                "category.maomod"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleHudKey.wasPressed()) {
                isHudVisible = !isHudVisible;
            }
        });
    }

    public static boolean isHudVisible() {
        return isHudVisible;
    }
}
