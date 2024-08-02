package com.bilirz.maomod.client;

import com.bilirz.maomod.Maomod;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class ModModelLayers {
    public static final EntityModelLayer CREEPER_HEAD = new EntityModelLayer(new Identifier(Maomod.MOD_ID, "creeper_head"), "main");
    public static final EntityModelLayer CREEPER_BODY = new EntityModelLayer(new Identifier(Maomod.MOD_ID, "creeper_body"), "main");
    public static final EntityModelLayer CREEPER_LEGS = new EntityModelLayer(new Identifier(Maomod.MOD_ID, "creeper_legs"), "main");
}
