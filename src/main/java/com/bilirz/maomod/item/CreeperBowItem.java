package com.bilirz.maomod.item;

import com.bilirz.maomod.enchantment.ModEnchantments;
import com.bilirz.maomod.entity.CreeperWithEnchantments;
import com.bilirz.maomod.entity.ModEntities;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class CreeperBowItem extends BowItem {
    private static final List<CreeperWithEnchantments> creeperList = new ArrayList<>(); // 存储被发射的苦力怕
    private static final double GRAVITY = 0.05; // 定义重力加速度
    private CreeperRadar creeperRadar = null; // 定义雷达对象

    // 构造函数，注册服务器的Tick事件处理
    public CreeperBowItem(Settings settings) {
        super(settings);

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            this.creeperRadar = new CreeperRadar(server); // 初始化 苦力怕雷达

            Iterator<CreeperWithEnchantments> iterator = creeperList.iterator();
            while (iterator.hasNext()) {
                CreeperWithEnchantments creeperWithEnchantments = iterator.next();
                CreeperEntity creeper = creeperWithEnchantments.creeper;
                World world = creeper.getWorld();

                // 处理苦力怕在地面上的行为
                if (creeper.isOnGround()) {
                    if (creeperWithEnchantments.hasChanneling && creeperWithEnchantments.lightningCount < 3) {
                        LightningEntity lightning = EntityType.LIGHTNING_BOLT.create(world);
                        if (lightning != null) {
                            lightning.refreshPositionAfterTeleport(creeper.getX(), creeper.getY(), creeper.getZ());
                            world.spawnEntity(lightning);
                            creeperWithEnchantments.lightningCount++;
                        }
                    }
                    if (!creeperWithEnchantments.isMultishot) {
                        creeper.ignite(); // 点燃苦力怕
                    }
                    if (creeperWithEnchantments.lightningCount >= 3) {
                        iterator.remove();
                    }
                } else if (creeperWithEnchantments.hasTracking) {
                    PlayerEntity target = findNearestPlayer(world, creeper, creeperWithEnchantments.owner); // 查找最近的玩家
                    if (target != null) {
                        Vec3d direction = predictTargetPosition(target).subtract(creeper.getPos()).normalize();
                        double speedMultiplier = creeperWithEnchantments.baseSpeedMultiplier;
                        creeper.setVelocity(direction.x * speedMultiplier, direction.y * speedMultiplier, direction.z * speedMultiplier);  // 设置苦力怕的跟踪速度
                    }
                }

                Vec3d velocity = creeper.getVelocity();
                creeper.setVelocity(velocity.x, velocity.y - GRAVITY, velocity.z); // 施加重力
            }
        });
    }

    // 检查苦力怕是否被指定的射手射击
    public static boolean isCreeperShotBy(CreeperEntity creeper, LivingEntity shooter) {
        for (CreeperWithEnchantments cwe : creeperList) {
            if (cwe.getCreeper() == creeper && cwe.getShooter() == shooter) {
                return true;
            }
        }
        return false;
    }

    // 查找距离苦力怕最近的玩家
    private PlayerEntity findNearestPlayer(World world, CreeperEntity creeper, LivingEntity owner) {
        Box searchBox = new Box(creeper.getBlockPos()).expand(20, 20, 20);
        List<PlayerEntity> players = world.getEntitiesByClass(PlayerEntity.class, searchBox, player -> player != owner);
        PlayerEntity nearestPlayer = null;
        double nearestDistance = Double.MAX_VALUE;

        for (PlayerEntity player : players) {
            double distance = player.squaredDistanceTo(creeper);
            if (distance < nearestDistance) {
                nearestPlayer = player;
                nearestDistance = distance;
            }
        }
        return nearestPlayer;
    }

    // 预测目标位置，增加随机偏差
    private Vec3d predictTargetPosition(LivingEntity target) {
        Vec3d targetVelocity = target.getVelocity();
        Vec3d predictedPosition = target.getPos().add(targetVelocity.multiply(5));

        // 增加随机偏差 -1 到 1 之间的随机偏差
        double randomOffsetX = (Math.random() - 0.5) * 2;
        double randomOffsetY = (Math.random() - 0.5) * 2;
        double randomOffsetZ = (Math.random() - 0.5) * 2;

        predictedPosition = predictedPosition.add(randomOffsetX, randomOffsetY, randomOffsetZ);
        return predictedPosition;
    }

    // 处理弓的使用行为
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        boolean hasCreeper = false;

        // 遍历玩家的所有箭袋，寻找任何一种箭袋中的苦力怕
        for (ItemStack stack : user.getInventory().main) {
            if (stack.getItem() instanceof CreeperQuiverItem) {
                NbtCompound nbt = stack.getOrCreateNbt();
                int creeperCount = nbt.getInt("CreeperCount");
                if (creeperCount > 0) {
                    hasCreeper = true;
                    break;
                }
            }
        }

        if (hasCreeper) {
            user.setCurrentHand(hand);
            notifyRadarUsers(user, "使用了苦力怕弓");
            return TypedActionResult.consume(itemStack);
        } else {
            notifyRadarUsers(user, "试图使用苦力怕弓，但发现箭袋里已经没有苦力怕了");
            return TypedActionResult.fail(itemStack);
        }
    }

    // 通知雷达用户
    private void notifyRadarUsers(PlayerEntity user, String action) {
        creeperRadar.notifyRadarUsers(user, action);
    }

    // 停止使用弓时调用
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity) {
            if (!world.isClient) {
                int useTicks = this.getMaxUseTime(stack) - remainingUseTicks;
                float pullProgress = getPullProgress(useTicks);
                if (pullProgress < 0.1F) {
                    return;
                }

                int multishotLevel = EnchantmentHelper.getLevel(Enchantments.MULTISHOT, stack);
                boolean isMultishot = multishotLevel > 0;

                // 处理多重射击
                if (isMultishot) {
                    EntityType[] entityTypes = new EntityType[]{
                            ModEntities.CREEPER_HEAD,
                            ModEntities.CREEPER_BODY,
                            ModEntities.CREEPER_LEGS
                    };

                    for (int i = 0; i < 3; i++) {
                        CreeperEntity creeper = (CreeperEntity) entityTypes[i].create(world);
                        if (creeper != null) {
                            creeper.setPosition(playerEntity.getX(), playerEntity.getEyeY() - 0.1, playerEntity.getZ());

                            Vec3d lookVec = playerEntity.getRotationVec(1.0F);
                            double angleOffset = (i - 1) * 10;
                            lookVec = lookVec.rotateY((float) Math.toRadians(angleOffset));

                            int powerLevel = EnchantmentHelper.getLevel(Enchantments.POWER, stack);
                            double baseSpeedMultiplier = (3.0 + (powerLevel * 0.5)) * pullProgress;
                            creeper.setVelocity(lookVec.x * baseSpeedMultiplier, lookVec.y * baseSpeedMultiplier, lookVec.z * baseSpeedMultiplier);

                            boolean hasChanneling = EnchantmentHelper.getLevel(Enchantments.CHANNELING, stack) > 0;
                            boolean hasTracking = EnchantmentHelper.getLevel(ModEnchantments.TRACKING, stack) > 0;

                            Objects.requireNonNull(creeper.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH)).setBaseValue(Double.MAX_VALUE);
                            creeper.setHealth((float) Objects.requireNonNull(creeper.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH)).getValue());

                            world.spawnEntity(creeper);
                            CreeperWithEnchantments creeperWithEnchantments = new CreeperWithEnchantments(creeper, playerEntity, baseSpeedMultiplier, hasChanneling, hasTracking, true);
                            creeperWithEnchantments.shooter = playerEntity;
                            creeperList.add(creeperWithEnchantments);
                        }
                    }
                } else {
                    // 处理单一射击
                    CreeperEntity creeper = new CreeperEntity(EntityType.CREEPER, world);
                    creeper.setPosition(playerEntity.getX(), playerEntity.getEyeY() - 0.1, playerEntity.getZ());

                    Vec3d lookVec = playerEntity.getRotationVec(1.0F);

                    int powerLevel = EnchantmentHelper.getLevel(Enchantments.POWER, stack);
                    double baseSpeedMultiplier = (3.0 + (powerLevel * 0.5)) * pullProgress;
                    creeper.setVelocity(lookVec.x * baseSpeedMultiplier, lookVec.y * baseSpeedMultiplier, lookVec.z * baseSpeedMultiplier);

                    boolean hasChanneling = EnchantmentHelper.getLevel(Enchantments.CHANNELING, stack) > 0;
                    boolean hasTracking = EnchantmentHelper.getLevel(ModEnchantments.TRACKING, stack) > 0;

                    Objects.requireNonNull(creeper.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH)).setBaseValue(Double.MAX_VALUE);
                    creeper.setHealth((float) Objects.requireNonNull(creeper.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH)).getValue());

                    world.spawnEntity(creeper);
                    CreeperWithEnchantments creeperWithEnchantments = new CreeperWithEnchantments(creeper, playerEntity, baseSpeedMultiplier, hasChanneling, hasTracking, false);
                    creeperWithEnchantments.shooter = playerEntity;
                    creeperList.add(creeperWithEnchantments);
                }

                world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F);
                stack.damage(1, playerEntity, (p) -> p.sendToolBreakStatus(playerEntity.getActiveHand()));

                // 减少玩家箭袋中的苦力怕数量
                for (ItemStack quiverStack : playerEntity.getInventory().main) {
                    if (quiverStack.getItem() instanceof CreeperQuiverItem creeperQuiverItem) {
                        if (creeperQuiverItem.getCreeperCount(quiverStack) > 0) {
                            creeperQuiverItem.decreaseCreeperCount(quiverStack);
                            break;
                        }
                    }
                }

                notifyRadarUsers(playerEntity, "发射出了苦力怕");
            }
        }
    }

}
