package com.bilirz.maomod.client.render;

import com.bilirz.maomod.Maomod;
import com.bilirz.maomod.client.ModModelLayers;
import com.bilirz.maomod.entity.CreeperHeadModel;
import com.bilirz.maomod.entity.custom.CreeperHeadEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class CreeperHeadRenderer extends MobEntityRenderer<CreeperHeadEntity, CreeperHeadModel> {
    private static final Identifier TEXTURE = new Identifier(Maomod.MOD_ID, "textures/entity/creeper.png");

    public CreeperHeadRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new CreeperHeadModel(ctx.getPart(ModModelLayers.CREEPER_HEAD)), 0.5f);
    }

    @Override
    public Identifier getTexture(CreeperHeadEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(CreeperHeadEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
        matrices.pop();
    }
}
