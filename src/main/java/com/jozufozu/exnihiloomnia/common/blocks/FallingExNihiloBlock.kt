package com.jozufozu.exnihiloomnia.common.blocks

import net.minecraft.block.FallingBlock
import net.minecraft.util.ResourceLocation

class FallingExNihiloBlock constructor(properties: Properties, registryName: ResourceLocation) : FallingBlock(properties) {
    init {
        this.registryName = registryName
    }
}
