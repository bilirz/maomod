package com.bilirz.maomod.event;

import com.bilirz.maomod.block.DaySleepingBlock;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

public class ModBlockUseHandler {

    public static void register() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            BlockPos pos = hitResult.getBlockPos();
            Block block = world.getBlockState(pos).getBlock();

            // 检查玩家是否右键点击了 DaySleepingBlock 的实例且手持可可豆
            if (block instanceof DaySleepingBlock && player.getStackInHand(hand).getItem() == Items.COCOA_BEANS) {
                ((DaySleepingBlock) block).activateWithCocoaBeans(world, pos);

                if (!player.isCreative()) {
                    ItemStack itemStack = player.getStackInHand(hand);
                    itemStack.decrement(1);
                }

                return ActionResult.SUCCESS;
            }
            return ActionResult.PASS;
        });
    }
}
