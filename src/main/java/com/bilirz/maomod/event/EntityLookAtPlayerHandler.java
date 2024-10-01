package com.bilirz.maomod.event;

import com.bilirz.maomod.effect.ModEffects;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class EntityLookAtPlayerHandler {

    // 记录每个生物看向玩家的时间
    private static final Map<LivingEntity, Integer> entityLookTime = new HashMap<>();

    public static void register() {
        // 每 tick 检查生物是否看向拥有再多看一眼就会爆炸效果的玩家
        ServerTickEvents.END_WORLD_TICK.register(world -> {
            if (world instanceof ServerWorld) {
                checkEntitiesLookingAtPlayer(world);
            }
        });
    }

    private static void checkEntitiesLookingAtPlayer(ServerWorld world) {
        for (PlayerEntity player : world.getPlayers()) {
            // 检查玩家是否拥有再多看一眼就会爆炸效果
            if (player.hasStatusEffect(ModEffects.EXPLOSIVE_GLANCE)) {
                Vec3d playerPos = player.getCameraPosVec(1.0F);

                // 定义检测范围 (边界框)
                Box box = new Box(playerPos.add(-20, -20, -20), playerPos.add(20, 20, 20));

                // 定义筛选条件，排除玩家自身
                Predicate<LivingEntity> predicate = livingEntity -> livingEntity != player;

                // 获取玩家附近的所有生物
                for (LivingEntity livingEntity : world.getEntitiesByClass(LivingEntity.class, box, predicate)) {
                    // 获取生物的眼睛位置和视线方向
                    Vec3d entityPos = livingEntity.getCameraPosVec(1.0F);
                    Vec3d entityLookVec = livingEntity.getRotationVec(1.0F).normalize();

                    // 计算从生物到玩家的向量
                    Vec3d vectorToPlayer = playerPos.subtract(entityPos).normalize();

                    // 使用点积判断生物是否在看向玩家
                    double dotProduct = entityLookVec.dotProduct(vectorToPlayer);

                    // 如果点积接近1，说明生物正在看向玩家
                    if (dotProduct > 0.95) { // 阈值可以调整
                        // 生物持续看向玩家的时间累积
                        int lookTime = entityLookTime.getOrDefault(livingEntity, 0) + 1;
                        entityLookTime.put(livingEntity, lookTime);

                        // 如果生物持续看向玩家超过 1 秒 (20 ticks)，触发爆炸
                        if (lookTime >= 20) {
                            triggerExplosion(livingEntity);
                            entityLookTime.remove(livingEntity); // 移除计时器
                        }
                    } else {
                        // 如果生物没有继续看向玩家，重置计时
                        entityLookTime.remove(livingEntity);
                    }
                }
            }
        }
    }

    // 触发生物爆炸
    private static void triggerExplosion(LivingEntity entity) {
        if (!entity.getWorld().isClient) {
            // 生成爆炸
            entity.getWorld().createExplosion(entity, entity.getX(), entity.getY(), entity.getZ(), 2.0F, false, World.ExplosionSourceType.BLOCK);

            if (entity.getWorld() instanceof ServerWorld serverWorld) {
                // 生成的鸡数量
                int chickenCount = 3;

                // 在爆炸中心附近生成鸡
                for (int i = 0; i < chickenCount; i++) {
                    // 更小的随机偏移，使鸡更集中于爆炸中心
                    double offsetX = (entity.getWorld().random.nextDouble() - 0.5) * 0.5; // X 轴偏移
                    double offsetZ = (entity.getWorld().random.nextDouble() - 0.5) * 0.5; // Z 轴偏移

                    // 计算生成鸡的最终位置
                    BlockPos spawnPos = new BlockPos((int) (entity.getX() + offsetX), (int) entity.getY(), (int) (entity.getZ() + offsetZ));
                    int groundY = serverWorld.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, spawnPos.getX(), spawnPos.getZ());
                    BlockPos groundPos = new BlockPos(spawnPos.getX(), groundY, spawnPos.getZ());

                    // 生成鸡
                    ChickenEntity chicken = EntityType.CHICKEN.create(serverWorld);
                    if (chicken != null) {
                        chicken.refreshPositionAndAngles(groundPos.getX(), groundPos.getY(), groundPos.getZ(), 0.0F, 0.0F);
                        serverWorld.spawnEntity(chicken);
                    }
                }
            }

            // 移除生物
            entity.discard();
        }
    }

}
