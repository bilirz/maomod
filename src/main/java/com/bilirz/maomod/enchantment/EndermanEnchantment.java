package com.bilirz.maomod.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

// 末影人附魔
public class EndermanEnchantment extends Enchantment {

    // 构造函数，BREAKABLE代表所有可破坏的物品
    public EndermanEnchantment(Rarity rarity, EquipmentSlot... slots) {
        super(rarity, EnchantmentTarget.BREAKABLE, slots);
    }

    // 设置附魔的最大等级，默认使用父类的最大等级
    @Override
    public int getMaxLevel() {
        return super.getMaxLevel();
    }
}
