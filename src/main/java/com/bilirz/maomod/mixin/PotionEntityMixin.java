package com.bilirz.maomod.mixin;

import com.bilirz.maomod.effect.ModEffects;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static com.bilirz.maomod.item.ModItems.PING_PONG;

@Mixin(PotionEntity.class)
public class PotionEntityMixin {

    @Inject(method = "onCollision", at = @At("HEAD"), cancellable = true)
    private void onCollision(HitResult hitResult, CallbackInfo ci) {
        PotionEntity potionEntity = (PotionEntity) (Object) this;
        World world = potionEntity.getWorld();

        // 如果是实体碰撞
        if (hitResult.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityHitResult = (EntityHitResult) hitResult;
            Entity entity = entityHitResult.getEntity();

            if (entity instanceof PlayerEntity player) {
                ItemStack mainHandStack = player.getMainHandStack();
                ItemStack offHandStack = player.getOffHandStack();

                // 检查玩家是否手持乒乓球拍
                if (mainHandStack.isOf(PING_PONG) || offHandStack.isOf(PING_PONG)) {
                    // 获取玩家的视角方向
                    Vec3d lookDirection = player.getRotationVec(1.0F);

                    // 检查附魔情况
                    ItemStack pingPongItem = mainHandStack.isOf(PING_PONG) ? mainHandStack : offHandStack;
                    int strengthLevel = EnchantmentHelper.getLevel(Enchantments.POWER, pingPongItem);

                    // 设置初速度，根据力量等级递增
                    double baseSpeed = 0.5;
                    double speedIncrement = 1.3;
                    double initialSpeed = baseSpeed + (strengthLevel * speedIncrement);
                    Vec3d newVelocity = lookDirection.normalize().multiply(initialSpeed);
                    potionEntity.setVelocity(newVelocity);

                    // 设置药水瓶的重力影响
                    potionEntity.setNoGravity(false);

                    // 减少耐久度
                    pingPongItem.damage(1, player, (p) -> p.sendToolBreakStatus(player.getActiveHand()));

                    // 取消默认碰撞效果
                    ci.cancel();
                }
            }
        }

        // 如果世界不是客户端，施加潮湿效果
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
