package net.mrwooly357.prism.client.render.block.state.model.connectivity

import net.minecraft.core.BlockPos
import java.util.concurrent.ConcurrentHashMap

object VerticalConnectivityCache {

    private val ENTRIES: MutableMap<BlockPos, VerticalConnectivity> = ConcurrentHashMap()


    fun getOrCreate(pos: BlockPos): VerticalConnectivity {
        return ENTRIES.getOrPut(pos) { VerticalConnectivity() }
    }

    fun clear(pos: BlockPos) {
        ENTRIES.remove(pos)
    }
}
