package com.bilirz.maomod.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

// 末影人附魔
public class EndermanEnchantment extends Enchantment {

    public EndermanEnchantment(Rarity rarity, EquipmentSlot... slots) {
        super(rarity, EnchantmentTarget.BREAKABLE, slots);
    }

    @Override
    public int getMaxLevel() {
        return super.getMaxLevel();
    }
}
