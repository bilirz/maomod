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
    public static final Enchantment ENDERMAN = new EndermanEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.values());
    public static final Enchantment THREAT = new ThreatEnchantment(Enchantment.Rarity.UNCOMMON, EquipmentSlot.MAINHAND);
    public static final Enchantment TRADE_REVERSAL = new TradeReversalEnchantment(Enchantment.Rarity.UNCOMMON, EquipmentSlot.MAINHAND);
    public static final Enchantment BEG = new BegEnchantment(Enchantment.Rarity.UNCOMMON, EquipmentSlot.MAINHAND);
    public static final Enchantment PHANTOM = new PhantomEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND);
    public static final Enchantment GHAST = new GhastEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND);

    // 注册自定义附魔的方法
    public static void registerModEnchantments() {
        Registry.register(Registries.ENCHANTMENT, new Identifier(MOD_ID, "tracking"), TRACKING); // 注册追踪附魔
        Registry.register(Registries.ENCHANTMENT, new Identifier(MOD_ID, "expansion"), EXPANSION); // 注册扩容附魔
        Registry.register(Registries.ENCHANTMENT, new Identifier(MOD_ID, "enderman"), ENDERMAN); // 注册末影人附魔
        Registry.register(Registries.ENCHANTMENT, new Identifier(MOD_ID, "threat"), THREAT); // 注册威胁附魔
        Registry.register(Registries.ENCHANTMENT, new Identifier(MOD_ID, "trade_reversal"), TRADE_REVERSAL); // 注册交易反转附魔
        Registry.register(Registries.ENCHANTMENT, new Identifier(MOD_ID, "beg"), BEG); // 注册乞讨附魔
        Registry.register(Registries.ENCHANTMENT, new Identifier(MOD_ID, "phantom"), PHANTOM); // 注册幻翼附魔
        Registry.register(Registries.ENCHANTMENT, new Identifier(MOD_ID, "ghast"), GHAST); // 注册恶魂附魔

    }
}
