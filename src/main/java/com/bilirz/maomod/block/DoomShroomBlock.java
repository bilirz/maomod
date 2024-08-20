package com.bilirz.maomod.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DoomShroomBlock extends DaySleepingBlock {

    public DoomShroomBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if (state.get(ACTIVATED)) {
            if (!world.isClient) {
                world.createExplosion(
                        entity,
                        entity.getX(), entity.getY(), entity.getZ(),
                        30.0f,
                        true, // 引发火灾
                        World.ExplosionSourceType.BLOCK
                );
                world.breakBlock(pos, false);
            }
        }
    }
}
