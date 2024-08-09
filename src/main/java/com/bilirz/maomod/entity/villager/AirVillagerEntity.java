package com.bilirz.maomod.entity.villager;

import com.bilirz.maomod.item.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.World;

public class AirVillagerEntity extends VillagerEntity {

    // 将其职业设置为傻子
    public AirVillagerEntity(EntityType<? extends VillagerEntity> entityType, World world) {
        super(entityType, world);
        this.setVillagerData(this.getVillagerData().withProfession(VillagerProfession.NITWIT));
    }

    // 定义属性
    public static DefaultAttributeContainer.Builder createAirVillagerAttributes() {
        return VillagerEntity.createVillagerAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3D) // 移动速度
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 100.0D); // 最大生命值
    }

    @Override
    public EntityType<?> getType() {
        return EntityType.VILLAGER;
    }

    // 添加自定义交易
    @Override
    protected void fillRecipes() {
        TradeOfferList tradeOfferList = this.getOffers();

        // 添加自定义交易：64个绿宝石换1个空气物品
        tradeOfferList.add(new TradeOffer(
                new ItemStack(Items.EMERALD, 64),
                new ItemStack(ModItems.AIR_ITEM, 1),
                16, // 最大使用次数
                2, // 交易后的经验值
                0.05f // 价格乘数
        ));

        super.fillRecipes();
        this.setVillagerData(this.getVillagerData().withProfession(VillagerProfession.NITWIT)); // 设置村民职业为傻子
    }

    // 显示名称
    @Override
    public Text getDisplayName() {
        return Text.translatable("entity.maomod.air_villager");
    }
}
