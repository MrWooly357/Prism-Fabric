package net.mrwooly357.prism.client.render.block.state.model.connectivity.face

class VerticalFaceConnectivity(
    var up: Boolean? = null,
    var down: Boolean? = null
) {

    val variant: UInt
        get() {
            val isUp = if (up == true) 1 else 0
            val isDown = if (down == true) 1 else 0
            val mask = (isDown shl 1) or isUp

            return when (mask) {
                0b01 -> 3U
                0b11 -> 2U
                0b10 -> 1U
                else -> 0U
            }
        }
}
