package com.jozufozu.exnihiloomnia.common.items

import com.jozufozu.exnihiloomnia.ExNihilo
import com.jozufozu.exnihiloomnia.common.blocks.ExNihiloBlocks
import com.jozufozu.exnihiloomnia.common.items.tools.ItemCrook
import com.jozufozu.exnihiloomnia.common.items.tools.ItemHammer
import com.jozufozu.exnihiloomnia.common.lib.LibItems
import com.jozufozu.exnihiloomnia.common.registries.RegistryManager
import com.jozufozu.exnihiloomnia.common.util.IItemBlockHolder
import net.minecraft.client.resources.I18n
import net.minecraft.item.Item
import net.minecraft.item.ItemTier
import net.minecraft.util.text.TranslationTextComponent
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.*

@Mod.EventBusSubscriber(modid = ExNihilo.MODID)
object ExNihiloItems {
    val modItems = ArrayList<Item>()

    val WOOD_MESH: Item = register(ItemBaseTool(LibItems.WOODEN_MESH, ItemTier.WOOD))
    val SILK_MESH: Item = register(ItemBaseTool(LibItems.SILK_MESH, ExNihiloMaterials.SILK))
    val DIAMOND_MESH: Item = register(ItemBaseTool(LibItems.DIAMOND_MESH, ItemTier.DIAMOND))
    val GOLD_MESH: Item = register(ItemBaseTool(LibItems.GOLD_MESH, ItemTier.GOLD))

    val CROOK: Item = register(ItemCrook(LibItems.WOODEN_CROOK, ItemTier.WOOD))
    val BONE_CROOK: Item = register(ItemCrook(LibItems.BONE_CROOK, ExNihiloMaterials.BONE))

    val WOOD_HAMMER: Item = register(ItemHammer(LibItems.WOODEN_HAMMER, ItemTier.WOOD))
    val STONE_HAMMER: Item = register(ItemHammer(LibItems.STONE_HAMMER, ItemTier.STONE))
    val IRON_HAMMER: Item = register(ItemHammer(LibItems.IRON_HAMMER, ItemTier.IRON))
    val GOLD_HAMMER: Item = register(ItemHammer(LibItems.GOLD_HAMMER, ItemTier.GOLD))
    val DIAMOND_HAMMER: Item = register(ItemHammer(LibItems.DIAMOND_HAMMER, ItemTier.DIAMOND))

    val TREE_SEED: Item = register(ItemTreeSeed())
    val STONE: Item = register(ItemStone())
    val ASTROLABE: Item = register(ItemJadeAstrolabe())
    val ASH: Item = register(ItemAsh())
    val SILKWORM: Item = register(ItemSilkWorm())

    val PORCELAIN_CLAY: Item = register(ItemBase(LibItems.PORCELAIN_CLAY))

    @JvmStatic fun registerItems(event: RegistryEvent.Register<Item>) {
        for (item in modItems)
            event.registry.register(item)

        for (block in ExNihiloBlocks.modBlocks)
            if (block is IItemBlockHolder)
                event.registry.register((block as IItemBlockHolder).itemBlock)
    }

    @JvmStatic fun addMeshTooltip(event: ItemTooltipEvent) {
        val itemStack = event.itemStack

        val toolTip = event.toolTip

        if (RegistryManager.isMesh(itemStack)) {

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