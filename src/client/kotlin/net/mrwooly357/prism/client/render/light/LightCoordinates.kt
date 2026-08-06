package net.mrwooly357.prism.client.render.light

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.util.StringRepresentable

sealed interface LightCoordinates {

    val type: Type
    val sky: LightValue
    val block: LightValue


    companion object {

        val DEFAULT: Specific = Specific(LightValue.Auto(LightType.SKY), LightValue.Auto(LightType.BLOCK))
        val CODEC: Codec<LightCoordinates> = Type.CODEC.dispatch(LightCoordinates::type, Type::mapCodec)
    }


    operator fun get(original: LightLevel): LightLevel = LightLevel(sky[original], block[original])


    data class General(
        val value: LightValue
    ) : LightCoordinates {

        override val type: Type = Type.GENERAL
        override val sky: LightValue = value
        override val block: LightValue = value


        companion object {

            val MAP_CODEC: MapCodec<General> = RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    LightValue.CODEC.fieldOf("value").forGetter(General::value)
                )
                    .apply(instance, ::General)
            }
        }


        override fun toString(): String = "($value, $value)"
    }


    data class Specific(
        override val sky: LightValue,
        override val block: LightValue
    ) : LightCoordinates {

        override val type: Type = Type.SPECIFIC


        companion object {

            val MAP_CODEC: MapCodec<Specific> = RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    LightValue.CODEC.optionalFieldOf(
                        "sky",
                        LightValue.Auto(LightType.SKY)
                    ).forGetter(Specific::sky),
                    LightValue.CODEC.optionalFieldOf(
                        "block",
                        LightValue.Auto(LightType.BLOCK)
                    ).forGetter(Specific::block),
                )
                    .apply(instance, ::Specific)
            }
        }


        override fun toString(): String = "($sky, $block)"
    }


    enum class Type(
        val id: String,
        val mapCodec: MapCodec<out LightCoordinates>
    ) : StringRepresentable {

        GENERAL("general", General.MAP_CODEC),
        SPECIFIC("specific", Specific.MAP_CODEC);


        companion object {

            val CODEC: Codec<Type> = StringRepresentable.fromEnum(Type::values)
        }


        override fun getSerializedName(): String = id

        override fun toString(): String = id
    }
}
