package com.jozufozu.exnihiloomnia.common.blocks.barrel

import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.ResourceLocation

class GlassBarrelBlock(registryName: ResourceLocation, properties: Properties) : BarrelBlock(registryName, properties) {
    override fun getRenderLayer() = BlockRenderLayer.TRANSLUCENT
}
