package com.jozufozu.exnihiloomnia.common.blocks

import com.jozufozu.exnihiloomnia.ExNihilo
import com.jozufozu.exnihiloomnia.common.items.ExNihiloItems
import com.jozufozu.exnihiloomnia.common.items.ExNihiloTabs
import com.jozufozu.exnihiloomnia.common.util.IItemBlockHolder
import com.jozufozu.exnihiloomnia.common.util.IModelRegister

open class BlockBase @JvmOverloads constructor(registryName: ResourceLocation, materialIn: Material, soundType: SoundType = SoundType.STONE) : Block(materialIn), IItemBlockHolder, IModelRegister {

    init {

        this.soundType = soundType
        this.registryName = registryName
        this.unlocalizedName = ExNihilo.MODID + "." + registryName.resourcePath
        this.setCreativeTab(ExNihiloTabs.BLOCKS)

        if (ExNihiloBlocks.hasRegisteredBlocks())
            ExNihilo.log.warn("Tried to make a block $registryName after registering!")
    }

    override fun getItemBlock(): ItemBlock {
        if (ExNihiloItems.hasRegisteredItems())
            return Item.getItemFromBlock(this) as ItemBlock

        val itemBlock = ItemBlock(this)
        itemBlock.registryName = registryName
        return itemBlock
    }

    @SideOnly(Side.CLIENT)
    override fun registerModels() {
        val name = this.registryName ?: return
        ModelLoader.setCustomModelResourceLocation(itemBlock, 0, ModelResourceLocation(name, "inventory"))
    }
}
