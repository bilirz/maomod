package com.bilirz.maomod.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static com.bilirz.maomod.Maomod.MOD_ID;

public class ModEnchantments {
    public static final Enchantment TRACKING = new TrackingEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND);
    public static final Enchantment EXPANSION = new ExpansionEnchantment(Enchantment.Rarity.UNCOMMON, EquipmentSlot.MAINHAND);

    // 注册自定义附魔的方法
    public static void registerModEnchantments() {
        Registry.register(Registries.ENCHANTMENT, new Identifier(MOD_ID, "tracking"), TRACKING); // 注册追踪附魔
        Registry.register(Registries.ENCHANTMENT, new Identifier(MOD_ID, "expansion"), EXPANSION); // 注册扩容附魔
    }
}
