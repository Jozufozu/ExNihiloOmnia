package com.jozufozu.exnihiloomnia.common.items

import com.google.common.collect.Sets
import com.jozufozu.exnihiloomnia.ExNihilo
import com.jozufozu.exnihiloomnia.common.util.IModelRegister
import net.minecraft.block.Block
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.item.Item
import net.minecraft.item.ItemTool
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

open class ItemBaseTool(registryName: ResourceLocation, damage: Float, attackSpeed: Float, toolMaterial: ToolMaterial, effectiveBlocksIn: Set<Block>) : ItemTool(damage, attackSpeed, toolMaterial, effectiveBlocksIn), IModelRegister {

    @JvmOverloads
    constructor(registryName: ResourceLocation, toolMaterial: ToolMaterial, effectiveBlocksIn: Set<Block> = Sets.newHashSet()) : this(registryName, 0f, 0f, toolMaterial, effectiveBlocksIn)

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
