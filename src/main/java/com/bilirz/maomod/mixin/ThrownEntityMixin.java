package com.bilirz.maomod.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownEntity.class)
public abstract class ThrownEntityMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    public void onTick(CallbackInfo ci) {
        ThrownEntity self = (ThrownEntity) (Object) this;

        // 仅对 SnowballEntity 进行处理
        if (self instanceof SnowballEntity) {
            World world = self.getWorld();
            Vec3d pos = self.getPos();
            BlockPos blockPos = new BlockPos((int) pos.x, (int) pos.y, (int) pos.z);

            // 检查火焰范围的半径
            int radius = 1;
            boolean nearFire = false;

            // 检查雪球周围的方块是否有火焰
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dy = -radius; dy <= radius; dy++) {
                    for (int dz = -radius; dz <= radius; dz++) {
                        BlockPos checkPos = blockPos.add(dx, dy, dz);
                        BlockState blockState = world.getBlockState(checkPos);
                        if (blockState.isOf(Blocks.FIRE)) {
                            nearFire = true;
                            break;
                        }
                    }
                    if (nearFire) break;
                }
                if (nearFire) break;
            }

            // 如果在范围内找到了火焰，将雪球替换为火球
            if (nearFire) {
                // 获取雪球的速度向量
                Vec3d velocity = self.getVelocity();
                double velocityX = velocity.x;
                double velocityY = velocity.y;
                double velocityZ = velocity.z;

                LivingEntity owner = self.getOwner() instanceof LivingEntity ? (LivingEntity) self.getOwner() : null;

                if (owner != null) {
                    FireballEntity fireballEntity = new FireballEntity(
                            world,
                            owner,
                            velocityX, velocityY, velocityZ,
                            1
                    );
                    fireballEntity.setPos(pos.x, pos.y, pos.z);

                    // 将雪球替换为火球
                    world.spawnEntity(fireballEntity);
                    self.discard();
                }
            }
        }
    }
}
