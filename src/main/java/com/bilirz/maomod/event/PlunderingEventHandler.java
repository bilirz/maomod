package com.bilirz.maomod.event;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import com.bilirz.maomod.enchantment.ModEnchantments;

public class PlunderingEventHandler {
    public static void register() {
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (world.isClient) {
                return ActionResult.PASS;
            }

            // 检查玩家手中的物品是否有抢劫附魔
            ItemStack weapon = player.getStackInHand(hand);
            int level = EnchantmentHelper.getLevel(ModEnchantments.PLUNDERING, weapon);

            if (level > 0) {
                if (entity instanceof MobEntity mob) {
                    // MobEntity 的处理
                    handleMobDrop(mob);
                } else if (entity instanceof PlayerEntity playerEntity) {
                    // PlayerEntity 的处理
                    handlePlayerDrop(playerEntity);
                }
                if (entity instanceof MerchantEntity merchant) {
                    // MerchantEntity 的处理
                    handleMerchantDrop(merchant);
                }
            }
            return ActionResult.PASS;
        });

    }

    private static void handleMobDrop(MobEntity mob) {
        // 遍历手中的物品并掉落一个物品后跳出循环
        for (Hand mobHand : Hand.values()) {
            ItemStack handItem = mob.getStackInHand(mobHand);
            if (!handItem.isEmpty()) {
                mob.dropStack(handItem.copy());
                mob.setStackInHand(mobHand, ItemStack.EMPTY);
                break; // 仅处理一个物品
            }
        }

        // 遍历盔甲栏中的物品并掉落一个装备后跳出循环
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                ItemStack armorItem = mob.getEquippedStack(slot);
                if (!armorItem.isEmpty()) {
                    mob.dropStack(armorItem.copy());
                    mob.equipStack(slot, ItemStack.EMPTY);
                    break; // 仅处理一个装备
                }
            }
        }
    }

    private static void handlePlayerDrop(PlayerEntity playerEntity) {
        // 处理玩家手中的物品
        for (Hand playerHand : Hand.values()) {
            ItemStack handItem = playerEntity.getStackInHand(playerHand);
            if (!handItem.isEmpty()) {
                playerEntity.dropStack(handItem.copy());
                playerEntity.setStackInHand(playerHand, ItemStack.EMPTY);
                break;
            }
        }

        // 处理玩家盔甲栏中的物品
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                ItemStack armorItem = playerEntity.getEquippedStack(slot);
                if (!armorItem.isEmpty()) {
                    playerEntity.dropStack(armorItem.copy());
                    playerEntity.equipStack(slot, ItemStack.EMPTY);
                    break;
                }
            }
        }
    }

    private static void handleMerchantDrop(MerchantEntity merchant) {
        boolean hasItems = false;

        // 检查是否还有物品
        for (Hand mobHand : Hand.values()) {
            if (!merchant.getStackInHand(mobHand).isEmpty()) {
                hasItems = true;
                break;
            }
        }

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (!merchant.getEquippedStack(slot).isEmpty()) {
                hasItems = true;
                break;
            }
        }

        // 如果没有物品，掉落交易物品并移除交易
        if (!hasItems) {
            TradeOfferList offers = merchant.getOffers();
            for (int i = 0; i < offers.size(); i++) {
                TradeOffer offer = offers.get(i);
                if (offer != null) {
                    ItemStack sellItem = offer.getSellItem();
                    if (!sellItem.isEmpty()) {
                        merchant.dropStack(sellItem.copy()); // 掉落销售物品
                        offers.remove(i); // 移除该交易
                        break; // 仅处理一个交易，然后跳出循环
                    }
                }
            }
        }
    }
}
