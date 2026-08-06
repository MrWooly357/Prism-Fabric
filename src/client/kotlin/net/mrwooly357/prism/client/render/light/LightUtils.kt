package net.mrwooly357.prism.client.render.light

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import net.mrwooly357.prism.client.util.CustomCodecs

object LightUtils {

    val VALUE_CODEC: Codec<UInt> = CustomCodecs.UINT.validate { light ->
        if (light > 15U)
            DataResult.error { "Light value cannot be higher than 15! Given: $light." }
        else
            DataResult.success(light)
    }


    fun check(value: UInt) {
        if (value > 15U)
            throw IllegalArgumentException("Light value cannot be higher than 15! Given: $value.")
    }
}
