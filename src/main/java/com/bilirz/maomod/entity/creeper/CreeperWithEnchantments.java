package com.bilirz.maomod.entity.creeper;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.CreeperEntity;

public class CreeperWithEnchantments {
    // 定义带附魔效果的苦力怕实体及其属性
    public final CreeperEntity creeper; // 苦力怕实体
    public final LivingEntity owner; // 拥有者 (final)
    public final double baseSpeedMultiplier; // 基础速度倍增器
    public final boolean hasChanneling; // 是否具有引雷附魔
    public final boolean hasTracking; // 是否具有追踪附魔
    public final boolean isMultishot; // 是否多重射击
    public int lightningCount; // 闪电击中次数
    public LivingEntity shooter; // 发射苦力怕的实体

    // 构造函数，初始化苦力怕及其附魔效果
    public CreeperWithEnchantments(CreeperEntity creeper, LivingEntity owner, double baseSpeedMultiplier, boolean hasChanneling, boolean hasTracking, boolean isMultishot) {
        this.creeper = creeper;
        this.owner = owner;
        this.baseSpeedMultiplier = baseSpeedMultiplier;
        this.hasChanneling = hasChanneling;
        this.hasTracking = hasTracking;
        this.isMultishot = isMultishot;
        this.lightningCount = 0;
        this.shooter = owner; // 默认发射者为拥有者
    }

    // 获取苦力怕实体
    public CreeperEntity getCreeper() {
        return creeper;
    }

    // 获取发射者
    public LivingEntity getShooter() {
        return shooter;
    }
}
