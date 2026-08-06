package net.mrwooly357.prism.client.render.block.state.model

import com.mojang.serialization.MapCodec
import net.fabricmc.fabric.api.client.model.loading.v1.CustomUnbakedBlockStateModel
import net.mrwooly357.prism.client.Prism

object PrismUnbakedBlockStateModels {

    init {
        register("enhanced", EnhancedBlockStateModel.Unbaked.MAP_CODEC)
    }


    private fun <M : CustomUnbakedBlockStateModel> register(id: String, mapCodec: MapCodec<M>) {
        CustomUnbakedBlockStateModel.register(
            Prism.id(id),
            mapCodec
        )
    }
}
