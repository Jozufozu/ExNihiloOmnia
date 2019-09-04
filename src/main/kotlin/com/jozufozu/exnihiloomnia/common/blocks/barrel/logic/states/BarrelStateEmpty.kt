package com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.states

import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.*
import com.jozufozu.exnihiloomnia.common.registries.RegistryManager
import com.jozufozu.exnihiloomnia.common.util.Color
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumHand
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fluids.FluidStack

class BarrelStateEmpty : BarrelState(BarrelStates.ID_EMPTY) {
    init {
        this.logic.add(FluidFillTrigger())
        this.logic.add(CompostTrigger())
        this.logic.add(RainTrigger())
    }

    override fun activate(barrel: TileEntityBarrel) {
        barrel.item = ItemStack.EMPTY
        barrel.fluid = null
        barrel.color = Color.WHITE
        barrel.compostAmount = 0
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
                barrel.compostAmount = compost.volume
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

    class RainTrigger : BarrelLogic() {
        override fun onUpdate(barrel: TileEntityBarrel): Boolean {
            val world = barrel.world

            if (!world.isRemote) {
                if (world.isRainingAt(barrel.pos.up())) {
                    barrel.fluid = FluidStack(FluidRegistry.WATER, 1)
                    barrel.state = BarrelStates.FLUID
                }
            }

            return false
        }
    }
}
