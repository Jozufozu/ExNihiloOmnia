package com.jozufozu.exnihiloomnia.client.ores.models

import com.google.common.collect.ImmutableMap
import com.jozufozu.exnihiloomnia.ExNihilo
import net.minecraft.client.resources.IResourceManager
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.ICustomModelLoader
import net.minecraftforge.client.model.IModel
import net.minecraftforge.client.model.ModelLoaderRegistry

object ExNihiloOreModels {
    fun preInit() {
        ModelLoaderRegistry.registerLoader(OreBlockModelLoader)
        ModelLoaderRegistry.registerLoader(OreItemModelLoader)
    }

    object OreBlockModelLoader : ICustomModelLoader {
        override fun loadModel(modelLocation: ResourceLocation): IModel {
            val model = ModelLoaderRegistry.getModel(ResourceLocation("minecraft", "block/cube_all"))

            return model.retexture(ImmutableMap.of("all", "${modelLocation.resourceDomain}:${modelLocation.resourcePath}"))
        }

        override fun onResourceManagerReload(resourceManager: IResourceManager) {}

        override fun accepts(modelLocation: ResourceLocation): Boolean {
            return modelLocation.resourceDomain == ExNihilo.MODID && modelLocation.resourcePath.startsWith("ores/block/")
        }
    }

    object OreItemModelLoader : ICustomModelLoader {
        override fun loadModel(modelLocation: ResourceLocation): IModel {
            val model = ModelLoaderRegistry.getModel(ResourceLocation("minecraft", "item/generated"))

            return model.retexture(ImmutableMap.of("layer0", "${modelLocation.resourceDomain}:${modelLocation.resourcePath}"))
        }

        override fun onResourceManagerReload(resourceManager: IResourceManager) {}

        override fun accepts(modelLocation: ResourceLocation): Boolean {
            return modelLocation.resourceDomain == ExNihilo.MODID && modelLocation.resourcePath.startsWith("ores/item/")
        }
    }
}