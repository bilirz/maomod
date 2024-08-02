package com.bilirz.maomod.client.render;

import com.bilirz.maomod.client.keybindings.ModKeyBindings;
import com.bilirz.maomod.item.CreeperQuiverItem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class QuiverHudRenderer implements HudRenderCallback {
    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {
        if (!ModKeyBindings.isHudVisible()) return;

        MinecraftClient client = MinecraftClient.getInstance();
        PlayerEntity player = client.player;

        if (player == null) return;

        int totalCreeperCount = getTotalCreeperCount(player);
        String message = Text.translatable("hud.quiver.creeper_count", totalCreeperCount).getString();

        int x = 10;
        int y = client.getWindow().getScaledHeight() - 10;

        drawContext.drawText(client.textRenderer, message, x, y, Formatting.GREEN.getColorValue(), true);
    }

    private int getTotalCreeperCount(PlayerEntity player) {
        int totalCreeperCount = 0;

        for (ItemStack stack : player.getInventory().main) {
            if (stack.getItem() instanceof CreeperQuiverItem) {
                totalCreeperCount += stack.getOrCreateNbt().getInt("CreeperCount");
            }
        }

        return totalCreeperCount;
    }
}
