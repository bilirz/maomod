package com.bilirz.maomod.entity.custom;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.world.World;

public class CreeperHeadEntity extends AbstractCreeperEntity {

    // 构造函数，调用父类 AbstractCreeperEntity 的构造函数
    public CreeperHeadEntity(EntityType<? extends CreeperEntity> entityType, World world) {
        super(entityType, world);
    }

    // 创建并返回 苦力怕头 实体的默认属性
    public static DefaultAttributeContainer.Builder createCreeperHeadAttributes() {
        return AbstractCreeperEntity.createCommonAttributes(); // 使用 AbstractCreeperEntity 通用的苦力怕属性
    }
}
