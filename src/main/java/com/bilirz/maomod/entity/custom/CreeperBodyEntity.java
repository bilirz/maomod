package com.bilirz.maomod.entity.custom;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.world.World;

public class CreeperBodyEntity extends AbstractCreeperEntity {

    public CreeperBodyEntity(EntityType<? extends CreeperEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createCreeperBodyAttributes() {
        return AbstractCreeperEntity.createCommonAttributes();
    }
}
