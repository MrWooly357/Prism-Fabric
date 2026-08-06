package net.mrwooly357.prism.client.render.state

import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey
import net.mrwooly357.prism.client.Prism
import net.mrwooly357.prism.client.render.light.LightCoordinates

object PrismRenderStateDataKeys {

    val CUSTOM_LIGHT_COORDINATES: RenderStateDataKey<LightCoordinates> = of("custom_light_coordinates")


    private fun <T : Any> of(id: String): RenderStateDataKey<T> = RenderStateDataKey.create { "${Prism.MOD_ID}:$id" }
}
