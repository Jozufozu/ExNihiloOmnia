package com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.states

import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.*
import com.jozufozu.exnihiloomnia.common.registries.RegistryManager
import com.jozufozu.exnihiloomnia.common.registries.recipes.FermentingRecipe
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.IFluidBlock
import java.util.*

object BarrelStateFluid : BarrelState(BarrelStates.ID_FLUID) {
    init {
        logic.add(FluidMixingTrigger)
        logic.add(FluidCraftingTrigger)
        logic.add(SummonTrigger)
        logic.add(WoodenBarrelBurn)
        logic.add(RainLogic)
        logic.add(FermentingTrigger)
        logic.add(MossLogic)
    }

    override fun getLightValue(barrel: TileEntityBarrel, state: IBlockState, world: IBlockAccess, pos: BlockPos): Int {
        return barrel.fluid?.fluid?.getLuminosity(barrel.fluid) ?: 0
    }

    override fun draw(barrel: TileEntityBarrel, x: Double, y: Double, z: Double, partialTicks: Float) {
        super.draw(barrel, x, y, z, partialTicks)

        drawFluid(barrel, x, y, z, partialTicks)
    }

    override fun randomDisplayTick(barrel: TileEntityBarrel, stateIn: IBlockState, worldIn: World, pos: BlockPos, rand: Random) {
        barrel.fluid?.fluid?.block?.let { block ->
            block.randomDisplayTick(block.defaultState, worldIn, pos, rand)
        }
    }

    object MossLogic : BarrelLogic() {
        override fun onUpdate(barrel: TileEntityBarrel): Boolean {
            val world = barrel.world
            if (!world.isRemote && barrel.material === Material.WOOD) barrel.fluid?.let { fluid ->
                val leaks = RegistryManager.getLeaking(fluid)
                val chanceMultiplier = (barrel.fluid?.amount?.toFloat()?.div(TileEntityBarrel.fluidCapacity) ?: 0f)
                val barrelPos = barrel.pos

                for (pos in BlockPos.MutableBlockPos.getAllInBoxMutable(barrelPos.add(-1, -1, -1), barrelPos.add(1, -1, 1))) {
                    for (leak in leaks.asSequence().filter { it.matches(world.getBlockState(pos)) }) {
                        val raining = world.isRainingAt(pos.up())
                        if (world.rand.nextFloat() < (leak.chance * chanceMultiplier * if (raining) 3 else 1)) {
                            world.setBlockState(pos, leak.result)
                            break
                        }
                    }
                }
            }

            return false
        }
    }

    object RainLogic : BarrelLogic() {
        override fun onUpdate(barrel: TileEntityBarrel): Boolean {
            val world = barrel.world
            if (!world.isRemote && barrel.fluid?.fluid === FluidRegistry.WATER && world.isRainingAt(barrel.pos.up())) {
                barrel.fluid?.let {
                    if (it.amount < TileEntityBarrel.fluidCapacity) it.amount++
                    barrel.fluid = it
                }
            }

            return false
        }
    }

    object FermentingTrigger : BarrelLogic() {
        override fun onUpdate(barrel: TileEntityBarrel): Boolean {
            val world = barrel.world
            if (!world.isRemote && barrel.material === Material.WOOD) {
                barrel.fluid?.let { fluid: FluidStack ->
                    if (fluid.amount == TileEntityBarrel.fluidCapacity) {
                        val possibleFermentations = RegistryManager.getFermenting(fluid)

                        if (possibleFermentations.isEmpty()) return false

                        val counts = hashMapOf<FermentingRecipe, Int>()
                        var total = 0
                        val barrelPos = barrel.pos
                        val box = BlockPos.MutableBlockPos.getAllInBoxMutable(barrelPos.add(-1, -1, -1), barrelPos.add(1, -1, 1))

                        for (pos in box) {
                            for (recipe in possibleFermentations) {
                                if (recipe.matches(world.getBlockState(pos))) {
                                    counts[recipe] = counts.getOrDefault(recipe, 0) + 1
                                    total++
                                }
                            }
                        }

                        if (total == 0) return false

                        val threshold = world.rand.nextFloat()

                        var weight = 0f
                        for ((recipe, c) in counts) {
                            weight += c.toFloat() / total
                            if (weight >= threshold) {
                                if (world.rand.nextFloat() < recipe.chance) {
                                    barrel.state = recipe.barrelState
                                    return true
                                }
                                return false
                            }
                        }
                    }
                }
            }

            return false
        }
    }

    object WoodenBarrelBurn : BarrelLogic() {
        override fun onUpdate(barrel: TileEntityBarrel): Boolean {
            if (barrel.material.canBurn) {
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

    object FluidMixingTrigger : BarrelLogic() {
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

    object FluidCraftingTrigger : BarrelLogic() {
        override fun canUseItem(barrel: TileEntityBarrel, player: EntityPlayer?, hand: EnumHand?, itemStack: ItemStack): Boolean {
            return barrel.fluid != null && barrel.fluidAmount == TileEntityBarrel.fluidCapacity && RegistryManager.getFluidCrafting(itemStack, barrel.fluid!!) != null
        }

        override fun onUseItem(barrel: TileEntityBarrel, player: EntityPlayer?, hand: EnumHand?, itemStack: ItemStack): EnumInteractResult {
            barrel.fluid?.let { fluid ->
                RegistryManager.getFluidCrafting(itemStack, fluid)?.let {
                    barrel.world.playSound(null, barrel.pos, it.craftSound, SoundCategory.BLOCKS, 1.0f, 1.0f)
                    barrel.state = BarrelStates.ITEMS
                    barrel.item = it.output
                    barrel.fluid = null

                    return EnumInteractResult.CONSUME
                }
            }
            return EnumInteractResult.PASS
        }
    }

    object SummonTrigger : BarrelLogic() {
        override fun canUseItem(barrel: TileEntityBarrel, player: EntityPlayer?, hand: EnumHand?, itemStack: ItemStack): Boolean {
            return barrel.fluid != null && barrel.fluidAmount == TileEntityBarrel.fluidCapacity && RegistryManager.getSummoning(itemStack, barrel.fluid!!) != null
        }

        override fun onUseItem(barrel: TileEntityBarrel, player: EntityPlayer?, hand: EnumHand?, itemStack: ItemStack): EnumInteractResult {
            barrel.fluid?.let { fluid ->
                RegistryManager.getSummoning(itemStack, fluid)?.let {
                    barrel.state = it.barrelState

                    return EnumInteractResult.CONSUME
                }
            }
            return EnumInteractResult.PASS
        }
    }
}
