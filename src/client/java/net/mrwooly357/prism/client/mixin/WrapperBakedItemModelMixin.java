package net.mrwooly357.prism.client.mixin;

import net.fabricmc.fabric.api.client.model.loading.v1.wrapper.WrapperBakedItemModel;
import net.minecraft.client.renderer.item.ItemModel;
import net.mrwooly357.prism.client.render.item.model.PrismItemModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(WrapperBakedItemModel.class)
public abstract class WrapperBakedItemModelMixin implements PrismItemModel {

    @Shadow
    protected ItemModel wrapped;
    @Unique
    private boolean prism_shouldReceiveCustomData = false;


    @Override
    public boolean getPrism_shouldReceiveCustomData() {
        return prism_shouldReceiveCustomData;
    }

    @Override
    public void setPrism_shouldReceiveCustomData(boolean value) {
        prism_shouldReceiveCustomData = value;

        if (wrapped instanceof PrismItemModel prismModel)
            prismModel.setPrism_shouldReceiveCustomData(value);
    }
}
