package com.bilirz.maomod.mixin;

import com.bilirz.maomod.effect.ModEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PotionEntity.class)
public abstract class PotionEntityMixin {

    @Inject(method = "onCollision", at = @At("HEAD"))
    private void onPotionCollision(HitResult hitResult, CallbackInfo ci) {
        PotionEntity potionEntity = (PotionEntity) (Object) this;
        World world = potionEntity.getWorld();

        if (!world.isClient) {
            // 获取范围内的所有生物实体
            List<LivingEntity> affectedEntities = world.getEntitiesByClass(LivingEntity.class, potionEntity.getBoundingBox().expand(4.0D, 2.0D, 4.0D), entity -> true);
            for (LivingEntity entity : affectedEntities) {
                if (entity.isAlive()) {
                    // 为范围内的所有生物实体添加潮湿效果
                    entity.addStatusEffect(new StatusEffectInstance(ModEffects.WET, 200));
                }
            }
        }
    }
}