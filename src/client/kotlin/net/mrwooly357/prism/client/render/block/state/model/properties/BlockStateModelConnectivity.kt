package net.mrwooly357.prism.client.render.block.state.model.properties

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.fabricmc.fabric.api.client.renderer.v1.mesh.MutableQuadView
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadEmitter
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.Sheets
import net.minecraft.client.renderer.block.BlockAndTintGetter
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.resources.model.sprite.SpriteId
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.resources.Identifier
import net.minecraft.util.RandomSource
import net.minecraft.util.StringRepresentable
import net.minecraft.world.level.block.state.BlockState
import net.mrwooly357.prism.client.level.block.state.predicate.BlockStatePredicate
import net.mrwooly357.prism.client.render.block.state.model.EnhancedBlockStateModel
import net.mrwooly357.prism.client.render.block.state.model.connectivity.HorizontalConnectivityCache
import net.mrwooly357.prism.client.render.block.state.model.connectivity.ConnectivityDirection
import net.mrwooly357.prism.client.render.block.state.model.connectivity.VerticalConnectivityCache
import net.mrwooly357.prism.client.util.CacheableValue
import java.util.*
import java.util.function.Predicate

sealed interface BlockStateModelConnectivity : EnhancedBlockStateModel.Property {

    val type: Type


    companion object {

        val CODEC: Codec<BlockStateModelConnectivity> = Type.CODEC.dispatch(BlockStateModelConnectivity::type, Type::mapCodec)
    }


    data object None : BlockStateModelConnectivity {

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


    class Horizontal(
        val faces: EnumSet<Direction>,
        val textures: SpriteId
    ) : BlockStateModelConnectivity {

        override val type: Type = Type.HORIZONTAL
        private val spriteCache: CacheableValue<TextureAtlasSprite> = CacheableValue()


        companion object {

            val MAP_CODEC: MapCodec<Horizontal> = RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    Direction.CODEC.listOf().xmap(
                        { list -> EnumSet.copyOf(list) },
                        { faces -> faces.toList() }
                    ).fieldOf("faces").forGetter(Horizontal::faces),
                    Identifier.CODEC.xmap(
                        { id -> Sheets.BLOCKS_MAPPER.apply(id) },
                        SpriteId::texture
                    ).fieldOf("textures").forGetter(Horizontal::textures)
                )
                    .apply(instance, ::Horizontal)
            }
            private const val TEXTURE_VARIANT_STEP: Float = 0.33333334F
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
            val connectivity = HorizontalConnectivityCache.getOrCreate(pos)
            val sprite = spriteCache.getOrSupply {
                val minecraft = Minecraft.getInstance()

                return@getOrSupply minecraft.atlasManager[textures]
            }
            val face = quad.nominalFace()

            if (face != null && face in faces && !cullTest.test(face)) {
                val faceConnectivity = connectivity.getOrCreate(face)

                if (faceConnectivity.right == null) {
                    val checkPos = ConnectivityDirection.RIGHT.getPosForFace(face, pos)
                    val checkState = level.getBlockState(checkPos)
                    val isRight = checkState.block == state.block
                    faceConnectivity.right = isRight
                }

                if (faceConnectivity.left == null) {
                    val checkPos = ConnectivityDirection.LEFT.getPosForFace(face, pos)
                    val checkState = level.getBlockState(checkPos)
                    faceConnectivity.left = checkState.block == state.block
                }

                val variant = faceConnectivity.variant.toFloat() - 1.0F

                if (variant != -1.0F) {
                    val uMin = sprite.getU(variant * TEXTURE_VARIANT_STEP)
                    val uMax = sprite.getU((variant + 1.0F) * TEXTURE_VARIANT_STEP)
                    val vMin = sprite.getV(0.0F)
                    val vMax = sprite.getV(1.0F)
                    quad.uv(0, uMin, vMin)
                    quad.uv(1, uMin, vMax)
                    quad.uv(2, uMax, vMax)
                    quad.uv(3, uMax, vMin)
                }
            }

            HorizontalConnectivityCache.clear(pos)
        }
    }


    class Vertical(
        val faces: EnumSet<Direction>,
        val textures: SpriteId
    ) : BlockStateModelConnectivity {

        override val type: Type = Type.VERTICAL
        private val spriteCache: CacheableValue<TextureAtlasSprite> = CacheableValue()


        companion object {

            val MAP_CODEC: MapCodec<Vertical> = RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    Direction.CODEC.listOf().xmap(
                        { list -> EnumSet.copyOf(list) },
                        { faces -> faces.toList() }
                    ).fieldOf("faces").forGetter(Vertical::faces),
                    Identifier.CODEC.xmap(
                        { id -> Sheets.BLOCKS_MAPPER.apply(id) },
                        SpriteId::texture
                    ).fieldOf("textures").forGetter(Vertical::textures)
                )
                    .apply(instance, ::Vertical)
            }
            private const val TEXTURE_VARIANT_STEP: Float = 0.33333334F
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
            val connectivity = VerticalConnectivityCache.getOrCreate(pos)
            val sprite = spriteCache.getOrSupply {
                val minecraft = Minecraft.getInstance()

                return@getOrSupply minecraft.atlasManager[textures]
            }
            val face = quad.nominalFace()

            if (face != null && face in faces && !cullTest.test(face)) {
                val faceConnectivity = connectivity.getOrCreate(face)

                if (faceConnectivity.up == null) {
                    val checkPos = ConnectivityDirection.UP.getPosForFace(face, pos)
                    val checkState = level.getBlockState(checkPos)
                    val isUp = checkState.block == state.block
                    faceConnectivity.up = isUp
                }

                if (faceConnectivity.down == null) {
                    val checkPos = ConnectivityDirection.DOWN.getPosForFace(face, pos)
                    val checkState = level.getBlockState(checkPos)
                    faceConnectivity.down = checkState.block == state.block
                }

                val variant = faceConnectivity.variant.toFloat() - 1.0F

                if (variant != -1.0F) {
                    val uMin = sprite.getU(0.0F)
                    val uMax = sprite.getU(1.0F)
                    val vMin = sprite.getV(variant * TEXTURE_VARIANT_STEP)
                    val vMax = sprite.getV((variant + 1.0F) * TEXTURE_VARIANT_STEP)
                    quad.uv(0, uMin, vMin)
                    quad.uv(1, uMin, vMax)
                    quad.uv(2, uMax, vMax)
                    quad.uv(3, uMax, vMin)
                }
            }

            VerticalConnectivityCache.clear(pos)
        }
    }


    class All(
        val connectsTo: BlockStatePredicate
    ) : BlockStateModelConnectivity {

        override val type: Type = Type.ALL


        companion object {

            val MAP_CODEC: MapCodec<All> = RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    BlockStatePredicate.CODEC.fieldOf("connects_to").forGetter(All::connectsTo)
                )
                    .apply(instance, ::All)
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
            TODO("Not yet implemented")
        }
    }


    enum class Type(
        val id: String,
        val mapCodec: MapCodec<out BlockStateModelConnectivity>
    ) : StringRepresentable {

        NONE("none", None.MAP_CODEC),
        HORIZONTAL("horizontal", Horizontal.MAP_CODEC),
        VERTICAL("vertical", Vertical.MAP_CODEC),
        ALL("all", All.MAP_CODEC);


        companion object {

            val CODEC: Codec<Type> = StringRepresentable.fromEnum(Type::values)
        }


        override fun getSerializedName(): String {
            return id
        }

        override fun toString(): String {
            return id
        }
    }
}
