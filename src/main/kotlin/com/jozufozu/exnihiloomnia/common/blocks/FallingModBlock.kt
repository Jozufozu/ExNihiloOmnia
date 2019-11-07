package com.jozufozu.exnihiloomnia.common.blocks

import com.jozufozu.exnihiloomnia.common.util.Identifiable
import net.minecraft.block.Block
import net.minecraft.block.FallingBlock
import net.minecraft.util.ResourceLocation

open class FallingModBlock(final override val identifier: ResourceLocation, properties: Properties) : FallingBlock(properties), Identifiable<Block> {
    init {
        this.registryName = identifier
    }
}
