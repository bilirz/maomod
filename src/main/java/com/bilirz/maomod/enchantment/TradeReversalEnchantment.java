package com.bilirz.maomod.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

// 交易反转附魔
public class TradeReversalEnchantment extends Enchantment {

    public TradeReversalEnchantment(Rarity rarity, EquipmentSlot... slots) {
        super(rarity, EnchantmentTarget.WEAPON, slots);
    }

    @Override
    public int getMaxLevel() {
        return 1; // 设定为1级附魔
    }
}
