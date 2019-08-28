package com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.states

import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.*
import com.jozufozu.exnihiloomnia.common.registries.RegistryManager
import com.jozufozu.exnihiloomnia.common.registries.recipes.CompostRecipe
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumHand
import net.minecraftforge.fluids.FluidStack

class BarrelStateEmpty : BarrelState(BarrelStates.ID_EMPTY) {
    init {
        this.logic.add(FluidFillTrigger())
        this.logic.add(CompostTrigger())
    }

    override fun activate(barrel: TileEntityBarrel, previousState: BarrelState?) {
        barrel.item = ItemStack.EMPTY
        barrel.fluid = null
        barrel.color = null
        barrel.compostAmount = 0

        super.activate(barrel, previousState)
    }

    class CompostTrigger : BarrelLogic() {
        override fun canUseItem(barrel: TileEntityBarrel, player: EntityPlayer?, hand: EnumHand?, itemStack: ItemStack): Boolean {
            return RegistryManager.getCompost(itemStack) != null
        }

        override fun onUseItem(barrel: TileEntityBarrel, player: EntityPlayer?, hand: EnumHand?, itemStack: ItemStack): EnumInteractResult {
            val compost = RegistryManager.getCompost(itemStack) ?: return EnumInteractResult.PASS

            if (!barrel.world.isRemote) {
                barrel.state = BarrelStates.COMPOST_COLLECT

                barrel.item = compost.output

                barrel.color = compost.color
                barrel.compostAmount = compost.amount
            }
            return EnumInteractResult.CONSUME
        }
    }

    class FluidFillTrigger : BarrelLogic() {
        override fun canFillFluid(barrel: TileEntityBarrel, fluidStack: FluidStack): Boolean {
            return true
        }

        override fun onFillFluid(barrel: TileEntityBarrel, fluidStack: FluidStack): EnumInteractResult {
            if (!barrel.world.isRemote) {
                barrel.state = BarrelStates.FLUID
            }
            return EnumInteractResult.INSERT
        }
    }
}
