package com.jozufozu.exnihiloomnia.common.blocks.barrel

import com.jozufozu.exnihiloomnia.common.lib.LibBlocks
import com.jozufozu.exnihiloomnia.common.util.IModelRegister
import net.minecraft.block.BlockPlanks
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemMultiTexture
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.world.World
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

class BlockBarrelWood : BlockBarrel(LibBlocks.WOODEN_BARREL, Material.WOOD, SoundType.WOOD), IModelRegister {
    companion object {
        @JvmField val VARIANT: PropertyEnum<BlockPlanks.EnumType> = BlockPlanks.VARIANT
    }

    init {
        this.defaultState = this.blockState.baseState.withProperty<BlockPlanks.EnumType, BlockPlanks.EnumType>(VARIANT, BlockPlanks.EnumType.OAK)
        this.setHardness(2.0f)
        this.setResistance(3.0f)
    }

    override fun getPickBlock(state: IBlockState, target: RayTraceResult, world: World, pos: BlockPos, player: EntityPlayer): ItemStack {
        return ItemStack(this, 1, getMetaFromState(state))
    }

    override fun getSubBlocks(itemIn: CreativeTabs, items: NonNullList<ItemStack>) {
        items.addAll(BlockPlanks.EnumType.values().map { ItemStack(this, 1, it.metadata) })
    }

    override fun createBlockState() = BlockStateContainer(this, VARIANT)

    override fun getStateFromMeta(meta: Int): IBlockState {
        return this.blockState.baseState.withProperty<BlockPlanks.EnumType, BlockPlanks.EnumType>(VARIANT, BlockPlanks.EnumType.byMetadata(meta))
    }

    override fun getMetaFromState(state: IBlockState) = state.getValue<BlockPlanks.EnumType>(VARIANT).metadata

    override val itemBlock: ItemBlock by lazy {
        ItemMultiTexture(this, this) { item -> BlockPlanks.EnumType.byMetadata(item.metadata).unlocalizedName }.also {
            it.registryName = registryName
        }
    }

    @SideOnly(Side.CLIENT)
    override fun registerModels() {
        val registryName = this.registryName ?: return
        val itemBlock = itemBlock
        for (enumType in BlockPlanks.EnumType.values()) {
            ModelLoader.setCustomModelResourceLocation(itemBlock, enumType.metadata, ModelResourceLocation(registryName, "variant=" + enumType.name))
        }
    }
}
