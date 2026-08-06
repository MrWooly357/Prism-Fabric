package net.mrwooly357.prism.client.render.light

import com.mojang.serialization.Codec
import net.minecraft.util.StringRepresentable

enum class LightType(
    val id: String
) : StringRepresentable {

    SKY("sky"),
    BLOCK("block");


    companion object {

        val CODEC: Codec<LightType> = StringRepresentable.fromEnum(LightType::values)
    }


    override fun getSerializedName(): String = id

    override fun toString(): String = id
}
