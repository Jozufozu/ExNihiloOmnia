package com.jozufozu.exnihiloomnia.common.items

import com.jozufozu.exnihiloomnia.ExNihilo
import com.jozufozu.exnihiloomnia.common.lib.LibItems
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.block.BlockPlanks
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

class ItemTreeSeed : ItemBase(LibItems.TREE_SEED) {
    init {
        this.setHasSubtypes(true)
    }

    override fun getSubItems(tab: CreativeTabs, items: NonNullList<ItemStack>) {
        if (this.isInCreativeTab(tab)) {
            for (enumType in BlockPlanks.EnumType.values()) {
                items.add(ItemStack(this, 1, enumType.metadata))
            }
        }
    }

    override fun getUnlocalizedName(stack: ItemStack): String {
        return super.getUnlocalizedName(stack) + "." + BlockPlanks.EnumType.byMetadata(stack.metadata).unlocalizedName
    }

    override fun onItemUse(player: EntityPlayer, worldIn: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        var pos = pos
        val state = worldIn.getBlockState(pos)
        val block = state.block

        if (!block.isReplaceable(worldIn, pos)) pos = pos.offset(facing)

        val stack = player.getHeldItem(hand)

        return if (!stack.isEmpty && player.canPlayerEdit(pos, facing, stack) && worldIn.mayPlace(Blocks.SAPLING, pos, false, facing, null)) {
            var placeState = Blocks.SAPLING.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, stack.metadata, player, hand)

            if (this.placeBlockAt(stack, player, worldIn, pos, placeState)) {
                placeState = worldIn.getBlockState(pos)
                val soundType = placeState.block.getSoundType(placeState, worldIn, pos, player)
                worldIn.playSound(player, pos, soundType.placeSound, SoundCategory.BLOCKS, (soundType.getVolume() + 1.0f) / 2.0f, soundType.getPitch() * 0.8f)
                stack.shrink(1)
            }

            EnumActionResult.SUCCESS
        } else {
            EnumActionResult.FAIL
        }
    }

    private fun placeBlockAt(stack: ItemStack, player: EntityPlayer, world: World, pos: BlockPos, newState: IBlockState): Boolean {
        if (!world.setBlockState(pos, newState, 11)) return false

        val state = world.getBlockState(pos)
        if (state.block === Blocks.SAPLING) {
            Blocks.SAPLING.onBlockPlacedBy(world, pos, state, player, stack)

            if (player is EntityPlayerMP)
                CriteriaTriggers.PLACED_BLOCK.trigger(player, pos, stack)
        }

        return true
    }

    @SideOnly(Side.CLIENT)
    override fun registerModels() {
        for (enumType in BlockPlanks.EnumType.values()) {
            ModelLoader.setCustomModelResourceLocation(this, enumType.metadata, ModelResourceLocation(ExNihilo.MODID + ":seed_" + enumType.unlocalizedName, "inventory"))
        }
    }
}
