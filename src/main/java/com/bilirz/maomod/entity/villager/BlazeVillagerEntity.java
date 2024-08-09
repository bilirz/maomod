package com.bilirz.maomod.entity.villager;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.World;

public class BlazeVillagerEntity extends VillagerEntity {

    // 将其职业设置为傻子
    public BlazeVillagerEntity(EntityType<? extends VillagerEntity> entityType, World world) {
        super(entityType, world);
        this.setVillagerData(this.getVillagerData().withProfession(VillagerProfession.NITWIT));
    }

    // 定义属性
    public static DefaultAttributeContainer.Builder createBlazeVillagerAttributes() {
        return VillagerEntity.createVillagerAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3D)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 30.0D);
    }

    // 设置类型为烈焰人
    @Override
    public EntityType<?> getType() {
        return EntityType.BLAZE;
    }

    // 添加自定义交易
    @Override
    protected void fillRecipes() {
        TradeOfferList tradeOfferList = this.getOffers();
        tradeOfferList.add(new TradeOffer(
                new ItemStack(Items.EMERALD, 64),
                new ItemStack(Items.BLAZE_ROD, 1),
                16, // 最大使用次数
                2, // 交易后的经验值
                0.05f // 价格乘数
        ));
        super.fillRecipes();
        this.setVillagerData(this.getVillagerData().withProfession(VillagerProfession.NITWIT));
    }

    // 修改成烈焰人的声音
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_BLAZE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_BLAZE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_BLAZE_DEATH;
    }

    // 显示名称
    @Override
    public Text getDisplayName() {
        // 自定义交易界面中的名称
        return Text.translatable("entity.maomod.blaze_villager");
    }
}