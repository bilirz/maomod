package com.bilirz.maomod.mixin;

import com.bilirz.maomod.effect.ModEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PotionItem.class)
public abstract class PotionItemMixin {

    @Inject(method = "finishUsing", at = @At("RETURN"))
    private void applyWetEffect(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        // 检查是否是普通药水
        if (stack.isOf(Items.POTION)) {
            // 在饮用药水后添加潮湿效果
            user.addStatusEffect(new StatusEffectInstance(ModEffects.WET, 200));
        }
    }
}
