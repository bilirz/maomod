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
    public static final ItemGroup MAO_GROUP = Registry.register(Registries.ITEM_GROUP,
            new Identifier(Maomod.MOD_ID, "mao_group"),
            FabricItemGroup.builder().displayName(Text.translatable("itemGroup.mao_group"))
                    .icon(() -> new ItemStack(ModItems.CREEPER_QUIVER)).entries((displayContext, entries) -> {
                        entries.add(ModItems.CREEPER_QUIVER);
                        entries.add(ModItems.SPAWNER_CREEPER_QUIVER);
                        entries.add(ModItems.CREEPER_BOW);
                        entries.add(ModItems.CREEPER_RADAR);
                        entries.add(ModItems.CREEPER_EMP);
                        entries.add(ModItems.SPAWNER_SHARD);
                        entries.add(ModItems.CAT_MISSILE);
                        entries.add(ModItems.CREEPER_ITEM);
                    }).build());

    public static void registerModItemsGroup() {
    }
}
