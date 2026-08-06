package net.mrwooly357.prism.client.render.block.state.model.connectivity

import com.mojang.serialization.Codec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.util.StringRepresentable

enum class ConnectivityDirection(
    val id: String,
    val oppositeId: UInt,
    val xOffset: Int,
    val yOffset: Int
) : StringRepresentable {

    UP("up", 4U, 0, 1),
    UP_RIGHT("up_right", 5U, 1, 1),
    RIGHT("right", 6U, 1, 0),
    RIGHT_DOWN("right_down", 7U, 1, -1),
    DOWN("down", 0U, 0, -1),
    DOWN_LEFT("down_left", 1U, -1, -1),
    LEFT("left", 2U, -1, 0),
    LEFT_UP("left_up", 3U, -1, 1);

    val opposite: ConnectivityDirection by lazy { entries[oppositeId.toInt()] }


    companion object {

        val CODEC: Codec<ConnectivityDirection> = StringRepresentable.fromEnum(ConnectivityDirection::values)
    }


    override fun getSerializedName(): String {
        return id
    }

    fun getPosForFace(face: Direction, pos: BlockPos): BlockPos {
        return when (face) {
            Direction.DOWN -> pos.offset(xOffset, 0, xOffset)
            Direction.UP -> pos.offset(xOffset, 0, -yOffset)
            Direction.NORTH -> pos.offset(-xOffset, yOffset, 0)
            Direction.SOUTH -> pos.offset(xOffset, yOffset, 0)
            Direction.WEST -> pos.offset(0, yOffset, xOffset)
            Direction.EAST -> pos.offset(0, yOffset, -xOffset)
        }
    }

    override fun toString(): String {
        return id
    }
}
