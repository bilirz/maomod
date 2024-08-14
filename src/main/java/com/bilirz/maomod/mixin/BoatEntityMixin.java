package com.bilirz.maomod.mixin;

import com.bilirz.maomod.enchantment.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.MinecraftClient;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BoatEntity.class)
public class BoatEntityMixin {

    @Unique
    private static final TrackedData<Boolean> PHANTOM_ENCHANTED = DataTracker.registerData(BoatEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    @Unique
    private static final TrackedData<Boolean> GHAST_ENCHANTED = DataTracker.registerData(BoatEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    @Unique
    private int burnTime = 0; // 燃烧时间计数器

    // 初始化数据跟踪器，注册自定义的附魔状态
    @Inject(method = "initDataTracker", at = @At("HEAD"))
    private void initEnchantmentDataTrackers(CallbackInfo ci) {
        BoatEntity boat = (BoatEntity)(Object)this;
        boat.getDataTracker().startTracking(PHANTOM_ENCHANTED, false);
        boat.getDataTracker().startTracking(GHAST_ENCHANTED, false);
    }

    // 在船被放置时检查玩家持有的附魔，并应用相应的效果
    @Inject(method = "<init>(Lnet/minecraft/world/World;DDD)V", at = @At("TAIL"))
    private void onBoatPlaced(World world, double x, double y, double z, CallbackInfo ci) {
        BoatEntity boat = (BoatEntity)(Object)this;

        // 获取最近的玩家，假设是放置船的玩家
        PlayerEntity player = world.getClosestPlayer(x, y, z, 5.0, false);

        if (player != null) {
            ItemStack itemStack = player.getMainHandStack();

            // 检查幻翼附魔
            int phantomLevel = EnchantmentHelper.getLevel(ModEnchantments.PHANTOM, itemStack);
            if (phantomLevel > 0) {
                boat.getDataTracker().set(PHANTOM_ENCHANTED, true);
                if (world.isDay()) {
                    boat.setOnFireFor(100); // 设置船燃烧（100 tick，约5秒）
                    burnTime = 100; // 记录燃烧时间
                }
            }

            // 检查恶魂附魔
            int ghastLevel = EnchantmentHelper.getLevel(ModEnchantments.GHAST, itemStack);
            if (ghastLevel > 0) {
                boat.getDataTracker().set(GHAST_ENCHANTED, true);
            }
        }
    }

    // 每 tick 检查并处理附魔效果
    @Inject(method = "tick", at = @At("HEAD"))
    private void checkEnchantments(CallbackInfo ci) {
        BoatEntity boat = (BoatEntity)(Object)this;

        if (boat.getDataTracker().get(PHANTOM_ENCHANTED)) {
            if (boat.getWorld().isDay()) {
                // 白天时处理燃烧效果
                handleBurning(boat);
            } else {
                // 夜晚时熄灭火焰
                boat.extinguish();
                burnTime = 0; // 重置燃烧时间
            }
            handleFlight(boat); // 处理飞行效果
        }

        if (boat.getDataTracker().get(GHAST_ENCHANTED)) {
            handleGhastAttack(boat); // 处理恶魂附魔效果
        }

        // 设置船无敌
        boat.setInvulnerable(true);
    }

    // 处理燃烧效果，如果燃烧时间结束，销毁船
    @Unique
    private void handleBurning(BoatEntity boat) {
        if (burnTime > 0) {
            burnTime--;

            if (burnTime == 0) {
                boat.remove(Entity.RemovalReason.DISCARDED); // 燃烧结束后销毁船
            }
        }
    }

    // 处理船的飞行能力
    @Unique
    private void handleFlight(BoatEntity boat) {
        if (boat.hasPassengers()) {
            Entity passenger = boat.getFirstPassenger();
            if (passenger instanceof PlayerEntity player) {
                Vec3d velocity = boat.getVelocity();
                MinecraftClient client = MinecraftClient.getInstance();

                if (client.player != null && client.player.equals(player)) {
                    if (client.options.jumpKey.isPressed()) {
                        boat.setVelocity(velocity.x, 0.2, velocity.z); // 上升
                    }
                }
            }
        }
    }

    // 处理恶魂附魔效果，允许船发射火球
    @Unique
    private void handleGhastAttack(BoatEntity boat) {
        if (boat.hasPassengers()) {
            Entity passenger = boat.getFirstPassenger();
            if (passenger instanceof PlayerEntity player) {
                MinecraftClient client = MinecraftClient.getInstance();
                if (client.player != null && client.player.equals(player)) {
                    // 当玩家按下使用键（右键）并且主手或副手持有火焰弹时
                    if (client.options.useKey.isPressed() && isHoldingFireCharge(player)) {
                        ItemStack fireChargeStack = getFireChargeStack(player);

                        // 火球实例
                        Vec3d lookVec = player.getRotationVec(1.0F);
                        FireballEntity fireball = new FireballEntity(boat.getWorld(), player, lookVec.x, lookVec.y, lookVec.z, 5); // 最后一个参数是火球的爆炸威力

                        // 设置火球的初始位置和方向
                        fireball.refreshPositionAndAngles(boat.getX(), boat.getY() + 1.0, boat.getZ(), boat.getYaw(), boat.getPitch());
                        boat.getWorld().spawnEntity(fireball);

                        // 消耗背包里的一个火焰弹
                        fireChargeStack.decrement(1);
                    }
                }
            }
        }
    }

    // 检查玩家的主手或副手是否持有火焰弹
    @Unique
    private boolean isHoldingFireCharge(PlayerEntity player) {
        return player.getMainHandStack().getItem() == Items.FIRE_CHARGE ||
                player.getOffHandStack().getItem() == Items.FIRE_CHARGE;
    }

    // 返回主手或副手的火焰弹
    @Unique
    private ItemStack getFireChargeStack(PlayerEntity player) {
        if (player.getMainHandStack().getItem() == Items.FIRE_CHARGE) {
            return player.getMainHandStack();
        } else {
            return player.getOffHandStack();
        }
    }
}
