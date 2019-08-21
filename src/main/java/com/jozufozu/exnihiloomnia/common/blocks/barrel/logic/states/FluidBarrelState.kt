package com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.states

//import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.*
//import com.jozufozu.exnihiloomnia.common.registries.RegistryManager
//import net.minecraft.block.FlowingFluidBlock
//import net.minecraft.block.material.Material
//import net.minecraft.block.state.IBlockState
//import net.minecraft.entity.player.EntityPlayer
//import net.minecraft.entity.player.PlayerEntity
//import net.minecraft.fluid.Fluids
//import net.minecraft.item.ItemStack
//import net.minecraft.util.EnumHand
//import net.minecraft.util.Hand
//import net.minecraft.util.SoundCategory
//import net.minecraft.world.World
//import net.minecraftforge.fluids.Fluid
//import net.minecraftforge.fluids.FluidRegistry
//import net.minecraftforge.fluids.FluidStack
//import net.minecraftforge.fluids.IFluidBlock
//
//class FluidBarrelState : BarrelState(BarrelStates.ID_FLUID) {
//    init {
//        this.logic.add(FluidMixingTrigger())
//        this.logic.add(FluidCraftingTrigger())
//    }
//
//    override fun canInteractWithItems(barrel: BarrelTileEntity): Boolean {
//        return true
//    }
//
//    override fun draw(barrel: BarrelTileEntity, x: Double, y: Double, z: Double, partialTicks: Float) {
//        super.draw(barrel, x, y, z, partialTicks)
//
//        renderFluid(barrel, x, y, z, partialTicks)
//    }
//
//    class FluidMixingTrigger : BarrelLogic() {
//        override fun onUpdate(barrel: BarrelTileEntity, world: World): Boolean {
//            barrel.world?.let { world ->
//                if (!world.isRemote) {
//                    val blockState = world.getBlockState(barrel.pos.up())
//
//                    val block = blockState.block
//
//                    val fluid: Fluid = when {
//                        block is IFluidBlock -> (block as IFluidBlock).fluid
//                        blockState.material == Material.LAVA -> Fluids.LAVA
//                        blockState.material == Material.WATER -> FluidRegistry.WATER
//                        else -> return false
//                    }
//
//                    val fluidAbove = FluidStack(fluid, 1000)
//                    val fluidBarrel = barrel.fluid ?: return null
//
//                    val recipe = RegistryManager.getMixing(fluidBarrel, fluidAbove) ?: return null
//
//                    world.playSound(null, barrel.pos, recipe.craftSound, SoundCategory.BLOCKS, 1.0f, 1.0f)
//                    barrel.item = recipe.output
//                    barrel.fluid = null
//
//                    barrel.state = BarrelStates.ITEMS
//
//                    return true
//                }
//            }
//            return false
//        }
//    }
//
//    class FluidCraftingTrigger : BarrelLogic() {
//        override fun canUseItem(barrel: BarrelTileEntity, world: World, itemStack: ItemStack, player: PlayerEntity?, hand: Hand?): Boolean {
//            return barrel.fluid != null && barrel.fluidAmount == barrel.fluidCapacity && RegistryManager.getFluidCrafting(itemStack, barrel.fluid) != null
//        }
//
//        override fun onUseItem(barrel: BarrelTileEntity, world: World, itemStack: ItemStack, player: PlayerEntity?, hand: Hand?): InteractResult {
//            if (barrel.fluid != null) {
//                RegistryManager.getFluidCrafting(itemStack, barrel.fluid)?.let {
//                    barrel.world?.playSound(null, barrel.pos, it.craftSound, SoundCategory.BLOCKS, 1.0f, 1.0f)
//                    barrel.state = BarrelStates.ITEMS
//                    barrel.item = it.result
//                    barrel.fluid = null
//
//                    return InteractResult.CONSUME
//                }
//            }
//            return InteractResult.PASS
//        }
//    }
//}
