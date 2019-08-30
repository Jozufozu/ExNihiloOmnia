package com.jozufozu.exnihiloomnia.common.registries.ingredients

import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList
import java.util.function.Predicate

class ExplicitWorldIngredient(val block: Block, val info: Info = Info.None) : WorldIngredient() {

    override val stacks: NonNullList<ItemStack> get() = if (Item.getItemFromBlock(block).hasSubtypes) {
        when (info) {
            is Info.Data -> NonNullList.from(ItemStack.EMPTY, ItemStack(block, 1, info.data))
            else -> {
                NonNullList.create<ItemStack>().also {
                    for (i in 0..16) {
                        if (test(block.getStateFromMeta(i)))
                            it.add(ItemStack(block, 1, i))
                    }
                }
            }
        }
    } else NonNullList.from(ItemStack.EMPTY, ItemStack(block, 1))

    override fun test(state: IBlockState): Boolean {
        if (state.block === this.block) {
            when (info) {
                is Info.None -> return true
                is Info.Data -> return this.block.getMetaFromState(state) == info.data
                is Info.Variants -> {
                    for (predicate in info.predicates) {
                        if (!predicate.test(state)) {
                            return false
                        }
                    }

                    return true
                }
            }
        } else {
            return false
        }
    }

    sealed class Info {
        object None : Info()
        class Data(val data: Int) : Info()
        class Variants(val predicates: ArrayList<Predicate<IBlockState>>) : Info()
    }
}
