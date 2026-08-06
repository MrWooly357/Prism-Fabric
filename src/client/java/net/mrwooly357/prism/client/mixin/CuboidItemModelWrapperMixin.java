package net.mrwooly357.prism.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.item.CuboidItemModelWrapper;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.mrwooly357.prism.client.render.item.model.PrismItemModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CuboidItemModelWrapper.class)
public abstract class CuboidItemModelWrapperMixin implements PrismItemModel {

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
            method = "update",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/item/ItemStackRenderState;newLayer()Lnet/minecraft/client/renderer/item/ItemStackRenderState$LayerRenderState;"
            )
    )
    private ItemStackRenderState.LayerRenderState setLayerShouldReceiveCustomData(
            ItemStackRenderState instance,
            Operation<ItemStackRenderState.LayerRenderState> original
    ) {
        ItemStackRenderState.LayerRenderState layer = original.call(instance);

        if (prism_shouldReceiveCustomData)
            layer.setPrism_shouldReceiveCustomData(true);

        return layer;
    }
}
