package com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.states

import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.*
import com.jozufozu.exnihiloomnia.common.registries.RegistryManager
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.world.World

class EmptyBarrelState : BarrelState(BarrelStates.ID_EMPTY) {
    init {
        //this.logic.add(FluidFillTrigger())
        this.logic.add(CompostTrigger())
    }

    override fun activate(barrel: TileEntityBarrel, world: World, previousState: BarrelState) {
        barrel.item = ItemStack.EMPTY
        //barrel.fluid = null
        barrel.color = null
        barrel.compostAmount = 0

        super.activate(barrel, world, previousState)
    }

    class CompostTrigger : BarrelLogic() {
        override fun canUseItem(barrel: TileEntityBarrel, world: World, itemStack: ItemStack, player: PlayerEntity?, hand: Hand?): Boolean {
            return RegistryManager.getCompost(itemStack) != null
        }

        override fun onUseItem(barrel: TileEntityBarrel, world: World, itemStack: ItemStack, player: PlayerEntity?, hand: Hand?): InteractResult {
            val compost = RegistryManager.getCompost(itemStack) ?: return InteractResult.PASS

            if (!world.isRemote) {
                barrel.state = BarrelStates.COLLECT_COMPOST

                barrel.item = compost.output

                barrel.color = compost.color
                barrel.compostAmount = compost.amount
            }
            return InteractResult.CONSUME
        }
    }

//    class FluidFillTrigger : BarrelLogic() {
//        override fun canFillFluid(barrel: TileEntityBarrel, world: World, fluidStack: FluidStack) = true
//
//        override fun onFillFluid(barrel: TileEntityBarrel, world: World, fluidStack: FluidStack): InteractResult {
//            if (!world.isRemote) {
//                barrel.state = BarrelStates.FLUID
//            }
//            return InteractResult.INSERT
//        }
//    }
}
