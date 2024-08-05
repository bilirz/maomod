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
    private static final int EXPLODE_DELAY_TICKS = 30; // 爆炸延迟为 1.5秒 (30 ticks)
    private int ticksOnGround = 0; // 实体在地面上的时间计数
    private boolean soundPlayed = false; // 标志是否已经播放过启动音效
    private boolean struckByLightning = false; // 标志是否被闪电击中

    // 构造函数，调用 CreeperEntity 的构造函数
    protected AbstractCreeperEntity(EntityType<? extends CreeperEntity> entityType, World world) {
        super(entityType, world);
    }

    // 创建并返回通用的苦力怕属性
    public static DefaultAttributeContainer.Builder createCommonAttributes() {
        return CreeperEntity.createCreeperAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, Double.MAX_VALUE) // 设置最大生命值为无限大
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.0); // 设置移动速度为 0
    }

    // 每个游戏Tick调用，用于处理实体的行为
    @Override
    public void tick() {
        super.tick();
        if (this.isOnGround()) {
            if (!soundPlayed) {
                this.playCreeperSound();
                soundPlayed = true;
            }
            ticksOnGround++; // 增加在地面上的时间计数
            if (ticksOnGround >= EXPLODE_DELAY_TICKS) {
                this.explode(); // 达到延迟时间后爆炸
            }
        } else {
            ticksOnGround = 0;
            soundPlayed = false;
        }
    }

    // 当实体被闪电击中时调用
    @Override
    public void onStruckByLightning(ServerWorld world, LightningEntity lightning) {
        super.onStruckByLightning(world, lightning);
        this.struckByLightning = true;
    }

    // 处理实体的爆炸行为
    private void explode() {
        if (!this.getWorld().isClient) {
            float explosionPower = this.struckByLightning ? 4.0F : 2.0F; // 根据是否被闪电击中设置爆炸威力
            this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), explosionPower, World.ExplosionSourceType.MOB);
            this.discard();
        }
    }

    // 播放苦力怕启动音效
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
