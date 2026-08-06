package net.mrwooly357.prism.client.mixin;

import net.minecraft.client.renderer.item.ConditionalItemModel;
import net.minecraft.client.renderer.item.ItemModel;
import net.mrwooly357.prism.client.render.item.model.PrismItemModel;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ConditionalItemModel.class)
public abstract class ConditionalItemModelMixin implements PrismItemModel {

    @Shadow
    @Final
    private ItemModel onTrue;
    @Shadow
    @Final
    private ItemModel onFalse;
    @Unique
    private boolean prism_shouldReceiveCustomData = false;


    @Override
    public boolean getPrism_shouldReceiveCustomData() {
        return prism_shouldReceiveCustomData;
    }

    @Override
    public void setPrism_shouldReceiveCustomData(boolean value) {
        prism_shouldReceiveCustomData = value;

        if (onTrue instanceof PrismItemModel prismModel)
            prismModel.setPrism_shouldReceiveCustomData(value);

        if (onFalse instanceof PrismItemModel prismModel)
            prismModel.setPrism_shouldReceiveCustomData(value);
    }
}
