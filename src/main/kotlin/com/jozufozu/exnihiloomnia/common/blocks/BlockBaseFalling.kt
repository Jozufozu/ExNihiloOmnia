package com.jozufozu.exnihiloomnia.common.blocks

import com.jozufozu.exnihiloomnia.ExNihilo
import com.jozufozu.exnihiloomnia.common.items.ExNihiloTabs
import com.jozufozu.exnihiloomnia.common.util.IItemBlockHolder
import com.jozufozu.exnihiloomnia.common.util.IModelRegister
import net.minecraft.block.BlockFalling
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.item.ItemBlock
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

class BlockBaseFalling @JvmOverloads constructor(registryName: ResourceLocation, materialIn: Material, soundType: SoundType = SoundType.STONE) : BlockFalling(materialIn), IItemBlockHolder, IModelRegister {

    init {

        this.setHarvestLevel("shovel", 0)
        this.soundType = soundType
        this.registryName = registryName
        this.unlocalizedName = ExNihilo.MODID + "." + registryName.resourcePath
        this.setCreativeTab(ExNihiloTabs.BLOCKS)

        if (ExNihiloBlocks.hasRegisteredBlocks())
            ExNihilo.log.warn("Tried to make a block $registryName after registering!")
    }

    override val itemBlock: ItemBlock by lazy {
        val itemBlock = ItemBlock(this)
        itemBlock.registryName = this.registryName
        itemBlock
    }

    @SideOnly(Side.CLIENT)
    override fun registerModels() {
        val name = this.registryName ?: return
        ModelLoader.setCustomModelResourceLocation(itemBlock, 0, ModelResourceLocation(name, "inventory"))
    }
}
