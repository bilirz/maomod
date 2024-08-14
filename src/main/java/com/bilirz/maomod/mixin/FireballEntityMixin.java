package com.bilirz.maomod.mixin;

import net.minecraft.entity.Entity;
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
    // TODO: 取消伤害要检测是否是火球发射者
    @Inject(method = "onCollision", at = @At("HEAD"), cancellable = true)
    private void onCollision(HitResult hitResult, CallbackInfo ci) {
        FireballEntity fireball = (FireballEntity) (Object) this;
        Entity owner = fireball.getOwner();

        if (hitResult instanceof EntityHitResult) {
            // 检查火球的所有者是否是玩家
            if (owner instanceof PlayerEntity) {
                // 取消对发射者的伤害，但保留爆炸效果
                ci.cancel();
            }
        }
    }
}
