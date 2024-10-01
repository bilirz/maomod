package com.bilirz.maomod.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static com.bilirz.maomod.Maomod.MOD_ID;

public class ModEffects {
    public static final StatusEffect WET = new WetEffect();
    public static final StatusEffect ENDERMAN = new EndermanEffect();
    public static final StatusEffect EXPLOSIVE_GLANCE = new ExplosiveGlanceEffect(); // 新增的效果

    public static void registerEffects() {
        Registry.register(Registries.STATUS_EFFECT, new Identifier(MOD_ID, "wet"), WET); // 注册潮湿效果
        Registry.register(Registries.STATUS_EFFECT, new Identifier(MOD_ID, "enderman"), ENDERMAN); // 注册末影人效果
        Registry.register(Registries.STATUS_EFFECT, new Identifier(MOD_ID, "explosive_glance"), EXPLOSIVE_GLANCE); // 注册爆炸凝视效果
    }
}
