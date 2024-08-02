
package com.bilirz.maomod.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.CreeperEntity;

public class CreeperWithEnchantments {
    public final CreeperEntity creeper;
    public final LivingEntity owner;
    public final double baseSpeedMultiplier;
    public final boolean hasChanneling;
    public final boolean hasTracking;
    public final boolean isMultishot;
    public int lightningCount;
    public LivingEntity shooter;

    public CreeperWithEnchantments(CreeperEntity creeper, LivingEntity owner, double baseSpeedMultiplier, boolean hasChanneling, boolean hasTracking, boolean isMultishot) {
        this.creeper = creeper;
        this.owner = owner;
        this.baseSpeedMultiplier = baseSpeedMultiplier;
        this.hasChanneling = hasChanneling;
        this.hasTracking = hasTracking;
        this.isMultishot = isMultishot;
        this.lightningCount = 0;
        this.shooter = owner;
    }

    public CreeperEntity getCreeper() {
        return creeper;
    }

    public LivingEntity getShooter() {
        return shooter;
    }
}