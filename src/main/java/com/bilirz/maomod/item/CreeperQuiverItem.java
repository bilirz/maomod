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

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        NbtCompound nbt = itemStack.getOrCreateNbt();

        if (!world.isClient) {
            int creeperCount = nbt.getInt("CreeperCount");
            int maxCreepers = BASE_MAX_CREEPERS + getExpansionLevel(itemStack);
            if (creeperCount >= maxCreepers) {
                player.sendMessage(Text.literal("箭袋已满"), true);
                return TypedActionResult.fail(itemStack);
            }

            CreeperEntity creeper = findNearestCreeper(player);
            if (creeper != null) {
                creeper.remove(Entity.RemovalReason.DISCARDED);
                nbt.putInt("CreeperCount", creeperCount + 1);
                player.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 1.0F, 1.0F);
                return TypedActionResult.success(itemStack);
            }
        }
        return TypedActionResult.pass(itemStack);
    }

    private CreeperEntity findNearestCreeper(PlayerEntity player) {
        List<CreeperEntity> creepers = player.getWorld().getEntitiesByClass(CreeperEntity.class, player.getBoundingBox().expand(5.0), e -> true);
        return creepers.isEmpty() ? null : creepers.get(0);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound nbt = stack.getOrCreateNbt();
        int creeperCount = nbt.getInt("CreeperCount");
        int maxCreepers = BASE_MAX_CREEPERS + getExpansionLevel(stack);
        tooltip.add(Text.translatable("tooltip.quiver.creeper_count", creeperCount, maxCreepers));
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        return nbt.getInt("CreeperCount") > 0;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        int maxCreepers = BASE_MAX_CREEPERS + getExpansionLevel(stack);
        return (int) (((double) nbt.getInt("CreeperCount") / (double) maxCreepers) * 13.0);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return 0x00FF00;
    }

    int getExpansionLevel(ItemStack stack) {
        return EnchantmentHelper.getLevel(ModEnchantments.EXPANSION, stack) * 5;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient) {
            NbtCompound nbt = stack.getOrCreateNbt();
            int creeperCount = nbt.getInt("CreeperCount");
            int customModelData = Math.min(creeperCount, BASE_MAX_CREEPERS); // 让 customModelData 不超过 BASE_MAX_CREEPERS
            stack.getOrCreateNbt().putInt("CustomModelData", customModelData);
        }
    }

    public int getCreeperCount(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        return nbt.getInt("CreeperCount");
    }

    public void decreaseCreeperCount(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        int creeperCount = nbt.getInt("CreeperCount");

        if (creeperCount > 0) {
            nbt.putInt("CreeperCount", creeperCount - 1);
        }
    }
}
