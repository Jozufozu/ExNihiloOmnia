package com.jozufozu.exnihiloomnia.common.blocks.leaves

import com.jozufozu.exnihiloomnia.ExNihilo
import com.jozufozu.exnihiloomnia.common.ModConfig
import com.jozufozu.exnihiloomnia.common.blocks.ModBlock
import com.jozufozu.exnihiloomnia.common.items.ExNihiloItems
import com.jozufozu.exnihiloomnia.common.util.Color
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.material.MapColor
import net.minecraft.block.material.Material
import net.minecraft.block.state.BlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.client.renderer.color.IBlockColor
import net.minecraft.client.renderer.model.ModelResourceLocation
import net.minecraft.client.resources.I18n
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.init.Items
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.IEnviromentBlockReader
import net.minecraft.world.World
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.client.event.ModelBakeEvent
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.EventPriority
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.OnlyIn
import net.minecraftforge.fml.relauncher.ReflectionHelper
import net.minecraftforge.fml.relauncher.Side

class BlockSilkwormInfested(val mimic: Block, blockName: ResourceLocation) : ModBlock(blockName, Properties.from(mimic)) {
    val mimicState: MimicStateContainer

    init {
        if (mimic.defaultState.material === Material.AIR) throw BlockMimicException("Cannot mimic air! This is likely a programmer's mistake, and probably not a config issue")
        MimicStateContainer.currentMimic = mimic
        mimicState = MimicStateContainer(mimic, this)
        blockStateField.set(this, mimicState)
        this.defaultState = this.stateContainer.baseState
    }

    fun uninfestedToInfested(uninfested: BlockState): BlockState = mimicState.getMimickingBlockState(uninfested)
    fun infestedToUninfested(infested: BlockState): BlockState = mimicState.getMimickedBlockState(infested)

    override fun removedByPlayer(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, willHarvest: Boolean): Boolean {
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

    override fun breakBlock(world: World, pos: BlockPos, state: BlockState) {
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
        @OnlyIn(Dist.CLIENT)
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
        @OnlyIn(Dist.CLIENT)
        @SubscribeEvent
        fun registerModels(event: ModelRegistryEvent) {
            infestedBlocks.forEach { leaf ->
                ModelLoader.setCustomStateMapper(leaf) {
                    leaf.mimicState.validStates.asSequence().map { it to ModelResourceLocation("minecraft:reeds", "normal") }.toMap()
                }
            }
        }

        @OnlyIn(Dist.CLIENT)
        fun postInit() {
            with(Minecraft.getInstance().blockColors) {
                for (leaf in infestedBlocks) {
                    register(IBlockColor { infested: BlockState, world: IEnviromentBlockReader?, pos: BlockPos?, i: Int ->
                        if (world != null && pos != null) (world.getTileEntity(pos) as? TileEntitySilkwormInfested)?.let {
                            if (it.infested) return@IBlockColor -1

                            val mimic = leaf.infestedToUninfested(infested)

                            val mimickedColor = Color(getColor(mimic, world, pos, i))
                            return@IBlockColor Color.weightedAverage(mimickedColor, Color.WHITE, it.percentInfested).toInt()
                        }

                        -1
                    }, leaf)
                }
            }
        }
    }
}
