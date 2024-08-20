package com.bilirz.maomod.mixin;

import com.bilirz.maomod.client.render.CustomSkeletonRenderLayer;
import com.bilirz.maomod.client.render.CustomZombieRenderLayer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin {

    // 初始化魅惑TrackedData标记
    @Inject(method = "initDataTracker", at = @At("RETURN"))
    private void initCustomDataTracker(CallbackInfo ci) {
        MobEntity mob = (MobEntity) (Object) this;

        if (mob instanceof ZombieEntity) {
            mob.getDataTracker().startTracking(CustomZombieRenderLayer.IS_HYPNO, false);
        } else if (mob instanceof SkeletonEntity) {
            mob.getDataTracker().startTracking(CustomSkeletonRenderLayer.IS_HYPNO, false);
        }
    }

    // 被魅惑的生物不会攻击玩家
    @Inject(method = "canTarget(Lnet/minecraft/entity/EntityType;)Z", at = @At("HEAD"), cancellable = true)
    private void onCanTarget(EntityType<?> type, CallbackInfoReturnable<Boolean> cir) {
        MobEntity mob = (MobEntity) (Object) this;

        if ((mob instanceof ZombieEntity zombie && zombie.getDataTracker().get(CustomZombieRenderLayer.IS_HYPNO)) ||
                (mob instanceof SkeletonEntity skeleton && skeleton.getDataTracker().get(CustomSkeletonRenderLayer.IS_HYPNO))) {
            if (type == EntityType.PLAYER) {
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        MobEntity mob = (MobEntity) (Object) this;

        if ((mob instanceof ZombieEntity zombie && zombie.getDataTracker().get(CustomZombieRenderLayer.IS_HYPNO)) ||
                (mob instanceof SkeletonEntity skeleton && skeleton.getDataTracker().get(CustomSkeletonRenderLayer.IS_HYPNO))) {

            // 阻止燃烧
            mob.extinguish();
            mob.setOnFire(false);

            LivingEntity currentTarget = mob.getTarget();

            // 阻止起内讧
            if (currentTarget == null || !currentTarget.isAlive() ||
                    (currentTarget instanceof ZombieEntity && currentTarget.getDataTracker().get(CustomZombieRenderLayer.IS_HYPNO)) ||
                    (currentTarget instanceof SkeletonEntity && currentTarget.getDataTracker().get(CustomSkeletonRenderLayer.IS_HYPNO))) {
                mob.setTarget(null);
            }

            for (PlayerEntity player : mob.getWorld().getPlayers()) {
                LivingEntity lastAttackedEntity = player.getAttacking();

                // 优先攻击玩家攻击的生物
                if (lastAttackedEntity != null && lastAttackedEntity.isAlive() &&
                        !(lastAttackedEntity instanceof ZombieEntity && lastAttackedEntity.getDataTracker().get(CustomZombieRenderLayer.IS_HYPNO)) &&
                        !(lastAttackedEntity instanceof SkeletonEntity && lastAttackedEntity.getDataTracker().get(CustomSkeletonRenderLayer.IS_HYPNO))) {
                    mob.setTarget(lastAttackedEntity);
                    return;
                }

                // 如果没有攻击目标，寻找距离最近的敌对生物
                if (mob.getTarget() == null) {
                    LivingEntity closestEntity = null;
                    double closestDistance = Double.MAX_VALUE;

                    for (LivingEntity entity : mob.getWorld().getEntitiesByClass(LivingEntity.class, mob.getBoundingBox().expand(10), entity ->
                            entity != mob && entity != player &&
                                    !(entity instanceof ZombieEntity && entity.getDataTracker().get(CustomZombieRenderLayer.IS_HYPNO)) &&
                                    !(entity instanceof SkeletonEntity && entity.getDataTracker().get(CustomSkeletonRenderLayer.IS_HYPNO)) &&
                                    entity.isAlive()
                    )) {
                        double distance = mob.squaredDistanceTo(entity);
                        if (distance < closestDistance) {
                            closestDistance = distance;
                            closestEntity = entity;
                        }
                    }

                    if (closestEntity != null) {
                        mob.setTarget(closestEntity);
                    }
                }
            }
        }
    }
}
