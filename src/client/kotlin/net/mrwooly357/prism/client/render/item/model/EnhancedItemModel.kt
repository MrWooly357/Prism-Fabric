package net.mrwooly357.prism.client.render.item.model

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.fabricmc.fabric.api.client.model.loading.v1.wrapper.WrapperBakedItemModel
import net.fabricmc.fabric.api.client.model.loading.v1.wrapper.WrapperUnbakedItemModel
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.renderer.item.ItemModel
import net.minecraft.client.renderer.item.ItemModelResolver
import net.minecraft.client.renderer.item.ItemModels
import net.minecraft.client.renderer.item.ItemStackRenderState
import net.minecraft.world.entity.ItemOwner
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import net.mrwooly357.prism.client.render.item.model.properties.ItemModelEmissivity
import org.joml.Matrix4fc

data class EnhancedItemModel(
    val model: ItemModel,
    val emissivity: ItemModelEmissivity
) : WrapperBakedItemModel(model), PrismItemModel {

    override var prism_shouldReceiveCustomData: Boolean = true


    override fun update(
        state: ItemStackRenderState,
        stack: ItemStack,
        resolver: ItemModelResolver,
        displayContext: ItemDisplayContext,
        level: ClientLevel?,
        itemOwner: ItemOwner?,
        seed: Int
    ) {
        super.update(state, stack, resolver, displayContext, level, itemOwner, seed)

        emissivity.apply(state, stack, resolver, displayContext, level, itemOwner, seed)
    }


    fun interface Property {


        fun apply(
            state: ItemStackRenderState,
            stack: ItemStack,
            resolver: ItemModelResolver,
            displayContext: ItemDisplayContext,
            level: ClientLevel?,
            itemOwner: ItemOwner?,
            seed: Int
        )
    }


    data class Unbaked(
        val model: ItemModel.Unbaked,
        val emissivity: ItemModelEmissivity
    ) : WrapperUnbakedItemModel(model) {


        companion object {

            val MAP_CODEC: MapCodec<Unbaked> = RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    ItemModels.CODEC.fieldOf("model").forGetter(Unbaked::model),
                    ItemModelEmissivity.CODEC.optionalFieldOf("emissivity", ItemModelEmissivity.None).forGetter(Unbaked::emissivity)
                )
                    .apply(instance, ::Unbaked)
            }
        }


        override fun type(): MapCodec<Unbaked> = MAP_CODEC

        override fun bake(context: ItemModel.BakingContext, transformation: Matrix4fc): ItemModel {
            val model = super.bake(context, transformation)

            if (model is PrismItemModel)
                model.prism_shouldReceiveCustomData = true

            return EnhancedItemModel(model, emissivity)
        }
    }
}
