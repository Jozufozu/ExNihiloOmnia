package com.jozufozu.exnihiloomnia.common.registries.ingredients

import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList
import java.util.*
import java.util.function.Predicate
import kotlin.collections.ArrayList

class ExplicitWorldIngredient(val block: Block, val data: Optional<Int> = Optional.empty()) : WorldIngredient() {
    val predicates = ArrayList<Predicate<IBlockState>>()

    override val stacks: NonNullList<ItemStack> get() = if (Item.getItemFromBlock(block).hasSubtypes) {
        if (data.isPresent) NonNullList.from(ItemStack.EMPTY, ItemStack(block, 1, data.get()))
        else {
            NonNullList.create<ItemStack>().also {
                for (i in 0..16) {
                    if (test(block.getStateFromMeta(i)))
                        it.add(ItemStack(block, 1, i))
                }
            }
        }
    } else NonNullList.from(ItemStack.EMPTY, ItemStack(block, 1))

    override fun test(state: IBlockState): Boolean {
        if (state.block === this.block) {
            if (this.predicates.isEmpty()) {
                return true
            } else {
                for (predicate in predicates) {
                    if (!predicate.test(state)) {
                        return false
                    }
                }

                return true
            }
        } else {
            return false
        }
    }
}
