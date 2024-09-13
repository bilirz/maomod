package com.bilirz.maomod.item;

import com.bilirz.maomod.Maomod;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroup {

    // 定义并注册自定义物品组
    public static final ItemGroup MAO_GROUP = Registry.register(Registries.ITEM_GROUP,
            new Identifier(Maomod.MOD_ID, "mao_group"), // 注册物品组ID
            FabricItemGroup.builder()
                    .displayName(Text.translatable("itemGroup.mao_group"))  // 设置物品组名称
                    .icon(() -> new ItemStack(ModItems.PING_PONG)) // 设置物品组图标
                    .entries((displayContext, entries) -> { // 添加物品到物品组中
                        entries.add(ModItems.PING_PONG);
                        entries.add(ModItems.CREEPER_QUIVER);
                        entries.add(ModItems.SPAWNER_CREEPER_QUIVER);
                        entries.add(ModItems.CREEPER_BOW);
                        entries.add(ModItems.CREEPER_RADAR);
                        entries.add(ModItems.CREEPER_EMP);
                        entries.add(ModItems.SPAWNER_SHARD);
                        entries.add(ModItems.CAT_MISSILE);
                        entries.add(ModItems.DURABLE_BOWL);
                        entries.add(ModItems.DURABLE_BUCKET);
                        entries.add(ModItems.HYPNO_SHROOM);
                        entries.add(ModItems.DOOM_SHROOM);
                    }).build());

    // 注册物品组的方法
    public static void registerModItemsGroup() {
    }
}
