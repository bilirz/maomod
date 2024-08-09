package com.bilirz.maomod.client.render;

import com.bilirz.maomod.Maomod;
import com.bilirz.maomod.client.ModModelLayers;
import com.bilirz.maomod.entity.CreeperBodyModel;
import com.bilirz.maomod.entity.creeper.CreeperBodyEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class CreeperBodyRenderer extends MobEntityRenderer<CreeperBodyEntity, CreeperBodyModel> {
    private static final Identifier TEXTURE = new Identifier(Maomod.MOD_ID, "textures/entity/creeper.png");

    public CreeperBodyRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new CreeperBodyModel(ctx.getPart(ModModelLayers.CREEPER_BODY)), 0.5f);
    }

    @Override
    public Identifier getTexture(CreeperBodyEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(CreeperBodyEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
        matrices.pop();
    }
}
