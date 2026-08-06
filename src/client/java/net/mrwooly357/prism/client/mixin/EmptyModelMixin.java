package net.mrwooly357.prism.client.mixin;

import net.minecraft.client.renderer.item.EmptyModel;
import net.mrwooly357.prism.client.render.item.model.PrismItemModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EmptyModel.class)
public abstract class EmptyModelMixin implements PrismItemModel {

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
