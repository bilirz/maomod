package com.bilirz.maomod.block;

import com.bilirz.maomod.client.render.CustomSkeletonRenderLayer;
import com.bilirz.maomod.client.render.CustomZombieRenderLayer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HypnoShroomBlock extends DaySleepingBlock {

    // 构造函数，初始化方块设置
    public HypnoShroomBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if (state.get(ACTIVATED)) {
            if (entity instanceof MobEntity mobEntity) {
                if (mobEntity instanceof ZombieEntity) {
                    // 使僵尸进入催眠状态
                    mobEntity.getDataTracker().set(CustomZombieRenderLayer.IS_HYPNO, true);
                } else if (mobEntity instanceof SkeletonEntity) {
                    // 使骷髅进入催眠状态
                    mobEntity.getDataTracker().set(CustomSkeletonRenderLayer.IS_HYPNO, true);
                }
                world.breakBlock(pos, true);
            }
        }
    }
}
