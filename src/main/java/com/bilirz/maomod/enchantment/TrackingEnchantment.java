package com.bilirz.maomod.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

// 追踪附魔
public class TrackingEnchantment extends Enchantment {

    // 构造函数，定义附魔的稀有度和适用的装备槽类型
    public TrackingEnchantment(Rarity weight, EquipmentSlot... slotTypes) {
        super(weight, EnchantmentTarget.WEAPON, slotTypes);
    }

    // 设置附魔的最大等级，默认使用父类的最大等级
    @Override
    public int getMaxLevel() {
        return super.getMaxLevel();
    }
}
