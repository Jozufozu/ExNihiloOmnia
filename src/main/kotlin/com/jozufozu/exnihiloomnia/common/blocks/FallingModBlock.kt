package com.jozufozu.exnihiloomnia.common.blocks

import net.minecraft.block.FallingBlock
import net.minecraft.util.ResourceLocation

open class FallingModBlock(registryName: ResourceLocation, properties: Properties) : FallingBlock(properties) {
    init {
        this.registryName = registryName
    }
}
