package com.jozufozu.exnihiloomnia.common.items

import com.jozufozu.exnihiloomnia.ExNihilo
import com.jozufozu.exnihiloomnia.common.blocks.ExNihiloBlocks
import com.jozufozu.exnihiloomnia.common.items.tools.ItemCrook
import com.jozufozu.exnihiloomnia.common.items.tools.ItemHammer
import com.jozufozu.exnihiloomnia.common.items.tools.ItemMesh
import com.jozufozu.exnihiloomnia.common.lib.LibItems
import com.jozufozu.exnihiloomnia.common.util.IItemBlockHolder
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

import java.util.ArrayList

@Mod.EventBusSubscriber(modid = ExNihilo.MODID)
object ExNihiloItems {
    val modItems = ArrayList<Item>()

    val WOOD_MESH: Item = register(ItemMesh(LibItems.WOODEN_MESH, Item.ToolMaterial.WOOD))
    val SILK_MESH: Item = register(ItemMesh(LibItems.SILK_MESH, ExNihiloMaterials.SILK))
    val DIAMOND_MESH: Item = register(ItemMesh(LibItems.DIAMOND_MESH, Item.ToolMaterial.DIAMOND))
    val GOLD_MESH: Item = register(ItemMesh(LibItems.GOLD_MESH, Item.ToolMaterial.GOLD))

    val CROOK: Item = register(ItemCrook(LibItems.CROOK, Item.ToolMaterial.WOOD))
    val BONE_CROOK: Item = register(ItemCrook(LibItems.BONE_CROOK, ExNihiloMaterials.SKELETAL))

    val WOOD_HAMMER: Item = register(ItemHammer(LibItems.WOODEN_HAMMER, Item.ToolMaterial.WOOD))
    val STONE_HAMMER: Item = register(ItemHammer(LibItems.STONE_HAMMER, Item.ToolMaterial.STONE))
    val IRON_HAMMER: Item = register(ItemHammer(LibItems.IRON_HAMMER, Item.ToolMaterial.IRON))
    val GOLD_HAMMER: Item = register(ItemHammer(LibItems.GOLD_HAMMER, Item.ToolMaterial.GOLD))
    val DIAMOND_HAMMER: Item = register(ItemHammer(LibItems.DIAMOND_HAMMER, Item.ToolMaterial.DIAMOND))

    val TREE_SEED: Item = register(ItemTreeSeed())
    val STONE: Item = register(ItemStone())
    val ASTROLABE: Item = register(ItemJadeAstrolabe())
    val ASH: Item = register(ItemAsh())

    @SubscribeEvent
    @JvmStatic fun registerItems(event: RegistryEvent.Register<Item>) {
        for (item in modItems)
            event.registry.register(item)

        for (block in ExNihiloBlocks.modBlocks)
            if (block is IItemBlockHolder)
                event.registry.register((block as IItemBlockHolder).itemBlock)
    }

    private fun <T: Item> register(item: T): T = item.also { modItems.add(it) }
}