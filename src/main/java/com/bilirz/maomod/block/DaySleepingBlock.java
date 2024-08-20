package com.bilirz.maomod.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FungusBlock;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.TreeConfiguredFeatures;

public abstract class DaySleepingBlock extends FungusBlock {

    public static final BooleanProperty ACTIVATED = BooleanProperty.of("activated");

    public DaySleepingBlock(Settings settings) {
        super(TreeConfiguredFeatures.CRIMSON_FUNGUS_PLANTED, Blocks.CRIMSON_NYLIUM, settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(ACTIVATED, false));
    }

    // 设置方块的属性
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(ACTIVATED);
    }

    // 如果是夜晚且未激活，则激活方块
    @Override
    @Deprecated
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        super.onBlockAdded(state, world, pos, oldState, notify);

        // 检查当前是否为夜晚
        if (!world.isDay() && !state.get(ACTIVATED)) {
            world.setBlockState(pos, state.with(ACTIVATED, true), Block.NOTIFY_ALL);
        }
    }

    // 使用可可豆激活方块的行为
    public void activateWithCocoaBeans(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (!state.get(ACTIVATED)) {
            world.setBlockState(pos, state.with(ACTIVATED, true));
            if (world instanceof ServerWorld serverWorld) {
                serverWorld.spawnParticles(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 10, 0.5, 0.5, 0.5, 0.1);
            }
        }
    }

    // 随机显示方块未激活时的粒子效果
    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (!state.get(ACTIVATED)) {
            displaySleepParticles(world, pos);
        }
    }

    // 显示休眠粒子效果
    protected void displaySleepParticles(World world, BlockPos pos) {
        if (world.isClient) {
            double offsetX = 0.5 + world.getRandom().nextGaussian() * 0.2;
            double offsetY = 1.0 + world.getRandom().nextGaussian() * 0.2;
            double offsetZ = 0.5 + world.getRandom().nextGaussian() * 0.2;
            world.addParticle(ParticleTypes.SMOKE, pos.getX() + offsetX, pos.getY() + offsetY, pos.getZ() + offsetZ, 0.0, 0.0, 0.0);
        }
    }
}
