package net.mrwooly357.prism.client

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.resources.Identifier
import net.mrwooly357.prism.client.render.block.state.model.PrismUnbakedBlockStateModels
import net.mrwooly357.prism.client.render.item.model.PrismItemModels
import org.slf4j.Logger
import org.slf4j.LoggerFactory

data object Prism : ClientModInitializer {

	const val MOD_ID = "prism"
	val LOGGER: Logger = LoggerFactory.getLogger("Prism")


	@Suppress("UNUSED_EXPRESSION")
	override fun onInitializeClient() {
		FabricLoader.getInstance().getModContainer(MOD_ID).ifPresent { mod ->
			val version = mod.metadata.version
			LOGGER.info("Initialising Prism $version.")
		}
		PrismItemModels
        PrismUnbakedBlockStateModels
	}

	fun id(path: String) = Identifier.fromNamespaceAndPath(MOD_ID, path)
}
