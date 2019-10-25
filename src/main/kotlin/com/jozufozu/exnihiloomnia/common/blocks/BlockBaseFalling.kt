package com.jozufozu.exnihiloomnia.common.blocks

import com.jozufozu.exnihiloomnia.ExNihilo
import com.jozufozu.exnihiloomnia.common.items.ExNihiloTabs
import com.jozufozu.exnihiloomnia.common.util.IItemBlockHolder
import com.jozufozu.exnihiloomnia.common.util.IModelRegister
import net.minecraft.block.BlockFalling
import net.minecraft.block.FallingBlock
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.item.ItemBlock
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

open class BlockBaseFalling(registryName: ResourceLocation, materialIn: Material, soundType: SoundType = SoundType.STONE) : FallingBlock(materialIn), IItemBlockHolder, IModelRegister {

    init {

        this.setHarvestLevel("shovel", 0)
        this.soundType = soundType
        this.registryName = registryName
        this.unlocalizedName = ExNihilo.MODID + "." + registryName.resourcePath
        this.setCreativeTab(ExNihiloTabs.ITEMS)
    }

    override val itemBlock: ItemBlock by lazy { ItemBlock(this).also { it.registryName = registryName } }

    @SideOnly(Side.CLIENT)
    override fun registerModels() {
        val name = this.registryName ?: return
        ModelLoader.setCustomModelResourceLocation(itemBlock, 0, ModelResourceLocation(name, "inventory"))
    }
}
