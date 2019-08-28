package com.jozufozu.exnihiloomnia.common.items

import net.minecraft.init.Items
import net.minecraft.item.Item.ToolMaterial
import net.minecraft.item.ItemStack
import net.minecraftforge.common.util.EnumHelper
import net.minecraftforge.fml.common.registry.GameRegistry

object ExNihiloMaterials {
    val SILK: ToolMaterial = EnumHelper.addToolMaterial("ENO_SILK", 0, 128, 10.0f, 0f, 18) ?: throw Exception("Error adding SILK to EnumToolMaterials")
    val SKELETAL: ToolMaterial = EnumHelper.addToolMaterial("ENO_BONE", 1, 200, 8.0f, 2f, 25) ?: throw Exception("Error adding BONE to EnumToolMaterials")

    private val STRING: ItemStack by lazy { ItemStack(Items.STRING) }
    private val BONE: ItemStack by lazy { ItemStack(Items.BONE) }

    fun preInit() {}

    fun init() {
        SILK.setRepairItem(STRING)
        SKELETAL.setRepairItem(BONE)
    }
}
