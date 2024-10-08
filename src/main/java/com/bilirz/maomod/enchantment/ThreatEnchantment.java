package com.bilirz.maomod.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

// 威胁附魔
public class ThreatEnchantment extends Enchantment {

    protected ThreatEnchantment(Rarity rarity, EquipmentSlot... slots) {
        super(rarity, EnchantmentTarget.WEAPON, slots);
    }

    @Override
    public int getMaxLevel() {
        return super.getMaxLevel();
    }
}
