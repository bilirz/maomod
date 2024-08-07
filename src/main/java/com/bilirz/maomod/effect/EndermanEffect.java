package com.bilirz.maomod.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class EndermanEffect extends StatusEffect {

    public EndermanEffect() {
        super(StatusEffectCategory.NEUTRAL, 0x5500FF); // 设置为中性效果，颜色为紫色
    }
}
