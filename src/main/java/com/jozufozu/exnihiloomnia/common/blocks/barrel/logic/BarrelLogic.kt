package com.jozufozu.exnihiloomnia.common.blocks.barrel.logic

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.world.World

abstract class BarrelLogic {
    /**
     * Called when a barrel state is set
     */
    open fun onActivate(barrel: TileEntityBarrel, world: World, previousState: BarrelState) {

    }

    /**
     * @param barrel the barrel being ticked
     * @return the state that the barrel should be in after this tick or null if the state shouldn't change
     */
    open fun onUpdate(barrel: TileEntityBarrel, world: World): Boolean {
        return false
    }

    /**
     * @param barrel    the barrel being tested
     * @param player
     * @param hand
     * @param itemStack the item being tested
     * @return true if item can be used on barrel
     */
    open fun canUseItem(barrel: TileEntityBarrel, world: World, itemStack: ItemStack, player: PlayerEntity?, hand: Hand?): Boolean {
        return false
    }

    /**
     * This is called BEFORE an item is inserted into the barrel
     * @param barrel    the barrel
     * @param player    can be null if the item is inserted by pipes!
     * @param hand      the hand holding the item
     * @param itemStack the item to be checked
     * @return true if the state changed
     */
    open fun onUseItem(barrel: TileEntityBarrel, world: World, itemStack: ItemStack, player: PlayerEntity?, hand: Hand?): InteractResult {
        return InteractResult.PASS
    }

//    /**
//     * @param barrel     the barrel being tested
//     * @param fluidStack the fluid being tested
//     * @return true if the fluid can be put in the barrel
//     */
//    open fun canFillFluid(barrel: TileEntityBarrel, world: World, fluidStack: FluidStack): Boolean {
//        return false
//    }
//
//    /**
//     * Preforms additional actions when a barrel is filled with fluid
//     * Only called if [BarrelState.canInteractWithFluids] returns true
//     * @return true if the state changed. Will prevent the tank from being filled
//     */
//    open fun onFillFluid(barrel: TileEntityBarrel, world: World, fluidStack: FluidStack): InteractResult {
//        return InteractResult.PASS
//    }
}
