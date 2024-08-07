package com.bilirz.maomod;

import com.bilirz.maomod.effect.ModEffects;
import com.bilirz.maomod.enchantment.ModEnchantments;
import com.bilirz.maomod.entity.ModEntities;
import com.bilirz.maomod.event.ModEvents;
import com.bilirz.maomod.item.ModItemGroup;
import com.bilirz.maomod.item.ModItems;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Maomod implements ModInitializer {
	public static final String MOD_ID = "maomod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
        ModItems.registerModItems(); // 注册自定义物品
        ModItemGroup.registerModItemsGroup(); // 注册自定义物品组
        ModEnchantments.registerModEnchantments(); // 注册自定义附魔
        ModEntities.registerEntities(); // 注册自定义实体
        ModEntities.registerAttributes(); // 注册实体属性
        ModEffects.registerEffects(); // 注册自定义状态效果
        ModEvents.registerModEvents(); // 注册所有事件处理器

        LOGGER.info("Maomod已安装！");
	}
}
