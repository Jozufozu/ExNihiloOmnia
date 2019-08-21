package com.jozufozu.exnihiloomnia.common.items

import com.jozufozu.exnihiloomnia.ExNihilo
import com.jozufozu.exnihiloomnia.common.blocks.ExNihiloBlocks
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier

object ExNihiloTabs {
    @JvmField val BLOCKS: ItemGroup = with(FabricItemGroupBuilder.create(Identifier(ExNihilo.MODID, "blocks"))) {
        icon { ItemStack(ExNihiloBlocks.ACACIA_SIEVE) }
        build()
    }

    @JvmField val BARRELS: ItemGroup = with(FabricItemGroupBuilder.create(Identifier(ExNihilo.MODID, "barrels"))) {
        icon { ItemStack(ExNihiloBlocks.OAK_WOOD_BARREL) }
        build()
    }

    @JvmField val ITEMS: ItemGroup = with(FabricItemGroupBuilder.create(Identifier(ExNihilo.MODID, "items"))) {
        icon { ItemStack(ExNihiloBlocks.DARK_OAK_SIEVE) }
        build()
    }
}
