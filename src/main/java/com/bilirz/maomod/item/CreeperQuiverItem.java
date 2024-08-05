package com.bilirz.maomod.item;

import com.bilirz.maomod.enchantment.ModEnchantments;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CreeperQuiverItem extends Item {
    public static final int BASE_MAX_CREEPERS = 5; // 基础装填最大值

    public CreeperQuiverItem(Settings settings) {
        super(settings);
    }

    // 当玩家使用箭袋时调用此方法
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        NbtCompound nbt = itemStack.getOrCreateNbt();

        if (!world.isClient) {
            int creeperCount = nbt.getInt("CreeperCount");
            int maxCreepers = BASE_MAX_CREEPERS + getExpansionLevel(itemStack); // 计算最大可装填的苦力怕数量
            if (creeperCount >= maxCreepers) {
                player.sendMessage(Text.literal("箭袋已满"), true);
                return TypedActionResult.fail(itemStack);
            }

            // 查找最近的苦力怕，并将其装填进箭袋
            CreeperEntity creeper = findNearestCreeper(player);
            if (creeper != null) {
                creeper.remove(Entity.RemovalReason.DISCARDED); // 移除苦力怕实体
                nbt.putInt("CreeperCount", creeperCount + 1); // 更新箭袋中的苦力怕数量
                player.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 1.0F, 1.0F); // 播放拾取音效
                return TypedActionResult.success(itemStack);
            }
        }
        return TypedActionResult.pass(itemStack);
    }

    // 查找玩家附近的最近的苦力怕
    private CreeperEntity findNearestCreeper(PlayerEntity player) {
        List<CreeperEntity> creepers = player.getWorld().getEntitiesByClass(CreeperEntity.class, player.getBoundingBox().expand(5.0), e -> true);
        return creepers.isEmpty() ? null : creepers.get(0);
    }

    // 在物品工具提示中显示箭袋内的苦力怕数量
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound nbt = stack.getOrCreateNbt();
        int creeperCount = nbt.getInt("CreeperCount");
        int maxCreepers = BASE_MAX_CREEPERS + getExpansionLevel(stack); // 计算最大可装填的苦力怕数量
        tooltip.add(Text.translatable("tooltip.quiver.creeper_count", creeperCount, maxCreepers)); // 添加工具提示
    }

    // 检查物品栏是否显示状态栏（苦力怕数量条）
    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        return nbt.getInt("CreeperCount") > 0;
    }

    // 返回显示状态栏的进度
    @Override
    public int getItemBarStep(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        int maxCreepers = BASE_MAX_CREEPERS + getExpansionLevel(stack); // 计算最大可装填的苦力怕数量
        return (int) (((double) nbt.getInt("CreeperCount") / (double) maxCreepers) * 13.0); // 计算状态条进度
    }

    // 返回状态栏的颜色
    @Override
    public int getItemBarColor(ItemStack stack) {
        return 0x00FF00; // 状态栏的颜色为绿色
    }

    // 获取扩展等级，根据附魔计算额外的苦力怕装填数量
    int getExpansionLevel(ItemStack stack) {
        return EnchantmentHelper.getLevel(ModEnchantments.EXPANSION, stack) * 5;
    }

    // 每个游戏Tick调用，用于更新物品栏中的箭袋数据
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient) {
            NbtCompound nbt = stack.getOrCreateNbt();
            int creeperCount = nbt.getInt("CreeperCount");
            int customModelData = Math.min(creeperCount, BASE_MAX_CREEPERS); // 让 customModelData 不超过 BASE_MAX_CREEPERS
            stack.getOrCreateNbt().putInt("CustomModelData", customModelData); // 更新自定义模型数据
        }
    }

    // 获取箭袋中的苦力怕数量
    public int getCreeperCount(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        return nbt.getInt("CreeperCount");
    }

    // 减少箭袋中的苦力怕数量
    public void decreaseCreeperCount(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        int creeperCount = nbt.getInt("CreeperCount");

        if (creeperCount > 0) {
            nbt.putInt("CreeperCount", creeperCount - 1);
        }
    }
}
