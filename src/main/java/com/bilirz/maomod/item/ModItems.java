package com.bilirz.maomod.item;

import com.bilirz.maomod.Maomod;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item CREEPER_QUIVER = registerItems("creeper_quiver", new CreeperQuiverItem(new Item.Settings().maxCount(1)));
    public static final Item SPAWNER_CREEPER_QUIVER = registerItems("spawner_creeper_quiver", new SpawnerCreeperQuiverItem(new Item.Settings().maxCount(1)));
    public static final Item CREEPER_BOW = registerItems("creeper_bow", new CreeperBowItem(new Item.Settings().maxDamage(384)));
    public static final Item CREEPER_RADAR = registerItems("creeper_radar", new Item(new Item.Settings().maxCount(1)));
    public static final Item CREEPER_EMP = registerItems("creeper_emp", new Item(new Item.Settings().maxCount(1)));
    public static final Item SPAWNER_SHARD = registerItems("spawner_shard", new Item(new Item.Settings().maxCount(64)));
    public static final Item CAT_MISSILE = registerItems("cat_missile", new CatMissileItem(new Item.Settings().maxCount(1)));
    public static final Item CREEPER_ITEM = registerItems("creeper_item", new CatMissileItem(new Item.Settings().maxCount(1)));

    private static void addItemsToItemGroup(FabricItemGroupEntries fabricItemGroupEntries) {
        fabricItemGroupEntries.add(CREEPER_QUIVER);
        fabricItemGroupEntries.add(SPAWNER_CREEPER_QUIVER);
        fabricItemGroupEntries.add(CREEPER_BOW);
        fabricItemGroupEntries.add(CREEPER_RADAR);
        fabricItemGroupEntries.add(CREEPER_EMP);
        fabricItemGroupEntries.add(SPAWNER_SHARD);
        fabricItemGroupEntries.add(CAT_MISSILE);
        fabricItemGroupEntries.add(CREEPER_ITEM);
    }

    public static Item registerItems(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(Maomod.MOD_ID, name), item);
    }

    public static void registerModItems() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(ModItems::addItemsToItemGroup);
    }
}
