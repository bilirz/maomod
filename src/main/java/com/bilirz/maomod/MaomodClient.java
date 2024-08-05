package com.bilirz.maomod;

import com.bilirz.maomod.client.ModItemProperties;
import com.bilirz.maomod.client.ModModelLayers;
import com.bilirz.maomod.client.keybindings.ModKeyBindings;
import com.bilirz.maomod.client.render.CreeperBodyRenderer;
import com.bilirz.maomod.client.render.CreeperHeadRenderer;
import com.bilirz.maomod.client.render.CreeperLegsRenderer;
import com.bilirz.maomod.client.render.QuiverHudRenderer;
import com.bilirz.maomod.entity.CreeperBodyModel;
import com.bilirz.maomod.entity.CreeperHeadModel;
import com.bilirz.maomod.entity.CreeperLegsModel;
import com.bilirz.maomod.entity.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class MaomodClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register(new QuiverHudRenderer()); // 注册HUD渲染回调
        ModItemProperties.registerItemProperties(); // 注册自定义物品属性

        // 注册实体渲染器
        EntityRendererRegistry.register(ModEntities.CREEPER_HEAD, CreeperHeadRenderer::new);
        EntityRendererRegistry.register(ModEntities.CREEPER_BODY, CreeperBodyRenderer::new);
        EntityRendererRegistry.register(ModEntities.CREEPER_LEGS, CreeperLegsRenderer::new);

        // 注册实体模型图层
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.CREEPER_HEAD, CreeperHeadModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.CREEPER_BODY, CreeperBodyModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.CREEPER_LEGS, CreeperLegsModel::getTexturedModelData);

        ModKeyBindings.registerKeyBindings(); // 注册自定义键位绑定
    }

}