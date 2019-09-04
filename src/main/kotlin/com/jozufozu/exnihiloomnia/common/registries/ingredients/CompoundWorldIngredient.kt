package com.jozufozu.exnihiloomnia.common.registries.ingredients

import net.minecraft.block.state.IBlockState
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList

class CompoundWorldIngredient(val elements: List<WorldIngredient>) : WorldIngredient() {
    override val stacks: NonNullList<ItemStack> get() = elements.flatMap { it.stacks }.toCollection(NonNullList.create())

    override fun test(state: IBlockState): Boolean = elements.any { it.test(state) }
}