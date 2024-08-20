package com.bilirz.maomod.block;

import com.bilirz.maomod.Maomod;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.block.Block;

public class ModBlocks {
    public static final Block HYPNO_SHROOM = registerBlock("hypno_shroom", new HypnoShroomBlock(Block.Settings.create())); // 魅惑菇
    public static final Block DOOM_SHROOM = registerBlock("doom_shroom", new DoomShroomBlock(Block.Settings.create())); // 毁灭菇

    public static Block registerBlock(String name, Block block) {
        return Registry.register(Registries.BLOCK, new Identifier(Maomod.MOD_ID, name), block);
    }
}
