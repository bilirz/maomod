package com.bilirz.maomod.client.render;

import com.bilirz.maomod.Maomod;
import com.bilirz.maomod.client.ModModelLayers;
import com.bilirz.maomod.entity.creeper.model.CreeperLegsModel;
import com.bilirz.maomod.entity.creeper.CreeperLegsEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class CreeperLegsRenderer extends MobEntityRenderer<CreeperLegsEntity, CreeperLegsModel> {
    private static final Identifier TEXTURE = new Identifier(Maomod.MOD_ID, "textures/entity/creeper.png");

    public CreeperLegsRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new CreeperLegsModel(ctx.getPart(ModModelLayers.CREEPER_LEGS)), 0.5f);
    }

    @Override
    public Identifier getTexture(CreeperLegsEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(CreeperLegsEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
        matrices.pop();
    }
}
