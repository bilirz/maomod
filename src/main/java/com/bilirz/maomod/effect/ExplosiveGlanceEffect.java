package com.bilirz.maomod.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class ExplosiveGlanceEffect extends StatusEffect {

    public ExplosiveGlanceEffect() {
        super(StatusEffectCategory.NEUTRAL, 0xFF5500); // 设置为中性效果，颜色为橙色
    }
}
