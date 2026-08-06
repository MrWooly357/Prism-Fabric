package net.mrwooly357.prism.client.render.item.model

import com.mojang.serialization.MapCodec
import net.minecraft.client.renderer.item.ItemModel
import net.minecraft.client.renderer.item.ItemModels
import net.mrwooly357.prism.client.Prism

object PrismItemModels {

    init {
        register("enhanced", EnhancedItemModel.Unbaked.MAP_CODEC)
    }


    private fun register(id: String, codec: MapCodec<out ItemModel.Unbaked>) {
        ItemModels.ID_MAPPER.put(Prism.id(id), codec)
    }
}
