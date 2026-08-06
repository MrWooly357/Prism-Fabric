package net.mrwooly357.prism.client.render.light

import net.minecraft.util.LightCoordsUtil
import kotlin.math.max

data class LightLevel(
    val sky: UInt,
    val block: UInt
) {

    val packedCoordinates: Int by lazy { LightCoordsUtil.pack(block.toInt(), sky.toInt()) }


    companion object {


        fun fromPackedCoordinates(packed: Int): LightLevel {
            val sky = LightCoordsUtil.sky(packed).toUInt()
            val block = LightCoordsUtil.block(packed).toUInt()

            return LightLevel(sky, block)
        }
    }


    operator fun get(type: LightType): UInt = when (type) {
        LightType.SKY -> sky
        LightType.BLOCK -> block
    }

    fun mix(other: LightLevel): LightLevel {
        val sky = max(sky, other.sky)
        val block = max(block, other.block)

        return LightLevel(sky, block)
    }

    override fun toString(): String = "(sky: $sky, block: $block)"
}
