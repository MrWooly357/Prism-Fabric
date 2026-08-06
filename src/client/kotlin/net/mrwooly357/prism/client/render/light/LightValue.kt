package net.mrwooly357.prism.client.render.light

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.util.StringRepresentable
import net.mrwooly357.prism.client.util.CustomCodecs

sealed class LightValue(
    open val lightType: LightType
) {

    abstract val type: Type


    companion object {

        val CODEC: Codec<LightValue> = Type.CODEC.dispatch(LightValue::type, Type::mapCodec)
    }


    abstract operator fun get(original: LightLevel): UInt


    data class Auto(
        override val lightType: LightType
    ) : LightValue(lightType) {

        override val type: Type = Type.AUTO


        companion object {

            val MAP_CODEC: MapCodec<Auto> = RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    LightType.CODEC.fieldOf("light_type").forGetter(Auto::lightType)
                )
                    .apply(instance, ::Auto)
            }
        }


        override fun get(original: LightLevel): UInt = original[lightType]

        override fun toString(): String = "$lightType: auto"
    }


    data class Custom(
        override val lightType: LightType,
        val value: UInt
    ) : LightValue(lightType) {

        override val type: Type = Type.CUSTOM

        init {
            LightUtils.check(value)
        }


        companion object {

            val MAP_CODEC: MapCodec<Custom> = RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    LightType.CODEC.fieldOf("light_type").forGetter(Custom::lightType),
                    CustomCodecs.UINT.fieldOf("value").forGetter(Custom::value)
                )
                    .apply(instance, ::Custom)
            }
        }


        override fun get(original: LightLevel): UInt = value

        override fun toString(): String = "$lightType: $value"
    }


    enum class Type(
        val id: String,
        val mapCodec: MapCodec<out LightValue>
    ) : StringRepresentable {

        AUTO("auto", Auto.MAP_CODEC),
        CUSTOM("custom", Custom.MAP_CODEC);


        companion object {

            val CODEC: Codec<Type> = StringRepresentable.fromEnum(Type::values)
        }


        override fun getSerializedName(): String = id

        override fun toString(): String = id
    }
}
