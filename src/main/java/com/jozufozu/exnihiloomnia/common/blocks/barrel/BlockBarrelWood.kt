package com.jozufozu.exnihiloomnia.common.blocks.barrel

import com.jozufozu.exnihiloomnia.common.items.ExNihiloItems
import com.jozufozu.exnihiloomnia.common.lib.LibBlocks
import com.jozufozu.exnihiloomnia.common.util.IModelRegister

class BlockBarrelWood : BlockBarrel(LibBlocks.WOODEN_BARREL, Material.WOOD, SoundType.WOOD), IModelRegister {
    companion object {
        @JvmField val VARIANT: PropertyEnum<EnumType> = BlockPlanks.VARIANT
    }

    init {
        this.defaultState = this.blockState.baseState.withProperty<EnumType, EnumType>(VARIANT, BlockPlanks.EnumType.OAK)
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
        return this.blockState.baseState.withProperty<EnumType, EnumType>(VARIANT, BlockPlanks.EnumType.byMetadata(meta))
    }

    override fun getMetaFromState(state: IBlockState) = state.getValue<EnumType>(VARIANT).metadata

    override fun getItemBlock(): ItemBlock {
        if (ExNihiloItems.hasRegisteredItems())
            return Item.getItemFromBlock(this) as ItemBlock

        val out = ItemMultiTexture(this, this) { item -> BlockPlanks.EnumType.byMetadata(item.metadata).unlocalizedName }
        out.registryName = registryName!!
        return out
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
