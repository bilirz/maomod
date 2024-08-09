package com.bilirz.maomod.entity;

import com.bilirz.maomod.entity.creeper.CreeperBodyEntity;
import com.bilirz.maomod.entity.creeper.CreeperHeadEntity;
import com.bilirz.maomod.entity.creeper.CreeperLegsEntity;
import com.bilirz.maomod.entity.villager.AirVillagerEntity;
import com.bilirz.maomod.entity.villager.BlazeVillagerEntity;
import com.bilirz.maomod.entity.villager.EndermanVillagerEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static com.bilirz.maomod.Maomod.MOD_ID;

public class ModEntities {
    // 注册 苦力怕头 实体类型
    public static final EntityType<CreeperHeadEntity> CREEPER_HEAD = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(MOD_ID, "creeper_head"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, CreeperHeadEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6f, 1.8f)) // 设置实体的尺寸
                    .build());

    // 注册 苦力怕身体 实体
    public static final EntityType<CreeperBodyEntity> CREEPER_BODY = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(MOD_ID, "creeper_body"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, CreeperBodyEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6f, 1.8f))
                    .build());

    // 注册 苦力怕腿 实体类型
    public static final EntityType<CreeperLegsEntity> CREEPER_LEGS = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(MOD_ID, "creeper_legs"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, CreeperLegsEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6f, 1.8f))
                    .build());

    public static final EntityType<BlazeVillagerEntity> BLAZE_VILLAGER = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(MOD_ID, "blaze_villager"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, BlazeVillagerEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6f, 1.95f))
                    .build()
    );

    public static final EntityType<EndermanVillagerEntity> ENDERMAN_VILLAGER = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(MOD_ID, "enderman_villager"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, EndermanVillagerEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6f, 1.95f))
                    .build()
    );

    public static final EntityType<AirVillagerEntity> AIR_VILLAGER = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(MOD_ID, "air_villager"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, AirVillagerEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6f, 1.95f))
                    .build()
    );
    // 注册所有自定义实体的方法
    public static void registerEntities() {
    }

    // 为自定义实体注册属性
    public static void registerAttributes() {
        FabricDefaultAttributeRegistry.register(CREEPER_HEAD, CreeperHeadEntity.createCreeperHeadAttributes());
        FabricDefaultAttributeRegistry.register(CREEPER_BODY, CreeperBodyEntity.createCreeperBodyAttributes());
        FabricDefaultAttributeRegistry.register(CREEPER_LEGS, CreeperLegsEntity.createCreeperLegsAttributes());

        FabricDefaultAttributeRegistry.register(BLAZE_VILLAGER, BlazeVillagerEntity.createBlazeVillagerAttributes());
        FabricDefaultAttributeRegistry.register(ENDERMAN_VILLAGER, EndermanVillagerEntity.createEndermanVillagerAttributes());
        FabricDefaultAttributeRegistry.register(AIR_VILLAGER, AirVillagerEntity.createAirVillagerAttributes());
    }
}
