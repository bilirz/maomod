package com.bilirz.maomod.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

// 乞讨附魔
public class BegEnchantment extends Enchantment {

    protected BegEnchantment(Rarity rarity, EquipmentSlot... slots) {
        super(rarity, EnchantmentTarget.BREAKABLE, slots);
    }

    @Override
    public int getMaxLevel() {
        return super.getMaxLevel();
    }
}