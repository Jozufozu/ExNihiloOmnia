package com.jozufozu.exnihiloomnia.common.blocks.leaves

import com.jozufozu.exnihiloomnia.ExNihilo
import com.jozufozu.exnihiloomnia.common.ModConfig
import com.jozufozu.exnihiloomnia.common.blocks.BlockBase
import com.jozufozu.exnihiloomnia.common.items.ExNihiloItems
import com.jozufozu.exnihiloomnia.common.util.Color
import net.minecraft.block.Block
import net.minecraft.block.ITileEntityProvider
import net.minecraft.block.material.MapColor
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.client.renderer.color.IBlockColor
import net.minecraft.client.resources.I18n
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.ItemStack
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.Explosion
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.client.event.ModelBakeEvent
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.ReflectionHelper
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.util.*

class BlockSilkwormInfested(val mimic: Block, blockName: ResourceLocation) : BlockBase(blockName, mimic.defaultState.material, mimic.soundType), ITileEntityProvider {
    val mimicState: MimicBlockState

    init {
        if (mimic.defaultState.material === Material.AIR) throw BlockMimicException("Cannot mimic air! This is likely a programmer's mistake, and probably not a config issue")
        MimicBlockState.currentMimic = mimic
        mimicState = MimicBlockState(mimic, this)
        blockStateField.set(this, mimicState)
        this.defaultState = this.blockState.baseState
        this.fullBlock = this.defaultState.isOpaqueCube
        this.lightOpacity = if (this.fullBlock) 255 else 0
    }

    fun uninfestedToInfested(uninfested: IBlockState): IBlockState = mimicState.getMimickingBlockState(uninfested)
    fun infestedToUninfested(infested: IBlockState): IBlockState = mimicState.getMimickedBlockState(infested)

    override fun removedByPlayer(state: IBlockState, world: World, pos: BlockPos, player: EntityPlayer, willHarvest: Boolean): Boolean {
        if (!world.isRemote && !player.isCreative) {
            (world.getTileEntity(pos) as? TileEntitySilkwormInfested)?.let {
                if (world.rand.nextFloat() < it.percentInfested * 1 / 4.0)
                    spawnAsEntity(world, pos, ItemStack(Items.STRING))

                if (world.rand.nextFloat() < it.percentInfested)
                    spawnAsEntity(world, pos, ItemStack(Items.STRING))

                if (world.rand.nextFloat() < 0.6)
                    spawnAsEntity(world, pos, ItemStack(ExNihiloItems.SILKWORM))
            }
        }

        world.removeTileEntity(pos)
        return world.setBlockToAir(pos)
    }

    override fun breakBlock(world: World, pos: BlockPos, state: IBlockState) {
        mimic.breakBlock(world, pos, state)
        super.breakBlock(world, pos, state)
    }

    override fun getLocalizedName(): String {
        val name = I18n.format(unlocalizedName)

        return if (name == unlocalizedName)
            I18n.format("tile.exnihiloomnia.infested_generic.name", mimic.localizedName)
        else
            name
    }

    override fun createNewTileEntity(worldIn: World, meta: Int) = TileEntitySilkwormInfested()

    override fun beginLeavesDecay(state: IBlockState, world: World, pos: BlockPos) {
        mimic.beginLeavesDecay(state, world, pos)
    }

    override fun isLeaves(state: IBlockState, world: IBlockAccess, pos: BlockPos) = mimic.isLeaves(state, world, pos)

    override fun getMapColor(state: IBlockState, worldIn: IBlockAccess, pos: BlockPos): MapColor = MapColor.WHITE_STAINED_HARDENED_CLAY

    override fun getActualState(state: IBlockState, worldIn: IBlockAccess, pos: BlockPos): IBlockState {
        val actualState = infestedToUninfested(state).getActualState(worldIn, pos)
        return uninfestedToInfested(actualState)
    }

    override fun getMetaFromState(state: IBlockState): Int = mimic.getMetaFromState(infestedToUninfested(state))

    override fun getStateFromMeta(meta: Int): IBlockState = uninfestedToInfested(mimic.getStateFromMeta(meta))

    override fun getFlammability(world: IBlockAccess, pos: BlockPos, face: EnumFacing): Int = mimic.getFlammability(world, pos, face)

    override fun isFlammable(world: IBlockAccess, pos: BlockPos, face: EnumFacing): Boolean = mimic.isFlammable(world, pos, face)

    override fun getFireSpreadSpeed(world: IBlockAccess, pos: BlockPos, face: EnumFacing): Int = mimic.getFireSpreadSpeed(world, pos, face)

    override fun isFireSource(world: World, pos: BlockPos, side: EnumFacing): Boolean = mimic.isFireSource(world, pos, side)

    override fun canRenderInLayer(state: IBlockState, layer: BlockRenderLayer): Boolean = mimic.canRenderInLayer(mimicState.getMimickedBlockState(state), layer)

    override fun recolorBlock(world: World, pos: BlockPos, side: EnumFacing, color: EnumDyeColor): Boolean = mimic.recolorBlock(world, pos, side, color)

    override fun isCollidable(): Boolean = mimic.isCollidable

    override fun canCollideCheck(state: IBlockState, hitIfLiquid: Boolean): Boolean = mimic.canCollideCheck(state, hitIfLiquid)

    override fun modifyAcceleration(worldIn: World, pos: BlockPos, entityIn: Entity, motion: Vec3d): Vec3d = mimic.modifyAcceleration(worldIn, pos, entityIn, motion)

    override fun doesSideBlockChestOpening(blockState: IBlockState, world: IBlockAccess, pos: BlockPos, side: EnumFacing): Boolean = mimic.doesSideBlockChestOpening(blockState, world, pos, side)

    override fun isBurning(world: IBlockAccess, pos: BlockPos): Boolean = mimic.isBurning(world, pos)

    override fun getExplosionResistance(world: World, pos: BlockPos, exploder: Entity?, explosion: Explosion): Float = mimic.getExplosionResistance(world, pos, exploder, explosion)

    override fun onBlockAdded(worldIn: World, pos: BlockPos, state: IBlockState) {
        mimic.onBlockAdded(worldIn, pos, state)
    }

    override fun isToolEffective(type: String, state: IBlockState): Boolean = mimic.isToolEffective(type, state)

    override fun rotateBlock(world: World, pos: BlockPos, axis: EnumFacing): Boolean = mimic.rotateBlock(world, pos, axis)

    override fun isLadder(state: IBlockState, world: IBlockAccess, pos: BlockPos, entity: EntityLivingBase): Boolean = mimic.isLadder(state, world, pos, entity)

    override fun canSpawnInBlock(): Boolean = mimic.canSpawnInBlock()

    override fun onEntityWalk(worldIn: World, pos: BlockPos, entityIn: Entity) {
        mimic.onEntityWalk(worldIn, pos, entityIn)
    }

    override fun updateTick(worldIn: World, pos: BlockPos, state: IBlockState, rand: Random) {
        mimic.updateTick(worldIn, pos, state, rand)
    }

    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean = mimic.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ)

    override fun randomTick(worldIn: World, pos: BlockPos, state: IBlockState, random: Random) {}

    override fun onEntityCollidedWithBlock(worldIn: World, pos: BlockPos, state: IBlockState, entityIn: Entity) {
        mimic.onEntityCollidedWithBlock(worldIn, pos, state, entityIn)
    }

    override fun canPlaceBlockOnSide(worldIn: World, pos: BlockPos, side: EnumFacing): Boolean = mimic.canPlaceBlockOnSide(worldIn, pos, side)

    override fun canPlaceBlockAt(worldIn: World, pos: BlockPos): Boolean = mimic.canPlaceBlockAt(worldIn, pos)

    @SideOnly(Side.CLIENT)
    override fun randomDisplayTick(stateIn: IBlockState, worldIn: World, pos: BlockPos, rand: Random) {
        mimic.randomDisplayTick(stateIn, worldIn, pos, rand)
    }

    @Mod.EventBusSubscriber(modid = ExNihilo.MODID)
    companion object {
        private val blockStateField by lazy { ReflectionHelper.findField(Block::class.java, "blockState", "field_176227_L") }

        private val infestedBlocks = mutableListOf<BlockSilkwormInfested>()

        private val infestedMap = hashMapOf<Block, BlockSilkwormInfested>()
        val blockMappings: Map<Block, BlockSilkwormInfested> get() = this.infestedMap

        @SubscribeEvent(priority = EventPriority.LOWEST)
        fun registerBlocks(event: RegistryEvent.Register<Block>) {
            val registry = event.registry
            ModConfig.blocks.silkwormInfestable.asSequence()
                    .map { ResourceLocation(it) }
                    .mapNotNull { registry.getValue(it) }
                    .filterNot { it.defaultState.material === Material.AIR }
                    .map { it to BlockSilkwormInfested(it, ResourceLocation(ExNihilo.MODID, "infested_${it.registryName!!.resourceDomain}_${it.registryName!!.resourcePath}")) }
                    .forEach {
                        infestedBlocks.add(it.second)
                        infestedMap[it.first] = it.second
                        registry.register(it.second)
                    }
        }

        // Map our blockstates to the corresponding models of the other block
        @SideOnly(Side.CLIENT)
        @SubscribeEvent(priority = EventPriority.LOWEST)
        fun registerModels(event: ModelBakeEvent) {
            infestedBlocks.forEach { leaf ->
                event.modelManager.blockModelShapes.registerBlockWithStateMapper(leaf) { _ ->
                    event.modelManager.blockModelShapes.blockStateMapper.getVariants(leaf.mimic).asSequence().map {
                        leaf.uninfestedToInfested(it.key) to it.value
                    }.toMap()
                }
            }
        }

        // Register the blocks with a dummy model until we have enough information to register the proper models
        @SideOnly(Side.CLIENT)
        @SubscribeEvent
        fun registerModels(event: ModelRegistryEvent) {
            infestedBlocks.forEach { leaf ->
                ModelLoader.setCustomStateMapper(leaf) {
                    leaf.mimicState.validStates.asSequence().map { it to ModelResourceLocation("minecraft:reeds", "normal") }.toMap()
                }
            }
        }

        @SideOnly(Side.CLIENT)
        fun postInit() {
            with(Minecraft.getMinecraft().blockColors) {
                for (leaf in infestedBlocks) {
                    registerBlockColorHandler(IBlockColor { infested: IBlockState, world: IBlockAccess?, pos: BlockPos?, i: Int ->
                        if (world != null && pos != null) (world.getTileEntity(pos) as? TileEntitySilkwormInfested)?.let {
                            if (it.infested) return@IBlockColor -1

                            val mimic = leaf.infestedToUninfested(infested)

                            val mimickedColor = Color(colorMultiplier(mimic, world, pos, i))
                            return@IBlockColor Color.weightedAverage(mimickedColor, Color.WHITE, it.percentInfested).toInt()
                        }

                        -1
                    }, leaf)
                }
            }
        }
    }
}
