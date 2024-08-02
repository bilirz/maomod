package com.bilirz.maomod.client;

import com.bilirz.maomod.item.ModItems;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;

public class ModItemProperties {

    public static void registerItemProperties() {
        registerBowProperties();
    }

    private static void registerBowProperties() {
        final Identifier pull = new Identifier("pull");
        final Identifier pulling = new Identifier("pulling");

        ModelPredicateProviderRegistry.register(ModItems.CREEPER_BOW, pull,
                (stack, world, entity, seed) -> {
                    if (entity == null || entity.getActiveItem() != stack) {
                        return 0.0F;
                    }
                    return (stack.getMaxUseTime() - entity.getItemUseTimeLeft()) / 20.0F;
                }
        );

        ModelPredicateProviderRegistry.register(ModItems.CREEPER_BOW, pulling,
                (stack, world, entity, seed) -> (entity != null && entity.isUsingItem() && entity.getActiveItem() == stack) ? 1.0F : 0.0F
        );
    }
}
