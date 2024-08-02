package com.bilirz.maomod.entity;

import com.bilirz.maomod.entity.custom.CreeperLegsEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class CreeperLegsModel extends EntityModel<CreeperLegsEntity> {
    private final ModelPart legs;

    public CreeperLegsModel(ModelPart root) {
        this.legs = root.getChild("legs");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData legs = modelPartData.addChild("legs", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData leg4 = legs.addChild("leg4", ModelPartBuilder.create().uv(0, 16).cuboid(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(2.0F, -6.0F, -4.0F));

        ModelPartData leg3 = legs.addChild("leg3", ModelPartBuilder.create().uv(0, 16).cuboid(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.0F, -6.0F, -4.0F));

        ModelPartData leg2 = legs.addChild("leg2", ModelPartBuilder.create().uv(0, 16).cuboid(-2.0F, 0.0F, -4.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(2.0F, -6.0F, 4.0F));

        ModelPartData leg1 = legs.addChild("leg1", ModelPartBuilder.create().uv(0, 16).cuboid(-2.0F, 0.0F, -4.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.0F, -6.0F, 4.0F));
        return TexturedModelData.of(modelData, 64, 32);
    }

    @Override
    public void setAngles(CreeperLegsEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        legs.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }
}
