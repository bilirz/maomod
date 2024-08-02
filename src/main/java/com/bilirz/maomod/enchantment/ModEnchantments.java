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

    public static void registerModEnchantments() {
        Registry.register(Registries.ENCHANTMENT, new Identifier(MOD_ID, "tracking"), TRACKING);
        Registry.register(Registries.ENCHANTMENT, new Identifier(MOD_ID, "expansion"), EXPANSION);
    }
}
