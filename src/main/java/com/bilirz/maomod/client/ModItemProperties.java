package com.bilirz.maomod.client;

import com.bilirz.maomod.item.ModItems;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;

public class ModItemProperties {

    // 注册所有自定义物品的属性
    public static void registerItemProperties() {
        registerBowProperties(); // 注册弓的属性
    }

    // 注册弓的自定义模型属性
    private static void registerBowProperties() {
        final Identifier pull = new Identifier("pull"); // 定义拉弓进度的标识符
        final Identifier pulling = new Identifier("pulling"); // 定义拉弓动作的标识符

        // 注册拉弓进度属性
        ModelPredicateProviderRegistry.register(ModItems.CREEPER_BOW, pull,
                (stack, world, entity, seed) -> {
                    if (entity == null || entity.getActiveItem() != stack) {
                        return 0.0F; // 如果实体为空或当前物品不是弓，返回 0
                    }
                    return (stack.getMaxUseTime() - entity.getItemUseTimeLeft()) / 20.0F; // 计算并返回拉弓进度
                }
        );

        // 注册拉弓动作属性
        ModelPredicateProviderRegistry.register(ModItems.CREEPER_BOW, pulling,
                (stack, world, entity, seed) -> (entity != null && entity.isUsingItem() && entity.getActiveItem() == stack) ? 1.0F : 0.0F
        );
    }
}
