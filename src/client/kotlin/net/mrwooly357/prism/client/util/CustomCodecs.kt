package net.mrwooly357.prism.client.util

import com.mojang.serialization.Codec

object CustomCodecs {

    val UINT: Codec<UInt> = Codec.INT.xmap(
        { i -> i.toUInt() },
        { ui -> ui.toInt() }
    )
}
