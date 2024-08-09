package com.bilirz.maomod.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class TradeReversalEnchantment extends Enchantment {

    public TradeReversalEnchantment(Rarity rarity, EquipmentSlot... slots) {
        super(rarity, EnchantmentTarget.WEAPON, slots);
    }

    // 设置附魔的最大等级为3
    @Override
    public int getMaxLevel() {
        return 1; // 设定为1级附魔
    }
}
