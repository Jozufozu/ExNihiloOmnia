package com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.states

import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.*
import com.jozufozu.exnihiloomnia.common.registries.RegistryManager
import net.minecraft.block.material.Material
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.IFluidBlock

class BarrelStateFluid : BarrelState(BarrelStates.ID_FLUID) {
    init {
        this.logic.add(FluidMixingTrigger())
        this.logic.add(FluidCraftingTrigger())
        this.logic.add(WoodenBarrelBurn())
    }

    override fun canInteractWithItems(barrel: TileEntityBarrel): Boolean {
        return true
    }

    override fun draw(barrel: TileEntityBarrel, x: Double, y: Double, z: Double, partialTicks: Float) {
        super.draw(barrel, x, y, z, partialTicks)

        renderFluid(barrel, x, y, z, partialTicks)
    }

    class WoodenBarrelBurn : BarrelLogic() {
        override fun onUpdate(barrel: TileEntityBarrel): Boolean {
            if (barrel.world.getBlockState(barrel.pos).material.canBurn) {
                barrel.fluid?.fluid?.takeIf { it.temperature > TileEntityBarrel.burnTemperature }?.let {
                    if (barrel.burnTimer++ > TileEntityBarrel.burnTime) {
                        val block = it.block
                        barrel.world.setBlockState(barrel.pos, block.defaultState)
                        barrel.world.scheduleBlockUpdate(barrel.pos, block, 1, 3)
                        barrel.world.playSound(null, barrel.pos, SoundEvents.ITEM_BUCKET_EMPTY_LAVA, SoundCategory.BLOCKS, 0.7f, 0.8f)
                    }
                } ?: barrel.resetBurnTimer()
            }
            return false
        }
    }

    class FluidMixingTrigger : BarrelLogic() {
        override fun onUpdate(barrel: TileEntityBarrel): Boolean {
            val world = barrel.world
            if (!world.isRemote) {
                val blockState = world.getBlockState(barrel.pos.up())

                val block = blockState.block

                val fluid = when {
                    block is IFluidBlock -> block.fluid
                    blockState.material === Material.LAVA -> FluidRegistry.LAVA
                    blockState.material === Material.WATER -> FluidRegistry.WATER
                    else -> return false
                }

                val onBarrel = FluidStack(fluid, 1000)
                val inBarrel = barrel.fluid ?: return false

                val recipe = RegistryManager.getMixing(inBarrel, onBarrel) ?: return false

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
            return barrel.fluid != null && barrel.fluidAmount == TileEntityBarrel.fluidCapacity && RegistryManager.getFluidCrafting(itemStack, barrel.fluid!!) != null
        }

        override fun onUseItem(barrel: TileEntityBarrel, player: EntityPlayer?, hand: EnumHand?, itemStack: ItemStack): EnumInteractResult {
            if (barrel.fluid != null) {
                val fluidCraftingRecipe = RegistryManager.getFluidCrafting(itemStack, barrel.fluid!!)
                if (fluidCraftingRecipe != null) {
                    barrel.world.playSound(null, barrel.pos, fluidCraftingRecipe.craftSound, SoundCategory.BLOCKS, 1.0f, 1.0f)
                    barrel.state = BarrelStates.ITEMS
                    barrel.item = fluidCraftingRecipe.output
                    barrel.fluid = null

                    return EnumInteractResult.CONSUME
                }
            }
            return EnumInteractResult.PASS
        }
    }
}
