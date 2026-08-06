package net.mrwooly357.prism.client.render.block.state.model.connectivity

import net.minecraft.core.Direction
import net.mrwooly357.prism.client.render.block.state.model.connectivity.face.VerticalFaceConnectivity
import java.util.*

class VerticalConnectivity(
    private val faces: EnumMap<Direction, VerticalFaceConnectivity> = EnumMap(Direction::class.java)
) {


    fun getOrCreate(face: Direction): VerticalFaceConnectivity {
        return faces.getOrPut(face) { VerticalFaceConnectivity() }
    }
}
