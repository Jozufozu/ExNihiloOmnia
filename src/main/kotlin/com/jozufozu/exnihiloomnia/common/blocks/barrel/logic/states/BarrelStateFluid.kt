package com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.states

import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.*
import com.jozufozu.exnihiloomnia.common.registries.RegistryManager
import net.minecraft.block.material.Material
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.IFluidBlock

class BarrelStateFluid : BarrelState(BarrelStates.ID_FLUID) {
    init {
        this.logic.add(FluidMixingTrigger())
        this.logic.add(FluidCraftingTrigger())
    }

    override fun canInteractWithItems(barrel: TileEntityBarrel): Boolean {
        return true
    }

    override fun draw(barrel: TileEntityBarrel, x: Double, y: Double, z: Double, partialTicks: Float) {
        super.draw(barrel, x, y, z, partialTicks)

        renderFluid(barrel, x, y, z, partialTicks)
    }

    class FluidMixingTrigger : BarrelLogic() {
        override fun onUpdate(barrel: TileEntityBarrel): Boolean {
            val world = barrel.world
            if (!world.isRemote) {
                val blockState = world.getBlockState(barrel.pos.up())

                var fluid: Fluid? = null

                val block = blockState.block

                if (block is IFluidBlock) {
                    fluid = (block as IFluidBlock).fluid
                } else if (blockState.material === Material.LAVA) {
                    fluid = FluidRegistry.LAVA
                } else if (blockState.material === Material.WATER) {
                    fluid = FluidRegistry.WATER
                }

                if (fluid == null) {
                    return false
                }

                val on = FluidStack(fluid, 1000)
                val `in` = barrel.fluid ?: return false

                val recipe = RegistryManager.getMixing(`in`, on) ?: return false

                barrel.state = BarrelStates.ITEMS

                world.playSound(null, barrel.pos, recipe.craftSound, SoundCategory.BLOCKS, 1.0f, 1.0f)
                barrel.item = recipe.output
                barrel.fluid = null

                return true
            }
            return false
        }
    }

    class FluidCraftingTrigger : BarrelLogic() {
        override fun canUseItem(barrel: TileEntityBarrel, player: EntityPlayer?, hand: EnumHand?, itemStack: ItemStack): Boolean {
            return barrel.fluid != null && barrel.fluidAmount == barrel.fluidCapacity && RegistryManager.getFluidCrafting(itemStack, barrel.fluid!!) != null
        }

        override fun onUseItem(barrel: TileEntityBarrel, player: EntityPlayer?, hand: EnumHand?, itemStack: ItemStack): EnumInteractResult {
            if (barrel.fluid != null) {
                val fluidCraftingRecipe = RegistryManager.getFluidCrafting(itemStack, barrel.fluid!!)
                if (fluidCraftingRecipe != null) {
                    barrel.world.playSound(null, barrel.pos, fluidCraftingRecipe.craftSound, SoundCategory.BLOCKS, 1.0f, 1.0f)
                    barrel.state = BarrelStates.ITEMS
                    barrel.item = fluidCraftingRecipe.result
                    barrel.fluid = null

                    return EnumInteractResult.CONSUME
                }
            }
            return EnumInteractResult.PASS
        }
    }
}
