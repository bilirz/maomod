package com.bilirz.maomod.client;

import com.bilirz.maomod.Maomod;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class ModModelLayers {
    // 定义 CreeperHead 模型层
    public static final EntityModelLayer CREEPER_HEAD = new EntityModelLayer(new Identifier(Maomod.MOD_ID, "creeper_head"), "main");

    // 定义 CreeperBody 模型层
    public static final EntityModelLayer CREEPER_BODY = new EntityModelLayer(new Identifier(Maomod.MOD_ID, "creeper_body"), "main");

    // 定义 CreeperLegs 模型层
    public static final EntityModelLayer CREEPER_LEGS = new EntityModelLayer(new Identifier(Maomod.MOD_ID, "creeper_legs"), "main");
}
