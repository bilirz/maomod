package com.bilirz.maomod.event;

import com.bilirz.maomod.enchantment.ModEnchantments;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class BeheadingEventHandler {

    public static void register() {
        // 在实体被击杀后触发
        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((world, entity, killedEntity) -> {
            if (!(entity instanceof LivingEntity attacker)) return;

            int level = EnchantmentHelper.getEquipmentLevel(ModEnchantments.BEHEADING, attacker);

            if (level > 0) {
                ItemStack headItem = null;
                int dropMultiplier = level; // 默认按附魔等级增加掉落数量

                if (killedEntity instanceof PlayerEntity) {
                    // 确保正确处理玩家头颅的生成
                    headItem = new ItemStack(Items.PLAYER_HEAD);
                    // 使用UUID而不是名字以确保正确的皮肤显示
                    headItem.getOrCreateSubNbt("SkullOwner").putUuid("Id", killedEntity.getUuid());
                } else if (killedEntity.getType() == EntityType.ZOMBIE) {
                    headItem = new ItemStack(Items.ZOMBIE_HEAD);
                } else if (killedEntity.getType() == EntityType.SKELETON) {
                    headItem = new ItemStack(Items.SKELETON_SKULL);
                } else if (killedEntity.getType() == EntityType.CREEPER) {
                    headItem = new ItemStack(Items.CREEPER_HEAD);
                } else if (killedEntity.getType() == EntityType.WITHER_SKELETON) {
                    headItem = new ItemStack(Items.WITHER_SKELETON_SKULL);
                } else if (killedEntity.getType() == EntityType.WITHER) {
                    headItem = new ItemStack(Items.WITHER_SKELETON_SKULL);
                    dropMultiplier = 3 * level; // 凋灵掉落3倍的凋灵骷髅头
                }

                // 根据附魔等级和掉落倍率掉落对应数量的头颅
                if (headItem != null) {
                    for (int i = 0; i < dropMultiplier; i++) {
                        killedEntity.dropStack(headItem.copy());
                    }
                }
            }
        });
    }
}
