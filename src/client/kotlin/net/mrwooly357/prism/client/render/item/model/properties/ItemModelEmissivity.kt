package net.mrwooly357.prism.client.render.item.model.properties

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.renderer.item.ItemModelResolver
import net.minecraft.client.renderer.item.ItemStackRenderState
import net.minecraft.util.StringRepresentable
import net.minecraft.world.entity.ItemOwner
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import net.mrwooly357.prism.client.render.state.PrismRenderStateDataKeys
import net.mrwooly357.prism.client.render.item.model.EnhancedItemModel
import net.mrwooly357.prism.client.render.light.LightCoordinates

sealed interface ItemModelEmissivity : EnhancedItemModel.Property {

    val type: Type


    companion object {

        val CODEC: Codec<ItemModelEmissivity> = Type.CODEC.dispatch(ItemModelEmissivity::type, Type::mapCodec)
    }


    data object None : ItemModelEmissivity {

        override val type: Type = Type.NONE
        val MAP_CODEC: MapCodec<None> = MapCodec.unit(this)


        override fun apply(
            state: ItemStackRenderState,
            stack: ItemStack,
            resolver: ItemModelResolver,
            displayContext: ItemDisplayContext,
            level: ClientLevel?,
            itemOwner: ItemOwner?,
            seed: Int
        ) {}
    }


    data class Emissive(
        val light: LightCoordinates
    ) : ItemModelEmissivity {

        override val type: Type = Type.EMISSIVE


        companion object {

            val MAP_CODEC: MapCodec<Emissive> = RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    LightCoordinates.CODEC.fieldOf("light").forGetter(Emissive::light)
                )
                    .apply(instance, ::Emissive)
            }
        }


        override fun apply(
            state: ItemStackRenderState,
            stack: ItemStack,
            resolver: ItemModelResolver,
            displayContext: ItemDisplayContext,
            level: ClientLevel?,
            itemOwner: ItemOwner?,
            seed: Int
        ) {
            for (layer in state.layers)
                if (layer.prism_shouldReceiveCustomData)
                layer.setData(PrismRenderStateDataKeys.CUSTOM_LIGHT_COORDINATES, light)
        }

        override fun toString(): String = light.toString()
    }


    enum class Type(
        val id: String,
        val mapCodec: MapCodec<out ItemModelEmissivity>
    ) : StringRepresentable {

        NONE("none", None.MAP_CODEC),
        EMISSIVE("emissive", Emissive.MAP_CODEC);


        companion object {

            val CODEC: Codec<Type> = StringRepresentable.fromEnum(Type::values)
        }


        override fun getSerializedName(): String = id

        override fun toString(): String = id
    }
}
