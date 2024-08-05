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
    private static final int SPAWN_INTERVAL = 200; // 生成间隔时间为10秒 (200 ticks)

    // 构造函数，继承自 CreeperQuiverItem
    public SpawnerCreeperQuiverItem(Settings settings) {
        super(settings);
    }

    // 每个游戏Tick调用，用于处理箭袋在玩家物品栏中的行为
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient && entity instanceof PlayerEntity) {
            NbtCompound nbt = stack.getOrCreateNbt();
            int creeperCount = nbt.getInt("CreeperCount");
            int maxCreepers = BASE_MAX_CREEPERS + getExpansionLevel(stack);
            long lastSpawnTime = nbt.getLong("LastSpawnTime");

            // 如果箭袋中苦力怕数量未达到上限，则按时间间隔生成苦力怕
            if (creeperCount < maxCreepers) {
                if (lastSpawnTime == 0) {
                    nbt.putLong("LastSpawnTime", world.getTime());
                } else if (world.getTime() >= lastSpawnTime + SPAWN_INTERVAL) {
                    nbt.putInt("CreeperCount", creeperCount + 1);
                    nbt.putLong("LastSpawnTime", world.getTime());
                }
            } else {
                nbt.putLong("LastSpawnTime", 0); // 达到上限时重置生成时间
            }

            // 调整 CustomModelData 以反映当前 CreeperCount
            int customModelData = Math.min(creeperCount, maxCreepers);
            stack.getOrCreateNbt().putInt("CustomModelData", customModelData);
        }
    }

    // 显示物品工具提示，包括当前的苦力怕数量和生成时间信息
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound nbt = stack.getOrCreateNbt();
        int creeperCount = nbt.getInt("CreeperCount");
        int maxCreepers = BASE_MAX_CREEPERS + getExpansionLevel(stack);

        tooltip.add(Text.translatable("tooltip.quiver.creeper_count", creeperCount, maxCreepers)); // 显示当前苦力怕数量

        if (world != null && nbt.contains("LastSpawnTime")) {
            long lastSpawnTime = nbt.getLong("LastSpawnTime");

            // 显示距离下次生成的时间或生成已准备就绪的提示
            if (creeperCount < maxCreepers && lastSpawnTime != 0) {
                long currentTime = world.getTime();
                long nextSpawnTime = lastSpawnTime + SPAWN_INTERVAL;
                long timeUntilNextSpawn = nextSpawnTime - currentTime;

                if (timeUntilNextSpawn > 0) {
                    tooltip.add(Text.translatable("tooltip.quiver.next_spawn", timeUntilNextSpawn / 20).formatted(Formatting.GRAY)); // 显示倒计时时间
                } else {
                    tooltip.add(Text.translatable("tooltip.quiver.ready_to_spawn").formatted(Formatting.GREEN)); // 显示准备刷新一个苦力怕
                }
            } else if (creeperCount >= maxCreepers) {
                tooltip.add(Text.translatable("tooltip.quiver.reached_max").formatted(Formatting.RED)); // 显示箭袋已满
            }
        }
    }
}
