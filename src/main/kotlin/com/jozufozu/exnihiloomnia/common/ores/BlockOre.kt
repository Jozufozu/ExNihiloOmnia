package com.jozufozu.exnihiloomnia.common.ores

import com.jozufozu.exnihiloomnia.common.registries.ores.Ore
import com.jozufozu.exnihiloomnia.common.util.IItemBlockHolder
import net.minecraft.block.BlockFalling
import net.minecraft.item.ItemBlock

class BlockOre(val ore: Ore, val type: BlockType) : BlockFalling(type.material), IItemBlockHolder {
    init {
        registryName = ore.getName(type)
        soundType = type.soundType
        setCreativeTab(OreManager.ORES_TAB)
        unlocalizedName = "exnihiloomnia.ores.${ore.getName(type).resourcePath}"
    }

    override val itemBlock: ItemBlock by lazy { ItemBlock(this).also { it.registryName = registryName } }
}