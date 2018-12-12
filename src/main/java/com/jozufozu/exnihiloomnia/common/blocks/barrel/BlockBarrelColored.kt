package com.jozufozu.exnihiloomnia.common.blocks.barrel

import com.jozufozu.exnihiloomnia.common.items.ExNihiloItems

open class BlockBarrelColored @JvmOverloads constructor(registryName: ResourceLocation, materialIn: Material, soundType: SoundType = SoundType.STONE) : BlockBarrel(registryName, materialIn, soundType) {
    companion object {
        @JvmField val COLOR: PropertyEnum<EnumDyeColor> = PropertyEnum.create("color", EnumDyeColor::class.java)
    }

    init {
        this.defaultState = this.blockState.baseState.withProperty(COLOR, EnumDyeColor.WHITE)
    }

    override fun getPickBlock(state: IBlockState, target: RayTraceResult, world: World, pos: BlockPos, player: EntityPlayer): ItemStack {
        return ItemStack(this, 1, getMetaFromState(state))
    }

    override fun getSubBlocks(itemIn: CreativeTabs, items: NonNullList<ItemStack>) {
        items.addAll(EnumDyeColor.values().map { ItemStack(this, 1, it.dyeDamage) })
    }

    override fun damageDropped(state: IBlockState) = getMetaFromState(state)

    override fun createBlockState() = BlockStateContainer(this, COLOR)

    override fun getStateFromMeta(meta: Int): IBlockState {
        return this.blockState.baseState.withProperty(COLOR, EnumDyeColor.byMetadata(meta))
    }

    override fun getMetaFromState(state: IBlockState) = state.getValue(COLOR).metadata

    override fun getItemBlock(): ItemBlock {
        if (ExNihiloItems.hasRegisteredItems()) return Item.getItemFromBlock(this) as ItemBlock

        val out = ItemMultiTexture(this, this) { item -> EnumDyeColor.byMetadata(item.metadata).dyeColorName }
        out.registryName = registryName
        return out
    }

    @SideOnly(Side.CLIENT)
    override fun registerModels() {
        val registryName = this.registryName ?: return
        val itemBlock = itemBlock
        for (enumType in EnumDyeColor.values()) {
            ModelLoader.setCustomModelResourceLocation(itemBlock, enumType.metadata, ModelResourceLocation(registryName, "color=" + enumType.dyeColorName))
        }
    }
}
