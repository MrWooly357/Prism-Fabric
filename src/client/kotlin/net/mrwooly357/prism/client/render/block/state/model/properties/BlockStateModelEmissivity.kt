package net.mrwooly357.prism.client.render.block.state.model.properties

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.fabricmc.fabric.api.client.renderer.v1.mesh.MutableQuadView
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadEmitter
import net.fabricmc.fabric.api.util.TriState
import net.minecraft.client.renderer.block.BlockAndTintGetter
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.util.RandomSource
import net.minecraft.util.StringRepresentable
import net.minecraft.world.level.block.state.BlockState
import net.mrwooly357.prism.client.render.block.state.model.EnhancedBlockStateModel
import net.mrwooly357.prism.client.render.light.LightCoordinates
import net.mrwooly357.prism.client.render.light.LightLevel
import java.util.function.Predicate

sealed interface BlockStateModelEmissivity : EnhancedBlockStateModel.Property {

    val type: Type


    companion object {

        val CODEC: Codec<BlockStateModelEmissivity> = Type.CODEC.dispatch(BlockStateModelEmissivity::type, Type::mapCodec)
    }


    data object None : BlockStateModelEmissivity {

        override val type: Type = Type.NONE
        val MAP_CODEC: MapCodec<None> = MapCodec.unit(this)


        override fun apply(
            emitter: QuadEmitter,
            level: BlockAndTintGetter,
            pos: BlockPos,
            state: BlockState,
            random: RandomSource,
            cullTest: Predicate<Direction?>,
            quad: MutableQuadView
        ) {}
    }


    data class Emissive(
        val light: LightCoordinates
    ) : BlockStateModelEmissivity {

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
            emitter: QuadEmitter,
            level: BlockAndTintGetter,
            pos: BlockPos,
            state: BlockState,
            random: RandomSource,
            cullTest: Predicate<Direction?>,
            quad: MutableQuadView
        ) {
            for (i in 0..3) {
                val light = quad.lightmap(i)
                val modifiedLight = this.light[LightLevel.fromPackedCoordinates(light)].packedCoordinates
                quad.lightmap(i, modifiedLight)
                quad.diffuseShade(false)
                quad.ambientOcclusion(TriState.FALSE)
            }
        }
    }


    enum class Type(
        val id: String,
        val mapCodec: MapCodec<out BlockStateModelEmissivity>
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
