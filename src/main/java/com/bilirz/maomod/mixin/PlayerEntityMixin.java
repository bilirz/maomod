package com.bilirz.maomod.mixin;

import com.bilirz.maomod.enchantment.ModEnchantments;
import com.bilirz.maomod.effect.ModEffects;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

    @Inject(method = "eatFood", at = @At("RETURN"))
    private void onEatFood(World world, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        // 检查食物是否带有末影人附魔
        if (EnchantmentHelper.getLevel(ModEnchantments.ENDERMAN, stack) > 0) {
            // 给玩家应用末影人效果，持续30秒
            PlayerEntity player = (PlayerEntity)(Object)this;
            player.addStatusEffect(new StatusEffectInstance(ModEffects.ENDERMAN, 600, 0));
        }
    }
}