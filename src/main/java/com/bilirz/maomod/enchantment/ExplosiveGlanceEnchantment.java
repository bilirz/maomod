package com.bilirz.maomod.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

// 再看一眼就会爆炸附魔
public class ExplosiveGlanceEnchantment extends Enchantment {

    public ExplosiveGlanceEnchantment(Rarity rarity, EquipmentSlot... slots) {
        super(rarity, EnchantmentTarget.BREAKABLE, slots);
    }

    @Override
    public int getMaxLevel() {
        return super.getMaxLevel();
    }
}
