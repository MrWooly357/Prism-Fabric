package net.mrwooly357.prism.client.mixin;

import net.minecraft.client.renderer.item.CompositeModel;
import net.minecraft.client.renderer.item.ItemModel;
import net.mrwooly357.prism.client.render.item.model.PrismItemModel;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(CompositeModel.class)
public abstract class CompositeModelMixin implements PrismItemModel {

    @Shadow
    @Final
    private List<ItemModel> models;
    @Unique
    private boolean prism_shouldReceiveCustomData = false;


    @Override
    public boolean getPrism_shouldReceiveCustomData() {
        return prism_shouldReceiveCustomData;
    }

    @Override
    public void setPrism_shouldReceiveCustomData(boolean value) {
        prism_shouldReceiveCustomData = value;

        for (ItemModel child : models)
            if (child instanceof PrismItemModel prismModel)
                prismModel.setPrism_shouldReceiveCustomData(value);
    }
}
