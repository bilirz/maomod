package com.bilirz.maomod.entity.custom;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.world.World;

public class CreeperLegsEntity extends AbstractCreeperEntity {

    public CreeperLegsEntity(EntityType<? extends CreeperEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createCreeperLegsAttributes() {
        return AbstractCreeperEntity.createCommonAttributes();
    }
}
