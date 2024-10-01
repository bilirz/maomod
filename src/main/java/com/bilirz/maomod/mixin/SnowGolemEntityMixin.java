package com.bilirz.maomod.mixin;

import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SnowGolemEntity.class)
public abstract class SnowGolemEntityMixin {

    @Inject(method = "initGoals", at = @At("HEAD"))
    private void initGoals(CallbackInfo ci) {
        SnowGolemEntity snowGolem = (SnowGolemEntity) (Object) this;
        SnowGolemEntityAccessor accessor = (SnowGolemEntityAccessor) snowGolem;

        // 使用 Accessor 接口来访问 protected 字段
        accessor.getGoalSelector().getGoals().clear();
        accessor.getTargetSelector().getGoals().clear();

        // 添加自定义攻击目标，设置攻击间隔为 0 tick
        accessor.getGoalSelector().add(1, new ProjectileAttackGoal(snowGolem, 1.25, 1, 10.0F)); // 0 为攻击间隔
        accessor.getGoalSelector().add(2, new WanderAroundFarGoal(snowGolem, 1.0, 1.0000001E-5F));
        accessor.getGoalSelector().add(3, new LookAtEntityGoal(snowGolem, PlayerEntity.class, 6.0F));
        accessor.getGoalSelector().add(4, new LookAroundGoal(snowGolem));
        accessor.getTargetSelector().add(1, new ActiveTargetGoal<>(snowGolem, MobEntity.class, 10, true, false, entity -> entity instanceof Monster));
    }
}