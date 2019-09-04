package com.jozufozu.exnihiloomnia.common.blocks.barrel.logic

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumHand
import net.minecraftforge.fluids.FluidStack

abstract class BarrelLogic {
    /**
     * Called when a barrel state is set
     */
    open fun onActivate(barrel: TileEntityBarrel) {

    }

    /**
     * @param barrel the barrel being ticked
     * @return true if the barrel state changed
     */
    open fun onUpdate(barrel: TileEntityBarrel): Boolean {
        return false
    }

    /**
     * @param barrel    the barrel being tested
     * @param itemStack the item being tested
     * @return true if item can be used on barrel
     */
    open fun canUseItem(barrel: TileEntityBarrel, player: EntityPlayer?, hand: EnumHand?, itemStack: ItemStack): Boolean {
        return false
    }

    /**
     * This is called BEFORE an item is inserted into the barrel
     * @param player    can be null if the item is inserted by pipes!
     * @param hand      the hand holding the item
     * @param barrel    the barrel
     * @param itemStack the item to be checked
     * @return true if the state changed
     */
    open fun onUseItem(barrel: TileEntityBarrel, player: EntityPlayer?, hand: EnumHand?, itemStack: ItemStack): EnumInteractResult {
        return EnumInteractResult.PASS
    }

    /**
     * @param barrel     the barrel being tested
     * @param fluidStack the fluid being tested
     * @return true if the fluid can be put in the barrel
     */
    open fun canFillFluid(barrel: TileEntityBarrel, fluidStack: FluidStack): Boolean {
        return false
    }

    /**
     * Preforms additional actions when a barrel is filled with fluid
     * Only called if [BarrelState.canInteractWithFluids] returns true
     * @return true if the state changed. Will prevent the tank from being filled
     */
    open fun onFillFluid(barrel: TileEntityBarrel, fluidStack: FluidStack): EnumInteractResult {
        return EnumInteractResult.PASS
    }
}
