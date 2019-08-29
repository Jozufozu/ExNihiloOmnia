package com.jozufozu.exnihiloomnia.proxy

import com.jozufozu.exnihiloomnia.ExNihilo
import com.jozufozu.exnihiloomnia.client.renderers.TileEntityBarrelRenderer
import com.jozufozu.exnihiloomnia.common.blocks.ExNihiloBlocks
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.TileEntityBarrel
import com.jozufozu.exnihiloomnia.common.entity.EntityThrownStone
import com.jozufozu.exnihiloomnia.common.items.ExNihiloItems
import com.jozufozu.exnihiloomnia.common.util.IModelRegister
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.entity.RenderSnowball
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.client.event.TextureStitchEvent
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.client.registry.RenderingRegistry
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class ClientProxy : CommonProxy() {

    override fun preInit(event: FMLPreInitializationEvent) {
        super.preInit(event)

        RenderingRegistry.registerEntityRenderingHandler(EntityThrownStone::class.java) { manager -> RenderSnowball(manager, ExNihiloItems.STONE, Minecraft.getMinecraft().renderItem) }
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBarrel::class.java, TileEntityBarrelRenderer())
    }

    @SubscribeEvent
    fun onTextureStitch(event: TextureStitchEvent.Pre) {
        COMPOST = event.map.registerSprite(COMPOST_RESOURCE)
    }

    @SubscribeEvent
    fun registerModels(event: ModelRegistryEvent) {
        for (modBlock in ExNihiloBlocks.modBlocks)
            if (modBlock is IModelRegister)
                (modBlock as IModelRegister).registerModels()

        for (item in ExNihiloItems.modItems)
            if (item is IModelRegister)
                (item as IModelRegister).registerModels()
    }

    companion object {
        val COMPOST_RESOURCE = ResourceLocation(ExNihilo.MODID, "blocks/compost")
        lateinit var COMPOST: TextureAtlasSprite
    }
}
