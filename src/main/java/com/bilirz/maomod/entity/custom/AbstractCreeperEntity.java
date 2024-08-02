package com.bilirz.maomod.entity.custom;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public abstract class AbstractCreeperEntity extends CreeperEntity {
    private static final int EXPLODE_DELAY_TICKS = 30; // 1.5秒 (20 ticks 每秒)
    private int ticksOnGround = 0;
    private boolean soundPlayed = false;
    private boolean struckByLightning = false;

    protected AbstractCreeperEntity(EntityType<? extends CreeperEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createCommonAttributes() {
        return CreeperEntity.createCreeperAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, Double.MAX_VALUE)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.0); // 设置移动速度为 0
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isOnGround()) {
            if (!soundPlayed) {
                this.playCreeperSound();
                soundPlayed = true;
            }
            ticksOnGround++;
            if (ticksOnGround >= EXPLODE_DELAY_TICKS) {
                this.explode();
            }
        } else {
            ticksOnGround = 0; // 如果实体不在地面上，重置计数器
            soundPlayed = false; // 如果实体不在地面上，重置 soundPlayed 标志
        }
    }

    @Override
    public void onStruckByLightning(ServerWorld world, LightningEntity lightning) {
        super.onStruckByLightning(world, lightning);
        this.struckByLightning = true; // 被闪电击中时设置标志位
    }

    private void explode() {
        if (!this.getWorld().isClient) {
            float explosionPower = this.struckByLightning ? 4.0F : 2.0F; // 判断是否被闪电击中
            this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), explosionPower, World.ExplosionSourceType.MOB);
            this.discard(); // 爆炸后移除实体
        }
    }

    private void playCreeperSound() {
        this.getWorld().playSound(
                null,
                this.getX(),
                this.getY(),
                this.getZ(),
                SoundEvents.ENTITY_CREEPER_PRIMED,
                SoundCategory.HOSTILE,
                1.0F,
                1.0F
        );
    }
}
