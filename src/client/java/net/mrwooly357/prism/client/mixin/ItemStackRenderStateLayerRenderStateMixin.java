package net.mrwooly357.prism.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.world.item.ItemDisplayContext;
import net.mrwooly357.prism.client.render.item.PrismItemStackLayerRenderState;
import net.mrwooly357.prism.client.render.state.PrismRenderStateDataKeys;
import net.mrwooly357.prism.client.render.light.LightCoordinates;
import net.mrwooly357.prism.client.render.light.LightLevel;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(ItemStackRenderState.LayerRenderState.class)
public abstract class ItemStackRenderStateLayerRenderStateMixin implements PrismItemStackLayerRenderState {

    @Unique
    private boolean prism_shouldReceiveCustomData = false;


    @Override
    public boolean getPrism_shouldReceiveCustomData() {
        return prism_shouldReceiveCustomData;
    }

    @Override
    public void setPrism_shouldReceiveCustomData(boolean value) {
        prism_shouldReceiveCustomData = value;
    }

    @WrapOperation(
            method = "submit",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/special/SpecialModelRenderer;submit(Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;IIZI)V"
            )
    )
    private void modifySpecialRendererSubmitLightCoordinatesForEmissiveLayers(
            SpecialModelRenderer<Object> instance,
            @Nullable Object argument,
            PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
            int lightCoords,
            int overlayCoords,
            boolean hasFoil,
            int outlineColor,
            Operation<Void> original
    ) {
        ItemStackRenderState.LayerRenderState asLayerRenderState = (ItemStackRenderState.LayerRenderState) (Object) this;
        LightCoordinates customLightCoordinates = asLayerRenderState.getData(PrismRenderStateDataKeys.INSTANCE.getCUSTOM_LIGHT_COORDINATES());

        if (customLightCoordinates != null) {
            LightLevel originalLightLevel = LightLevel.Companion.fromPackedCoordinates(lightCoords);
            original.call(
                    instance,
                    argument,
                    poseStack,
                    submitNodeCollector,
                    originalLightLevel.mix(customLightCoordinates.get(originalLightLevel)).getPackedCoordinates(),
                    overlayCoords,
                    hasFoil,
                    outlineColor
            );
        } else
            original.call(
                    instance,
                    argument,
                    poseStack,
                    submitNodeCollector,
                    lightCoords,
                    overlayCoords,
                    hasFoil,
                    outlineColor
            );
    }

    @WrapOperation(
            method = "submit",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitItem(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/item/ItemDisplayContext;III[ILjava/util/List;Lnet/minecraft/client/renderer/item/ItemStackRenderState$FoilType;)V"
            )
    )
    private void modifySubmitItemLightCoordinatesForEmissiveLayers(
            SubmitNodeCollector instance,
            PoseStack poseStack,
            ItemDisplayContext displayContext,
            int lightCoords,
            int overlayCoords,
            int outlineColor,
            int[] tintLayers,
            List<BakedQuad> quads,
            ItemStackRenderState.FoilType foilType,
            Operation<Void> original
    ) {
        ItemStackRenderState.LayerRenderState asLayerRenderState = (ItemStackRenderState.LayerRenderState) (Object) this;
        LightCoordinates customLightCoordinates = asLayerRenderState.getData(PrismRenderStateDataKeys.INSTANCE.getCUSTOM_LIGHT_COORDINATES());

        if (customLightCoordinates != null) {
            LightLevel originalLightLevel = LightLevel.Companion.fromPackedCoordinates(lightCoords);
            original.call(
                    instance,
                    poseStack,
                    displayContext,
                    originalLightLevel.mix(customLightCoordinates.get(originalLightLevel)).getPackedCoordinates(),
                    overlayCoords,
                    outlineColor,
                    tintLayers,
                    quads,
                    foilType
            );
        } else
            original.call(
                    instance,
                    poseStack,
                    displayContext,
                    lightCoords,
                    overlayCoords,
                    outlineColor,
                    tintLayers,
                    quads,
                    foilType
            );
    }
}
