package com.bilirz.maomod.client.render;

import com.bilirz.maomod.Maomod;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.ZombieEntityModel;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.util.Identifier;
import net.minecraft.entity.mob.ZombieEntity;

public class CustomZombieRenderLayer extends FeatureRenderer<ZombieEntity, ZombieEntityModel<ZombieEntity>> {
    public static final TrackedData<Boolean> IS_HYPNO = DataTracker.registerData(ZombieEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final Identifier TEXTURE = new Identifier(Maomod.MOD_ID, "textures/entity/mob_color.png");

    public CustomZombieRenderLayer(FeatureRendererContext<ZombieEntity, ZombieEntityModel<ZombieEntity>> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ZombieEntity entity, float limbAngle, float limbDistance, float tickDelta, float customAngle, float headYaw, float headPitch) {
        if (entity.getDataTracker().get(IS_HYPNO)) {
            // 渲染模型并应用魅惑纹理
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE));
            this.getContextModel().render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1F, 1F, 1F, 0.4F);
        }
    }
}
