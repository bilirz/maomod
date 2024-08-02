package com.bilirz.maomod.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpawnerCreeperQuiverItem extends CreeperQuiverItem {
    private static final int SPAWN_INTERVAL = 200; // 10秒钟 (20 ticks)

    public SpawnerCreeperQuiverItem(Settings settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient && entity instanceof PlayerEntity) {
            NbtCompound nbt = stack.getOrCreateNbt();
            int creeperCount = nbt.getInt("CreeperCount");
            int maxCreepers = BASE_MAX_CREEPERS + getExpansionLevel(stack);
            long lastSpawnTime = nbt.getLong("LastSpawnTime");

            if (creeperCount < maxCreepers) {
                if (lastSpawnTime == 0) {
                    nbt.putLong("LastSpawnTime", world.getTime());
                } else if (world.getTime() >= lastSpawnTime + SPAWN_INTERVAL) {
                    nbt.putInt("CreeperCount", creeperCount + 1);
                    nbt.putLong("LastSpawnTime", world.getTime());
                }
            } else {
                nbt.putLong("LastSpawnTime", 0);
            }

            // 调整 CustomModelData 以反映当前 CreeperCount
            int customModelData = Math.min(creeperCount, maxCreepers);
            stack.getOrCreateNbt().putInt("CustomModelData", customModelData);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound nbt = stack.getOrCreateNbt();
        int creeperCount = nbt.getInt("CreeperCount");
        int maxCreepers = BASE_MAX_CREEPERS + getExpansionLevel(stack);

        tooltip.add(Text.translatable("tooltip.quiver.creeper_count", creeperCount, maxCreepers));

        if (world != null && nbt.contains("LastSpawnTime")) {
            long lastSpawnTime = nbt.getLong("LastSpawnTime");

            if (creeperCount < maxCreepers && lastSpawnTime != 0) {
                long currentTime = world.getTime();
                long nextSpawnTime = lastSpawnTime + SPAWN_INTERVAL;
                long timeUntilNextSpawn = nextSpawnTime - currentTime;

                if (timeUntilNextSpawn > 0) {
                    tooltip.add(Text.translatable("tooltip.quiver.next_spawn", timeUntilNextSpawn / 20).formatted(Formatting.GRAY));
                } else {
                    tooltip.add(Text.translatable("tooltip.quiver.ready_to_spawn").formatted(Formatting.GREEN));
                }
            } else if (creeperCount >= maxCreepers) {
                tooltip.add(Text.translatable("tooltip.quiver.reached_max").formatted(Formatting.RED));
            }
        }
    }
}
