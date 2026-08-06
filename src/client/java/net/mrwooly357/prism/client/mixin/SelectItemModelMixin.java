package net.mrwooly357.prism.client.mixin;

import net.minecraft.client.renderer.item.SelectItemModel;
import net.mrwooly357.prism.client.render.item.model.PrismItemModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SelectItemModel.class)
public abstract class SelectItemModelMixin implements PrismItemModel {

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
}
