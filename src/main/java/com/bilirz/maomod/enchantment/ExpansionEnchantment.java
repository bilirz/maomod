package com.bilirz.maomod.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

// 扩容附魔
public class ExpansionEnchantment extends Enchantment {

    // 构造函数，定义附魔的稀有度和适用的装备槽类型
    public ExpansionEnchantment(Rarity weight, EquipmentSlot... slotTypes) {
        super(weight, EnchantmentTarget.WEARABLE, slotTypes);
    }

    // 设置附魔的最大等级为3
    @Override
    public int getMaxLevel() {
        return 3;
    }
}
