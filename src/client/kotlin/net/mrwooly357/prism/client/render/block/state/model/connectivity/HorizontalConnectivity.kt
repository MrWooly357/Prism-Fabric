package net.mrwooly357.prism.client.render.block.state.model.connectivity

import net.minecraft.core.Direction
import net.mrwooly357.prism.client.render.block.state.model.connectivity.face.HorizontalFaceConnectivity
import java.util.EnumMap

class HorizontalConnectivity(
    private val faces: EnumMap<Direction, HorizontalFaceConnectivity> = EnumMap(Direction::class.java)
) {


    fun getOrCreate(face: Direction): HorizontalFaceConnectivity {
        return faces.getOrPut(face) { HorizontalFaceConnectivity() }
    }
}
