package net.mrwooly357.prism.client.mixin;

import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.RangeSelectItemModel;
import net.mrwooly357.prism.client.render.item.model.PrismItemModel;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(RangeSelectItemModel.class)
public abstract class RangeSelectItemModelMixin implements PrismItemModel {

    @Shadow
    @Final
    private ItemModel[] models;
    @Shadow
    @Final
    private ItemModel fallback;
    @Unique
    private boolean prism_shouldReceiveCustomData = false;


    @Override
    public boolean getPrism_shouldReceiveCustomData() {
        return prism_shouldReceiveCustomData;
    }

    @Override
    public void setPrism_shouldReceiveCustomData(boolean value) {
        prism_shouldReceiveCustomData = value;

        for (ItemModel model : models)
            if (model instanceof PrismItemModel prismModel)
                prismModel.setPrism_shouldReceiveCustomData(value);

        if (fallback instanceof PrismItemModel prismModel)
            prismModel.setPrism_shouldReceiveCustomData(value);
    }
}
