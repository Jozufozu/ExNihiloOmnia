package com.jozufozu.exnihiloomnia.common.items

import com.jozufozu.exnihiloomnia.ExNihilo
import com.jozufozu.exnihiloomnia.common.blocks.ExNihiloBlocks
import com.jozufozu.exnihiloomnia.common.items.tools.ItemCrook
import com.jozufozu.exnihiloomnia.common.items.tools.ItemHammer
import com.jozufozu.exnihiloomnia.common.lib.ItemsLib
import com.jozufozu.exnihiloomnia.common.registries.RegistryManager
import net.minecraft.block.Blocks
import net.minecraft.client.resources.I18n
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemTier
import net.minecraft.util.text.TranslationTextComponent
import net.minecraftforge.common.ToolType
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.fml.common.Mod
import java.util.*

@Mod.EventBusSubscriber(modid = ExNihilo.MODID)
object ExNihiloItems {
    val modItems = ArrayList<Item>()

    val MESH_TOOL = ToolType.get("mesh")
    val CROOK_TOOL = ToolType.get("crook")

    val WOOD_MESH: Item = register(ItemBaseTool(ItemsLib.WOODEN_MESH, ItemTier.WOOD, Item.Properties().addToolType(MESH_TOOL, 0)))
    val SILK_MESH: Item = register(ItemBaseTool(ItemsLib.SILK_MESH, ExNihiloMaterials.SILK, Item.Properties().addToolType(MESH_TOOL, 1)))
    val DIAMOND_MESH: Item = register(ItemBaseTool(ItemsLib.DIAMOND_MESH, ItemTier.DIAMOND, Item.Properties().addToolType(MESH_TOOL, 3)))
    val GOLD_MESH: Item = register(ItemBaseTool(ItemsLib.GOLD_MESH, ItemTier.GOLD, Item.Properties().addToolType(MESH_TOOL, 2)))

    val CROOK: Item = register(ItemCrook(ItemsLib.WOODEN_CROOK, ItemTier.WOOD, Item.Properties().addToolType(CROOK_TOOL, 0)))
    val BONE_CROOK: Item = register(ItemCrook(ItemsLib.BONE_CROOK, ExNihiloMaterials.BONE, Item.Properties().addToolType(CROOK_TOOL, 1)))

    val WOOD_HAMMER: Item = register(ItemHammer(ItemsLib.WOODEN_HAMMER, ItemTier.WOOD))
    val STONE_HAMMER: Item = register(ItemHammer(ItemsLib.STONE_HAMMER, ItemTier.STONE))
    val IRON_HAMMER: Item = register(ItemHammer(ItemsLib.IRON_HAMMER, ItemTier.IRON))
    val GOLD_HAMMER: Item = register(ItemHammer(ItemsLib.GOLD_HAMMER, ItemTier.GOLD))
    val DIAMOND_HAMMER: Item = register(ItemHammer(ItemsLib.DIAMOND_HAMMER, ItemTier.DIAMOND))

    val OAK_SEED: Item = register(BlockItem(Blocks.OAK_SAPLING, Item.Properties().group(ExNihiloTabs.ITEMS)).also { it.registryName = ItemsLib.OAK_SEED })
    val SPRUCE_SEED: Item = register(BlockItem(Blocks.SPRUCE_SAPLING, Item.Properties().group(ExNihiloTabs.ITEMS)).also { it.registryName = ItemsLib.SPRUCE_SEED })
    val BIRCH_SEED: Item = register(BlockItem(Blocks.BIRCH_SAPLING, Item.Properties().group(ExNihiloTabs.ITEMS)).also { it.registryName = ItemsLib.BIRCH_SEED })
    val JUNGLE_SEED: Item = register(BlockItem(Blocks.JUNGLE_SAPLING, Item.Properties().group(ExNihiloTabs.ITEMS)).also { it.registryName = ItemsLib.JUNGLE_SEED })
    val ACACIA_SEED: Item = register(BlockItem(Blocks.ACACIA_SAPLING, Item.Properties().group(ExNihiloTabs.ITEMS)).also { it.registryName = ItemsLib.ACACIA_SEED })
    val DARK_OAK_SEED: Item = register(BlockItem(Blocks.DARK_OAK_SAPLING, Item.Properties().group(ExNihiloTabs.ITEMS)).also { it.registryName = ItemsLib.DARK_OAK_SEED })

    val STONE: Item = register(SmallStoneItem())
    val ASTROLABE: Item = register(ItemJadeAstrolabe())
    val SILKWORM: Item = register(ItemSilkWorm())

    val PORCELAIN_CLAY: Item = register(ModItem(ItemsLib.PORCELAIN_CLAY))

    @JvmStatic fun registerItems(event: RegistryEvent.Register<Item>) {
        for (item in modItems)
            event.registry.register(item)

        for (block in ExNihiloBlocks.modBlocks)
            event.registry.register(block.asItem())
    }

    @JvmStatic fun addMeshTooltip(event: ItemTooltipEvent) {
        val itemStack = event.itemStack

        if (RegistryManager.isMesh(itemStack)) {
            val toolTip = event.toolTip

            toolTip.add(null)
            val multipliers = RegistryManager.getMultipliers(itemStack)

            toolTip.add(TranslationTextComponent("exnihiloomnia.drops.effective_list"))
            for ((type, modifier) in multipliers) {
                val localizedTypeName = "exnihiloomnia.drops.type.$type".let { if (I18n.hasKey(it)) I18n.format(it) else it }

                toolTip.add(TranslationTextComponent("exnihiloomnia.drops.effective_entry", localizedTypeName, modifier))
            }
            toolTip.add(null)
        }
    }


    private fun <T: Item> register(item: T): T = item.also { modItems.add(it) }
}