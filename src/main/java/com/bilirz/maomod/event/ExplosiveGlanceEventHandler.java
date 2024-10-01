package com.bilirz.maomod.event;

import com.bilirz.maomod.enchantment.ModEnchantments;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;

import java.util.*;

public class ExplosiveGlanceEventHandler {

    // 记录每个生物看向玩家的时间
    private static final Map<PlayerEntity, Long> playerLookingAtItemTime = new HashMap<>();
    // 用于记录每个玩家当前正在看向的物品
    private static final Map<PlayerEntity, ItemEntity> playerCurrentLookingItem = new HashMap<>();
    private static final List<ItemEntity> explosiveItems = new ArrayList<>();
    private static final int REQUIRED_TICKS = 10;

    public static void register() {
        // 监听物品实体生成事件
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (entity instanceof ItemEntity itemEntity) {
                ItemStack stack = itemEntity.getStack();

                // 检查物品是否有再多看一眼就会爆炸附魔
                if (EnchantmentHelper.getLevel(ModEnchantments.EXPLOSIVE_GLANCE, stack) > 0) {
                    explosiveItems.add(itemEntity); // 将附魔物品加入列表
                }
            }
        });

        // 每 tick 检查玩家是否看向带有附魔的物品
        ServerTickEvents.END_WORLD_TICK.register(world -> {
            if (world instanceof ServerWorld) {
                checkPlayerLookingAtItem(world);
            }
        });
    }

    // 检查玩家是否看向带有再多看一眼就会爆炸的物品实体
    private static void checkPlayerLookingAtItem(ServerWorld world) {
        for (PlayerEntity player : world.getPlayers()) {
            Vec3d playerPos = player.getCameraPosVec(1.0F); // 玩家眼睛位置
            Vec3d playerLookVec = player.getRotationVec(1.0F).normalize(); // 玩家视线方向

            double maxDistance = 100.0D;
            Vec3d playerViewEnd = playerPos.add(playerLookVec.multiply(maxDistance)); // 计算视线终点
            Box viewBox = player.getBoundingBox().stretch(playerLookVec.multiply(maxDistance)).expand(1.0D);

            List<Entity> entitiesInRange = world.getOtherEntities(player, viewBox);

            // 默认不在看任何物品
            ItemEntity lookingAtItem = null;

            for (Entity entity : entitiesInRange) {
                if (entity instanceof ItemEntity itemEntity) {
                    // 判断玩家视线是否与物品实体相交
                    Optional<Vec3d> hitResult = itemEntity.getBoundingBox().raycast(playerPos, playerViewEnd);

                    if (hitResult.isPresent() && explosiveItems.contains(itemEntity)) {
                        lookingAtItem = itemEntity; // 记录正在看向的物品
                        break;
                    }
                }
            }

            // 如果正在看向某个物品
            if (lookingAtItem != null) {
                // 检查玩家是否在持续看同一个物品
                if (playerCurrentLookingItem.get(player) == lookingAtItem) {
                    // 增加持续时间
                    long lookingTime = playerLookingAtItemTime.getOrDefault(player, 0L) + 1;
                    playerLookingAtItemTime.put(player, lookingTime);

                    // 如果玩家看向物品超过 3 秒，触发爆炸
                    if (lookingTime >= REQUIRED_TICKS) {
                        triggerExplosion(lookingAtItem);
                        playerLookingAtItemTime.remove(player); // 重置计时
                        playerCurrentLookingItem.remove(player); // 移除当前物品
                    }
                } else {
                    // 切换了物品，重置计时
                    playerCurrentLookingItem.put(player, lookingAtItem);
                    playerLookingAtItemTime.put(player, 0L);
                }
            } else {
                // 玩家没有在看任何附魔物品，重置计时
                if (playerCurrentLookingItem.containsKey(player)) {
                    playerLookingAtItemTime.remove(player);
                    playerCurrentLookingItem.remove(player);
                }
            }
        }
    }

    // 触发爆炸，爆炸威力根据物品数量决定，并生成鸡
    private static void triggerExplosion(ItemEntity itemEntity) {
        World world = itemEntity.getWorld();
        ItemStack stack = itemEntity.getStack();

        // 物品堆栈数量决定爆炸威力
        int stackSize = stack.getCount(); // 获取物品堆栈的数量
        float explosionPower = 1.0F + stackSize * 0.5F; // 基础威力为 1，每 1 个物品增加 0.5 爆炸威力

        if (!world.isClient && world instanceof ServerWorld serverWorld) {
            // 生成爆炸
            serverWorld.createExplosion(itemEntity, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), explosionPower, false, World.ExplosionSourceType.BLOCK);

            // 在爆炸中心附近生成鸡
            int chickenCount = (int) explosionPower * 2; // 根据爆炸威力生成的鸡数量，威力x2
            for (int i = 0; i < chickenCount; i++) {
                // 控制随机生成鸡的位置，使其更集中于爆炸中心附近
                double offsetX = (world.random.nextDouble() - 0.5) * explosionPower * 0.4; // X 轴偏移
                double offsetZ = (world.random.nextDouble() - 0.5) * explosionPower * 0.4; // Z 轴偏移

                // 获取生成位置地面高度
                BlockPos spawnPos = new BlockPos((int) (itemEntity.getX() + offsetX), (int) itemEntity.getY(), (int) (itemEntity.getZ() + offsetZ));
                int groundY = serverWorld.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, spawnPos.getX(), spawnPos.getZ());
                BlockPos groundPos = new BlockPos(spawnPos.getX(), groundY, spawnPos.getZ());

                // 生成鸡
                ChickenEntity chicken = EntityType.CHICKEN.create(serverWorld);
                if (chicken != null) {
                    chicken.refreshPositionAndAngles(groundPos.getX(), groundPos.getY(), groundPos.getZ(), 0.0F, 0.0F);
                    serverWorld.spawnEntity(chicken);
                }
            }

            // 移除掉落物
            itemEntity.discard();
            explosiveItems.remove(itemEntity); // 从列表中移除该物品
        }
    }
}
