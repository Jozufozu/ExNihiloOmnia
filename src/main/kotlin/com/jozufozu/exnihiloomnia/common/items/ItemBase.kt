package com.jozufozu.exnihiloomnia.common.items

import com.jozufozu.exnihiloomnia.ExNihilo
import com.jozufozu.exnihiloomnia.common.util.IModelRegister
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.item.Item
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

open class ItemBase(registryName: ResourceLocation) : Item(), IModelRegister {
    init {
        this.registryName = registryName
        this.unlocalizedName = ExNihilo.MODID + "." + registryName.resourcePath
        this.creativeTab = ExNihiloTabs.ITEMS
    }

    @SideOnly(Side.CLIENT)
    override fun registerModels() {
        ModelLoader.setCustomModelResourceLocation(this, 0, ModelResourceLocation(this.registryName!!, "inventory"))
    }
}
