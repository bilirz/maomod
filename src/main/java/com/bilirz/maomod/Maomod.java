package com.bilirz.maomod;

import com.bilirz.maomod.enchantment.ModEnchantments;
import com.bilirz.maomod.entity.ModEntities;
import com.bilirz.maomod.event.ModBlockBreakHandler;
import com.bilirz.maomod.event.PlayerVoidTeleportHandler;
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
		ModItems.registerModItems();
		ModItemGroup.registerModItemsGroup();
		ModEnchantments.registerModEnchantments();
		PlayerVoidTeleportHandler.register();
		ModBlockBreakHandler.register();
		ModEntities.registerEntities();
		ModEntities.registerAttributes();

		LOGGER.info("苦力怕弓已安装！");
	}
}
