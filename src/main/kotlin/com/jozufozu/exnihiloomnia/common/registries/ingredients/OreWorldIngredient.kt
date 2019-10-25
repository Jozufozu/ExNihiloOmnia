package com.jozufozu.exnihiloomnia.common.registries.ingredients

import net.minecraft.block.Block
import net.minecraft.block.state.BlockState
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList
import net.minecraftforge.oredict.OreDictionary

class OreWorldIngredient(oreDictName: String) : WorldIngredient() {
    val matchingBlocks = HashSet<Block>()
    override val stacks: NonNullList<ItemStack> = NonNullList.create()

    init {
        for (ore in OreDictionary.getOres(oreDictName)) {
            val block = Block.getBlockFromItem(ore.item)

            if (block != Blocks.AIR) {
                stacks.add(ore)
                matchingBlocks.add(block)
            }
        }
    }

    override fun test(t: BlockState): Boolean = t.block in matchingBlocks
}