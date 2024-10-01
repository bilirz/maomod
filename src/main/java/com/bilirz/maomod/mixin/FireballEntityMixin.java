package com.bilirz.maomod.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FireballEntity.class)
public abstract class FireballEntityMixin {

    // 取消对雪傀儡的直接伤害
    @Inject(method = "onEntityHit", at = @At("HEAD"), cancellable = true)
    private void onEntityHit(EntityHitResult entityHitResult, CallbackInfo ci) {
        Entity hitEntity = entityHitResult.getEntity();

        // 如果击中的是雪傀儡，取消伤害
        if (hitEntity instanceof SnowGolemEntity) {
            ci.cancel();
        }
    }

    // 取消对雪傀儡的爆炸伤害
    @Inject(method = "onCollision", at = @At("HEAD"), cancellable = true)
    private void onCollision(HitResult hitResult, CallbackInfo ci) {
        FireballEntity fireball = (FireballEntity) (Object) this;
        Entity owner = fireball.getOwner();

        // TODO: 取消伤害要检测是否是火球发射者
        if (hitResult instanceof EntityHitResult) {
            // 检查火球的所有者是否是玩家
            if (owner instanceof PlayerEntity) {
                // 取消对发射者的伤害，但保留爆炸效果
                ci.cancel();
            }
        }

        double explosionRadius = 4.0; // 默认火球的爆炸半径

        // 获取火球的爆炸范围内的实体
        for (Entity entity : fireball.getWorld().getOtherEntities(fireball, fireball.getBoundingBox().expand(explosionRadius))) {
            // 如果实体是雪傀儡，设置其无敌状态来避免爆炸伤害
            if (entity instanceof SnowGolemEntity) {
                entity.setInvulnerable(true);
            }
        }
    }

    @Inject(method = "onCollision", at = @At("TAIL"))
    private void afterExplosion(HitResult hitResult, CallbackInfo ci) {
        FireballEntity fireball = (FireballEntity) (Object) this;
        double explosionRadius = 4.0;

        // 恢复雪傀儡的普通状态（取消无敌）
        for (Entity entity : fireball.getWorld().getOtherEntities(fireball, fireball.getBoundingBox().expand(explosionRadius))) {
            if (entity instanceof SnowGolemEntity) {
                entity.setInvulnerable(false);
            }
        }
    }
}