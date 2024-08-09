package com.bilirz.maomod.mixin;

import com.bilirz.maomod.enchantment.ModEnchantments;
import com.bilirz.maomod.item.ModItems;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityInteraction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin {

    // 禁用村民受伤或死亡时的负面流言传播
    @Inject(method = "onInteractionWith", at = @At("HEAD"), cancellable = true)
    private void disableGossip(EntityInteraction interaction, Entity entity, CallbackInfo ci) {
        if (interaction == EntityInteraction.VILLAGER_HURT || interaction == EntityInteraction.VILLAGER_KILLED) {
            ci.cancel();
        }
    }

    // 禁用村民死亡时的负面效果
    @Inject(method = "onDeath", at = @At("HEAD"), cancellable = true)
    private void disableNegativeEffectsOnDeath(DamageSource source, CallbackInfo ci) {
        if (source.getAttacker() instanceof LivingEntity) {
            ci.cancel();
        }
    }

    @Unique
    private final int[] persistentDiscounts = new int[10]; // 假设最多有10个交易，用于保存交易折扣信息

    // 当村民被攻击时调用，应用威胁附魔的效果
    @Inject(method = "setAttacker", at = @At("HEAD"))
    private void onAttacked(LivingEntity attacker, CallbackInfo ci) {
        if (attacker instanceof PlayerEntity player) {
            ItemStack heldItem = player.getMainHandStack();

            // 检查玩家使用的武器是否带有威胁附魔
            if (EnchantmentHelper.getLevel(ModEnchantments.THREAT, heldItem) > 0) {
                VillagerEntity villager = (VillagerEntity) (Object) this;
                applyThreatDiscount(villager); // 应用交易折扣
            }
        }
    }

    // 威胁附魔的交易折扣
    @Unique
    private void applyThreatDiscount(VillagerEntity villager) {
        TradeOfferList offers = villager.getOffers();
        for (int i = 0; i < offers.size(); i++) {
            TradeOffer offer = offers.get(i);
            int currentPrice = offer.getAdjustedFirstBuyItem().getCount();
            int discountedPrice = Math.max(1, MathHelper.floor(currentPrice * 0.5)); // 价格减半
            offer.increaseSpecialPrice(-discountedPrice); // 使用原版方法降低价格

            // 保存折扣信息，以便在后续恢复
            persistentDiscounts[i] += discountedPrice;
        }
    }

    // 重置客户后恢复交易的持久折扣
    @Inject(method = "resetCustomer", at = @At("RETURN"))
    private void onResetCustomer(CallbackInfo ci) {
        restorePersistentDiscounts();
    }

    // 补货后恢复交易的持久折扣
    @Inject(method = "restock", at = @At("RETURN"))
    private void onRestock(CallbackInfo ci) {
        restorePersistentDiscounts();
    }

    // 恢复交易的持久折扣
    @Unique
    private void restorePersistentDiscounts() {
        VillagerEntity villager = (VillagerEntity) (Object) this;
        TradeOfferList offers = villager.getOffers();
        for (int i = 0; i < offers.size(); i++) {
            TradeOffer offer = offers.get(i);
            offer.increaseSpecialPrice(-persistentDiscounts[i]); // 恢复折扣
        }
    }

    // 应用交易反转和乞讨附魔
    @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
    private void onInteractMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack heldItem = player.getStackInHand(hand);

        // 交易反转
        if (EnchantmentHelper.getLevel(ModEnchantments.TRADE_REVERSAL, heldItem) > 0) {
            VillagerEntity villager = (VillagerEntity) (Object) this;
            reverseTrades(villager);
            cir.setReturnValue(ActionResult.success(player.getWorld().isClient)); // 终止原方法，成功返回
        }

        // 乞讨
        if (EnchantmentHelper.getLevel(ModEnchantments.BEG, heldItem) > 0) {
            VillagerEntity villager = (VillagerEntity) (Object) this;

            if (heldItem.isOf(ModItems.DURABLE_BOWL)) {
                giveEmerald(villager, player, heldItem, 1); // 碗给1个绿宝石
            } else if (heldItem.isOf(ModItems.DURABLE_BUCKET)) {
                giveEmerald(villager, player, heldItem, 3); // 桶给3个绿宝石
            }

            cir.setReturnValue(ActionResult.success(player.getWorld().isClient)); // 终止原方法，成功返回
        }
    }

    // 反转村民的交易物品
    @Unique
    private void reverseTrades(VillagerEntity villager) {
        TradeOfferList offers = villager.getOffers();
        for (TradeOffer offer : offers) {
            // 交换输入和输出
            int maxUses = offer.getMaxUses();
            int currentUses = offer.getUses();
            int specialPrice = offer.getSpecialPrice();

            // 创建反转后的交易
            TradeOffer reversedOffer = new TradeOffer(
                    offer.getSellItem().copy(), // 输出变成输入
                    offer.getSecondBuyItem().copy(), // 第二个输入保持不变
                    offer.getOriginalFirstBuyItem().copy(), // 输入变成输出
                    currentUses,
                    maxUses,
                    specialPrice,
                    (float) offer.getMerchantExperience(),
                    (int) offer.getPriceMultiplier()
            );

            // 将反转后的交易替换原交易
            offers.set(offers.indexOf(offer), reversedOffer);
        }

        // 播放音效
        villager.getWorld().playSound(
                null, // 如果是null，声音只在客户端播放
                villager.getBlockPos(),
                SoundEvents.ENTITY_VILLAGER_YES, // 使用村民同意的音效
                villager.getSoundCategory(),
                1.0F, // 音量
                1.0F  // 音调
        );

        // 生成粒子效果
        villager.getWorld().addParticle(
                ParticleTypes.HAPPY_VILLAGER, // 使用村民的快乐粒子效果
                villager.getX(),
                villager.getY() + 2,
                villager.getZ(),
                0.0,
                0.5,
                0.0
        );
    }

    // 村民给予玩家指定数量的绿宝石
    @Unique
    private void giveEmerald(VillagerEntity villager, PlayerEntity player, ItemStack item, int emeraldCount) {
        // 村民扔出指定数量的绿宝石
        ItemStack emerald = new ItemStack(Items.EMERALD, emeraldCount);
        villager.dropStack(emerald);

        // 播放音效和粒子效果
        villager.getWorld().playSound(
                null,
                villager.getBlockPos(),
                SoundEvents.ENTITY_VILLAGER_YES,
                villager.getSoundCategory(),
                1.0F,
                1.0F
        );

        villager.getWorld().addParticle(
                ParticleTypes.HAPPY_VILLAGER,
                villager.getX(),
                villager.getY() + 2,
                villager.getZ(),
                0.0,
                0.5,
                0.0
        );

        // 物品掉耐久
        item.damage(1, player, p -> p.sendToolBreakStatus(player.getActiveHand()));
    }
}
