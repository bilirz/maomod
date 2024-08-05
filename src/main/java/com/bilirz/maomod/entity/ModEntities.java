package com.bilirz.maomod.entity;

import com.bilirz.maomod.Maomod;
import com.bilirz.maomod.entity.custom.CreeperBodyEntity;
import com.bilirz.maomod.entity.custom.CreeperHeadEntity;
import com.bilirz.maomod.entity.custom.CreeperLegsEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
    // 注册 苦力怕头 实体类型
    public static final EntityType<CreeperHeadEntity> CREEPER_HEAD = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Maomod.MOD_ID, "creeper_head"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, CreeperHeadEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6f, 1.8f)) // 设置实体的尺寸
                    .build());

    // 注册 苦力怕身体 实体
    public static final EntityType<CreeperBodyEntity> CREEPER_BODY = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Maomod.MOD_ID, "creeper_body"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, CreeperBodyEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6f, 1.8f))
                    .build());

    // 注册 苦力怕腿 实体类型
    public static final EntityType<CreeperLegsEntity> CREEPER_LEGS = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Maomod.MOD_ID, "creeper_legs"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, CreeperLegsEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6f, 1.8f))
                    .build());

    // 注册所有自定义实体的方法
    public static void registerEntities() {
    }

    // 为自定义实体注册属性
    public static void registerAttributes() {
        FabricDefaultAttributeRegistry.register(CREEPER_HEAD, CreeperHeadEntity.createCreeperHeadAttributes());
        FabricDefaultAttributeRegistry.register(CREEPER_BODY, CreeperBodyEntity.createCreeperBodyAttributes());
        FabricDefaultAttributeRegistry.register(CREEPER_LEGS, CreeperLegsEntity.createCreeperLegsAttributes());
    }
}
