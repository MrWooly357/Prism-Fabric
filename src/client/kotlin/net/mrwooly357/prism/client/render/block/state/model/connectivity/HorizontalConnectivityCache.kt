package net.mrwooly357.prism.client.render.block.state.model.connectivity

import net.minecraft.core.BlockPos
import java.util.concurrent.ConcurrentHashMap

object HorizontalConnectivityCache {

    private val ENTRIES: MutableMap<BlockPos, HorizontalConnectivity> = ConcurrentHashMap()


    fun getOrCreate(pos: BlockPos): HorizontalConnectivity {
        return ENTRIES.getOrPut(pos) { HorizontalConnectivity() }
    }

    fun clear(pos: BlockPos) {
        ENTRIES.remove(pos)
    }
}
