package net.mrwooly357.prism.client

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator

data object PrismDataGenerator : DataGeneratorEntrypoint {


	override fun onInitializeDataGenerator(generator: FabricDataGenerator) {
		Prism.LOGGER.info("Initialising $this.")
	}
}
