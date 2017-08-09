package com.jozufozu.exnihiloomnia.proxy;

import com.jozufozu.exnihiloomnia.common.blocks.ExNihiloBlocks;
import com.jozufozu.exnihiloomnia.common.entity.EntityThrownStone;
import com.jozufozu.exnihiloomnia.common.items.ExNihiloItems;
import com.jozufozu.exnihiloomnia.common.util.IModelRegister;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        super.preInit(event);
        RenderingRegistry.registerEntityRenderingHandler(EntityThrownStone.class, manager ->
                new RenderSnowball<>(manager, ExNihiloItems.STONE, Minecraft.getMinecraft().getRenderItem()));
    
    }
    
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event)
    {
        for (Block modBlock : ExNihiloBlocks.modBlocks)
            if (modBlock instanceof IModelRegister)
                ((IModelRegister) modBlock).registerModels();
    
        for (Item item : ExNihiloItems.modItems)
            if (item instanceof IModelRegister)
                ((IModelRegister) item).registerModels();
    }
}
