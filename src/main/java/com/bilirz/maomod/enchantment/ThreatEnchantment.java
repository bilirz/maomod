package com.bilirz.maomod.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class ThreatEnchantment extends Enchantment {

    protected ThreatEnchantment(Rarity rarity, EquipmentSlot... slots) {
        super(rarity, EnchantmentTarget.WEAPON, slots);
    }

    // 设置附魔的最大等级为3
    @Override
    public int getMaxLevel() {
        return super.getMaxLevel();
    }
}
