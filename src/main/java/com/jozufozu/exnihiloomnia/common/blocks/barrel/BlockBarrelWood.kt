package com.jozufozu.exnihiloomnia.common.blocks.barrel

import com.jozufozu.exnihiloomnia.common.items.ExNihiloItems
import com.jozufozu.exnihiloomnia.common.lib.LibBlocks
import com.jozufozu.exnihiloomnia.common.util.IModelRegister
import net.minecraft.block.*
import net.minecraft.block.BlockPlanks.EnumType
import java.util.*
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockFaceShape
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.init.SoundEvents
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemMultiTexture
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraft.world.WorldServer
import net.minecraft.world.storage.loot.LootContext
import net.minecraft.world.storage.loot.LootTableList
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fluids.FluidUtil
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.ItemHandlerHelper

class BlockBarrelWood : BlockBarrel(LibBlocks.WOODEN_BARREL, Material.WOOD, SoundType.WOOD), IModelRegister {
    companion object {
        @JvmField val VARIANT: PropertyEnum<BlockPlanks.EnumType> = BlockPlanks.VARIANT
    }

    init {
        this.defaultState = this.blockState.baseState.withProperty<BlockPlanks.EnumType, BlockPlanks.EnumType>(VARIANT, EnumType.OAK)
        this.setHardness(2.0f)
        this.setResistance(3.0f)
    }

    override fun getPickBlock(state: IBlockState, target: RayTraceResult, world: World, pos: BlockPos, player: EntityPlayer): ItemStack {
        return ItemStack(this, 1, getMetaFromState(state))
    }

    override fun getSubBlocks(itemIn: CreativeTabs, items: NonNullList<ItemStack>) {
        items.addAll(EnumType.values().map { ItemStack(this, 1, it.metadata) })
    }

    override fun createBlockState() = BlockStateContainer(this, VARIANT)

    override fun getStateFromMeta(meta: Int): IBlockState {
        return this.blockState.baseState.withProperty<BlockPlanks.EnumType, BlockPlanks.EnumType>(VARIANT, EnumType.byMetadata(meta))
    }

    override fun getMetaFromState(state: IBlockState) = state.getValue<EnumType>(VARIANT).metadata

    override fun getItemBlock(): ItemBlock {
        if (ExNihiloItems.hasRegisteredItems())
            return Item.getItemFromBlock(this) as ItemBlock

        val out = ItemMultiTexture(this, this) { item -> EnumType.byMetadata(item.metadata).unlocalizedName }
        out.registryName = registryName!!
        return out
    }

    @SideOnly(Side.CLIENT)
    override fun registerModels() {
        val registryName = this.registryName ?: return
        val itemBlock = itemBlock
        for (enumType in EnumType.values()) {
            ModelLoader.setCustomModelResourceLocation(itemBlock, enumType.metadata, ModelResourceLocation(registryName, "variant=" + enumType.name))
        }
    }
}
