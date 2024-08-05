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
    // 定义并注册自定义物品

    // 苦力怕弓相关注册
    public static final Item CREEPER_QUIVER = registerItems("creeper_quiver", new CreeperQuiverItem(new Item.Settings().maxCount(1))); // 苦力怕箭袋
    public static final Item SPAWNER_CREEPER_QUIVER = registerItems("spawner_creeper_quiver", new SpawnerCreeperQuiverItem(new Item.Settings().maxCount(1))); // 刷怪苦力怕箭袋
    public static final Item CREEPER_BOW = registerItems("creeper_bow", new CreeperBowItem(new Item.Settings().maxDamage(384))); // 苦力怕弓
    public static final Item CREEPER_RADAR = registerItems("creeper_radar", new Item(new Item.Settings().maxCount(1))); // 苦力怕雷达
    public static final Item CREEPER_EMP = registerItems("creeper_emp", new Item(new Item.Settings().maxCount(1))); // 苦力怕emp（暂时没用）
    public static final Item SPAWNER_SHARD = registerItems("spawner_shard", new Item(new Item.Settings().maxCount(64))); // 刷怪笼碎片
    public static final Item CAT_MISSILE = registerItems("cat_missile", new CatMissileItem(new Item.Settings().maxCount(1))); // 猫猫反导
    public static final Item CREEPER_ITEM = registerItems("creeper_item", new CatMissileItem(new Item.Settings().maxCount(1))); // 视频演示苦力怕

    // 将自定义物品添加到物品组中
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

    // 注册单个物品的方法
    public static Item registerItems(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(Maomod.MOD_ID, name), item);
    }

    // 注册所有自定义物品的方法，并将它们添加到指定的物品组中
    public static void registerModItems() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(ModItems::addItemsToItemGroup);
    }
}
