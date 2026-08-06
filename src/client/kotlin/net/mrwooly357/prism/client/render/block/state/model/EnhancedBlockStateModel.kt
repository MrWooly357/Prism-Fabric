package net.mrwooly357.prism.client.render.block.state.model

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.fabricmc.fabric.api.client.model.loading.v1.CustomUnbakedBlockStateModel
import net.fabricmc.fabric.api.client.model.loading.v1.wrapper.WrapperBlockStateModel
import net.fabricmc.fabric.api.client.renderer.v1.mesh.MutableQuadView
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadEmitter
import net.minecraft.client.renderer.block.BlockAndTintGetter
import net.minecraft.client.renderer.block.dispatch.BlockStateModel
import net.minecraft.client.resources.model.ModelBaker
import net.minecraft.client.resources.model.ResolvableModel
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.state.BlockState
import net.mrwooly357.prism.client.render.block.state.model.properties.BlockStateModelConnectivity
import net.mrwooly357.prism.client.render.block.state.model.properties.BlockStateModelEmissivity
import java.util.function.Predicate

class EnhancedBlockStateModel(
    model: BlockStateModel,
    val emissivity: BlockStateModelEmissivity,
    val connectivity: BlockStateModelConnectivity
) : WrapperBlockStateModel(model) {


    override fun emitQuads(
        emitter: QuadEmitter,
        level: BlockAndTintGetter,
        pos: BlockPos,
        state: BlockState,
        random: RandomSource,
        cullTest: Predicate<Direction?>
    ) {
        emitter.pushTransform { quad ->
            emissivity.apply(emitter, level, pos, state, random, cullTest, quad)
            connectivity.apply(emitter, level, pos, state, random, cullTest, quad)
            true
        }

        super.emitQuads(emitter, level, pos, state, random, cullTest)

        emitter.popTransform()
    }


    data class Unbaked(
        val model: BlockStateModel.Unbaked,
        val emissivity: BlockStateModelEmissivity,
        val connectivity: BlockStateModelConnectivity
    ) : BlockStateModel.Unbaked, CustomUnbakedBlockStateModel {

        override fun codec(): MapCodec<Unbaked> = MAP_CODEC


        companion object {

            val MAP_CODEC: MapCodec<Unbaked> = RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    BlockStateModel.Unbaked.CODEC.fieldOf("model").forGetter(Unbaked::model),
                    BlockStateModelEmissivity.CODEC.optionalFieldOf("emissivity", BlockStateModelEmissivity.None)
                        .forGetter(Unbaked::emissivity),
                    BlockStateModelConnectivity.CODEC.optionalFieldOf("connectivity", BlockStateModelConnectivity.None)
                        .forGetter(Unbaked::connectivity)
                )
                    .apply(instance, ::Unbaked)
            }
        }


        override fun resolveDependencies(resolver: ResolvableModel.Resolver) {
            model.resolveDependencies(resolver)
        }

        override fun bake(modelBakery: ModelBaker): BlockStateModel {
            return EnhancedBlockStateModel(model.bake(modelBakery), emissivity, connectivity)
        }
    }


    fun interface Property {


        fun apply(
            emitter: QuadEmitter,
            level: BlockAndTintGetter,
            pos: BlockPos,
            state: BlockState,
            random: RandomSource,
            cullTest: Predicate<Direction?>,
            quad: MutableQuadView
        )
    }
}
