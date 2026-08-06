package net.mrwooly357.prism.client.level.block.state.predicate

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import net.minecraft.client.renderer.block.BlockAndTintGetter
import net.minecraft.core.BlockPos
import net.minecraft.resources.Identifier
import net.minecraft.world.level.block.state.BlockState

sealed interface BlockStatePredicate {

    val type: Type<out BlockStatePredicate>


    companion object {

        val CODEC: Codec<BlockStatePredicate> = Type.CODEC.dispatch(BlockStatePredicate::type) { type -> type.mapCodec }
    }


    fun test(level: BlockAndTintGetter, pos: BlockPos, state: BlockState): Boolean


    data class Type<P : BlockStatePredicate>(
        val mapCodec: MapCodec<P>
    ) {


        companion object {

            val CODEC: Codec<Type<*>> = Identifier.CODEC.xmap(
                BlockStatePredicateTypeRegistry::get,
                BlockStatePredicateTypeRegistry::getId
            )
        }
    }
}
