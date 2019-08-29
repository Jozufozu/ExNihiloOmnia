package com.jozufozu.exnihiloomnia.common.blocks.barrel

import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemMultiTexture
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.world.World
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

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

    override val itemBlock: ItemBlock by lazy {
        ItemMultiTexture(this, this) { item -> EnumDyeColor.byMetadata(item.metadata).dyeColorName }.also {
            it.registryName = registryName
        }
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
