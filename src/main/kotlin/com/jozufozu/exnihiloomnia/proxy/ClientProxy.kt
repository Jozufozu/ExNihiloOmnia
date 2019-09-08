package com.jozufozu.exnihiloomnia.proxy

import com.jozufozu.exnihiloomnia.ExNihilo
import com.jozufozu.exnihiloomnia.client.ores.textures.ExNihiloOreTextures
import com.jozufozu.exnihiloomnia.client.tileentities.TileEntityBarrelRenderer
import com.jozufozu.exnihiloomnia.common.blocks.ExNihiloBlocks
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.TileEntityBarrel
import com.jozufozu.exnihiloomnia.common.blocks.leaves.BlockSilkwormInfested
import com.jozufozu.exnihiloomnia.common.entity.EntityThrownStone
import com.jozufozu.exnihiloomnia.common.items.ExNihiloItems
import com.jozufozu.exnihiloomnia.common.lib.LibBlocks
import com.jozufozu.exnihiloomnia.common.util.IModelRegister
import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.block.model.ModelBakery
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.client.renderer.block.statemap.StateMapperBase
import net.minecraft.client.renderer.entity.RenderSnowball
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.item.ItemBlock
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.client.event.TextureStitchEvent
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.client.registry.RenderingRegistry
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent


class ClientProxy : CommonProxy() {

    override fun preInit(event: FMLPreInitializationEvent) {
        super.preInit(event)

        RenderingRegistry.registerEntityRenderingHandler(EntityThrownStone::class.java) { manager -> RenderSnowball(manager, ExNihiloItems.STONE, Minecraft.getMinecraft().renderItem) }
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBarrel::class.java, TileEntityBarrelRenderer())
    }

    override fun postInit(event: FMLPostInitializationEvent) {
        super.postInit(event)
        BlockSilkwormInfested.postInit()
    }

    @SubscribeEvent
    fun onTextureStitch(event: TextureStitchEvent.Pre) {
        COMPOST = event.map.registerSprite(COMPOST_RESOURCE)
        ExNihiloOreTextures.register(event.map)
    }

    @SubscribeEvent
    fun registerModels(event: ModelRegistryEvent) {
        for (modBlock in ExNihiloBlocks.modBlocks)
            if (modBlock is IModelRegister)
                (modBlock as IModelRegister).registerModels()

        for (item in ExNihiloItems.modItems)
            if (item is IModelRegister)
                (item as IModelRegister).registerModels()

        ModelBakery.registerItemVariants(ItemBlock(ExNihiloBlocks.WITCHWATER))

        ModelLoader.setCustomStateMapper(ExNihiloBlocks.WITCHWATER, object : StateMapperBase() {
            override fun getModelResourceLocation(state: IBlockState): ModelResourceLocation {
                return WITCHWATER_MODEL_LOCATION
            }
        })

        ModelLoader.setCustomMeshDefinition(ItemBlock(ExNihiloBlocks.WITCHWATER)) { WITCHWATER_MODEL_LOCATION }
    }

    companion object {
        val WITCHWATER_MODEL_LOCATION = ModelResourceLocation(LibBlocks.WITCHWATER, "fluid")
        val PORCELAIN_BUCKET_LOCATION = ModelResourceLocation(ResourceLocation(ExNihilo.MODID, "porcelain_bucket"), "inventory")

        val COMPOST_RESOURCE = ResourceLocation(ExNihilo.MODID, "blocks/compost")
        lateinit var COMPOST: TextureAtlasSprite
    }
}
