package net.mrwooly357.prism.client.render.block.state.model.connectivity.face

class HorizontalFaceConnectivity(
    var right: Boolean? = null,
    var left: Boolean? = null
) {

    val variant: UInt
        get() {
            val isRight = if (right == true) 1 else 0
            val isLeft = if (left == true) 1 else 0
            val mask = (isLeft shl 1) or isRight

            return when (mask) {
                0b01 -> 1U
                0b11 -> 2U
                0b10 -> 3U
                else -> 0U
            }
        }
}
