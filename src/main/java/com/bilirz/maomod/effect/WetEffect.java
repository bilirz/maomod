package com.bilirz.maomod.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class WetEffect extends StatusEffect {

    public WetEffect() {
        super(StatusEffectCategory.NEUTRAL, 0x1F75FE); // 设置效果为中立，颜色为蓝色
    }
}
