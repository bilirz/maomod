package com.bilirz.maomod.item;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.*;

public class CatMissileItem extends Item {
    private static final int DETECTION_RADIUS = 10;
    private static final double SPEED_MULTIPLIER = 1.0;
    private static final Map<LivingEntity, CreeperEntity> catCreeperTargets = new WeakHashMap<>();
    private static final Map<PlayerEntity, Long> playerCooldowns = new HashMap<>();
    private static final int COOLDOWN_DURATION = 10 * 1000; // 10秒 CD
    private static boolean catSpawned = false;

    public CatMissileItem(Settings settings) {
        super(settings);
        ServerTickEvents.END_SERVER_TICK.register(server -> onEndTick(server.getOverworld()));
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient && entity instanceof PlayerEntity player && !catSpawned) {

            if ((player.getMainHandStack().getItem() instanceof CatMissileItem || player.getOffHandStack().getItem() instanceof CatMissileItem)
                    && hasTwoFish(player) && !isOnCooldown(player)) {

                ServerWorld serverWorld = (ServerWorld) world;
                BlockPos entityPos = entity.getBlockPos();
                Box detectionBox = new Box(entityPos).expand(DETECTION_RADIUS);
                List<CreeperEntity> creepers = serverWorld.getEntitiesByClass(CreeperEntity.class, detectionBox, LivingEntity::isAlive);

                if (!creepers.isEmpty()) {
                    for (CreeperEntity creeper : creepers) {
                        if (!CreeperBowItem.isCreeperShotBy(creeper, player)) {
                            spawnCatAndTargetCreeper(serverWorld, player, creeper);
                            consumeTwoFish(player);
                            setCooldown(player);
                            player.getItemCooldownManager().set(this, COOLDOWN_DURATION / 50); // 设置冷却时间，以tick为单位（20 ticks = 1秒）
                            catSpawned = true;
                            break;
                        }
                    }
                }
            }
        }
    }

    private void spawnCatAndTargetCreeper(ServerWorld world, LivingEntity player, CreeperEntity creeper) {
        Random random = new Random();
        EntityType<?>[] catTypes = new EntityType<?>[]{EntityType.CAT, EntityType.OCELOT};
        EntityType<?> chosenCatType = catTypes[random.nextInt(catTypes.length)];
        LivingEntity cat = (LivingEntity) chosenCatType.create(world);

        if (cat != null) {
            BlockPos playerPos = player.getBlockPos();
            cat.refreshPositionAndAngles(playerPos.getX(), playerPos.getY(), playerPos.getZ(), player.getYaw(), player.getPitch());
            world.spawnEntity(cat);

            catCreeperTargets.put(cat, creeper);
        }
    }

    private void onEndTick(ServerWorld world) {
        for (Map.Entry<LivingEntity, CreeperEntity> entry : catCreeperTargets.entrySet()) {
            LivingEntity cat = entry.getKey();
            CreeperEntity creeper = entry.getValue();

            if (creeper.isAlive() && cat.isAlive()) {
                Vec3d direction = new Vec3d(creeper.getX() - cat.getX(), creeper.getY() - cat.getY(), creeper.getZ() - cat.getZ()).normalize();
                cat.setVelocity(direction.x * SPEED_MULTIPLIER, direction.y * SPEED_MULTIPLIER, direction.z * SPEED_MULTIPLIER);
                cat.velocityModified = true;

                if (cat.squaredDistanceTo(creeper) <= 1.0) {
                    world.playSound(null, creeper.getBlockPos(), SoundEvents.ENTITY_CAT_DEATH, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                    world.playSound(null, creeper.getBlockPos(), SoundEvents.ENTITY_CREEPER_DEATH, SoundCategory.HOSTILE, 1.0F, 1.0F);

                    creeper.kill();
                    cat.kill();
                    catSpawned = false;
                }
            } else if (!cat.isAlive()) {
                catSpawned = false;
            }
        }

        catCreeperTargets.entrySet().removeIf(entry -> !entry.getKey().isAlive() || !entry.getValue().isAlive());
    }

    private boolean hasTwoFish(PlayerEntity player) {
        int fishCount = 0;
        for (ItemStack itemStack : player.getInventory().main) {
            if (itemStack.getItem() == Items.COD || itemStack.getItem() == Items.SALMON) {
                fishCount += itemStack.getCount();
                if (fishCount >= 2) {
                    return true;
                }
            }
        }
        return false;
    }

    private void consumeTwoFish(PlayerEntity player) {
        int fishCount = 2;
        for (ItemStack itemStack : player.getInventory().main) {
            if (itemStack.getItem() == Items.COD || itemStack.getItem() == Items.SALMON) {
                int count = itemStack.getCount();
                if (count > fishCount) {
                    itemStack.decrement(fishCount);
                    return;
                } else {
                    fishCount -= count;
                    itemStack.decrement(count);
                }
            }
        }
    }

    private boolean isOnCooldown(PlayerEntity player) {
        Long lastUseTime = playerCooldowns.get(player);
        if (lastUseTime == null) {
            return false;
        }
        long currentTime = System.currentTimeMillis();
        return currentTime - lastUseTime < COOLDOWN_DURATION;
    }

    private void setCooldown(PlayerEntity player) {
        playerCooldowns.put(player, System.currentTimeMillis());
    }
}
