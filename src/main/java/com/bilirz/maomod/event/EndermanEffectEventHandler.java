package com.bilirz.maomod.event;

import com.bilirz.maomod.effect.ModEffects;
import com.bilirz.maomod.enchantment.ModEnchantments;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static net.minecraft.block.Blocks.WATER_CAULDRON;

public class EndermanEffectEventHandler {

    private static final Random random = new Random();
    private static int tickCounter = 0;
    private static final int TICKS_PER_MOVE = 5; // 每 5 个 tick 触发一次

    public static void registerEvents() {
        // 监听玩家在游戏中的每个刻
        ServerTickEvents.END_SERVER_TICK.register(server -> server.getWorlds().forEach(world -> world.getPlayers().forEach(player -> {
            // 应用潮湿效果
            if (shouldApplyWetEffect(player)) {
                player.addStatusEffect(new StatusEffectInstance(ModEffects.WET, 200));
            }

            // 检查是否应该移动物品
            if (tickCounter++ >= TICKS_PER_MOVE) {
                tickCounter = 0;

                // 如果玩家同时有潮湿效果和末影人效果
                if (player.hasStatusEffect(ModEffects.WET) && player.hasStatusEffect(ModEffects.ENDERMAN)) {
                    // 随机移动背包里的所有物品
                    randomizeAllItems(player);
                } else if (player.hasStatusEffect(ModEffects.WET)) {
                    // 只随机移动带有末影人附魔的物品
                    randomizeEndermanEnchantedItems(player);
                }
            }
        })));
    }

    private static boolean shouldApplyWetEffect(PlayerEntity player) {
        // 检查玩家是否在雨中
        if (player.getWorld().isRaining() && player.getWorld().isSkyVisible(player.getBlockPos())) {
            return true;
        }

        // 检查玩家是否在水中或被雨淋
        if (player.isSubmergedInWater() || player.isTouchingWaterOrRain()) {
            return true;
        }

        // 检查玩家是否站在装有水的炼药锅中
        return player.getWorld().getBlockState(player.getBlockPos().down()).getBlock() == WATER_CAULDRON;
    }

    private static void randomizeEndermanEnchantedItems(PlayerEntity player) {
        List<Integer> emptySlots = new ArrayList<>();

        // 收集所有空的格子位置
        for (int i = 0; i < player.getInventory().size(); i++) {
            if (player.getInventory().getStack(i).isEmpty()) {
                emptySlots.add(i);
            }
        }

        // 如果有空格子
        if (!emptySlots.isEmpty()) {
            for (int i = 0; i < player.getInventory().size(); i++) {
                ItemStack stack = player.getInventory().getStack(i);

                // 只对有末影人附魔的物品进行操作
                if (!stack.isEmpty() && EnchantmentHelper.getLevel(ModEnchantments.ENDERMAN, stack) > 0) {
                    // 从空格子中随机选择一个位置
                    int randomSlot = emptySlots.get(random.nextInt(emptySlots.size()));

                    // 确保目标位置是空的
                    if (player.getInventory().getStack(randomSlot).isEmpty()) {
                        // 将物品移动到随机选择的空位置
                        player.getInventory().setStack(randomSlot, stack);
                        // 将原位置设为空
                        player.getInventory().setStack(i, ItemStack.EMPTY);

                        // 更新空槽列表
                        emptySlots.set(emptySlots.indexOf(randomSlot), i);
                    }
                }
            }
        }
    }

    private static void randomizeAllItems(PlayerEntity player) {
        // 收集背包里的所有物品，包括空白格子
        List<ItemStack> allItems = new ArrayList<>();
        for (int i = 0; i < player.getInventory().size(); i++) {
            allItems.add(player.getInventory().getStack(i));
            player.getInventory().setStack(i, ItemStack.EMPTY);  // 清空背包
        }

        // 随机打乱物品列表
        Collections.shuffle(allItems, random);

        // 重新分配物品到随机位置
        for (int i = 0; i < allItems.size(); i++) {
            player.getInventory().setStack(i, allItems.get(i));
        }
    }
}
