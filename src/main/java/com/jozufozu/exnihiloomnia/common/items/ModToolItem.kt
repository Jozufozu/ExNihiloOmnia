package com.jozufozu.exnihiloomnia.common.items

import com.google.common.collect.Sets
import com.jozufozu.exnihiloomnia.ExNihilo
import com.jozufozu.exnihiloomnia.common.util.Identifiable
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ToolItem
import net.minecraft.item.ToolMaterial
import net.minecraft.util.Identifier
import net.minecraft.util.ResourceLocation

open class ModToolItem(override val identifier: Identifier, toolMaterial: ToolMaterial) : ToolItem(toolMaterial, Settings().group(ExNihiloTabs.ITEMS)), Identifiable<Item> {

    init {
        name
        this.setUnlocalizedName(ExNihilo.MODID + "." + registryName.getResourcePath())
    }

    override fun getIdentified() = this
}
