package com.bilirz.maomod.client.render;

import com.bilirz.maomod.Maomod;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.SkeletonEntityModel;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.util.Identifier;

public class CustomSkeletonRenderLayer extends FeatureRenderer<AbstractSkeletonEntity, SkeletonEntityModel<AbstractSkeletonEntity>> {
    public static final TrackedData<Boolean> IS_HYPNO = DataTracker.registerData(SkeletonEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final Identifier TEXTURE = new Identifier(Maomod.MOD_ID, "textures/entity/mob_color.png");

    public CustomSkeletonRenderLayer(FeatureRendererContext<AbstractSkeletonEntity, SkeletonEntityModel<AbstractSkeletonEntity>> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractSkeletonEntity entity, float limbAngle, float limbDistance, float tickDelta, float customAngle, float headYaw, float headPitch) {
        if (entity.getDataTracker().get(IS_HYPNO)) {
            // 渲染模型并应用魅惑纹理
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE));
            this.getContextModel().render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1F, 1F, 1F, 0.3F);
        }
    }
}
