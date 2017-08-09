package com.jozufozu.exnihiloomnia;

import com.jozufozu.exnihiloomnia.advancements.ExNihiloTriggers;
import com.jozufozu.exnihiloomnia.common.blocks.ExNihiloBlocks;
import com.jozufozu.exnihiloomnia.common.items.ExNihiloItems;
import com.jozufozu.exnihiloomnia.common.lib.LibRegistries;
import com.jozufozu.exnihiloomnia.common.registries.HeatSource;
import com.jozufozu.exnihiloomnia.common.registries.Ore;
import com.jozufozu.exnihiloomnia.common.registries.RegistryManager;
import com.jozufozu.exnihiloomnia.common.registries.ReloadableRegistry;
import com.jozufozu.exnihiloomnia.common.registries.command.CommandRegistry;
import com.jozufozu.exnihiloomnia.common.registries.recipes.*;
import com.jozufozu.exnihiloomnia.proxy.CommonProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(modid = ExNihilo.MODID, name = ExNihilo.NAME, version = ExNihilo.VERSION)
public class ExNihilo
{
    @Mod.Instance
    public static ExNihilo INSTANCE;
    
    public static final String MODID = "exnihiloomnia";
    public static final String NAME = "Ex Nihilo Omnia";
    public static final String VERSION = "1.0";
    
    public static final Logger log = LogManager.getLogger(NAME);
    
    @SidedProxy(serverSide = "com.jozufozu.exnihiloomnia.proxy.CommonProxy", clientSide = "com.jozufozu.exnihiloomnia.proxy.ClientProxy")
    public static CommonProxy proxy;
    
    public static Configuration CONFIG;
    public static File PATH;
    
    public ExNihilo()
    {
        new ExNihiloTriggers();
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(ExNihiloBlocks.class);
        MinecraftForge.EVENT_BUS.register(ExNihiloItems.class);
    }
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        proxy.preInit(event);
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit(event);
    }
    
    @EventHandler
    public void onServerStart(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandRegistry());
    }
    
    @SubscribeEvent
    public void createRegistries(RegistryEvent.NewRegistry event)
    {
        RegistryManager.ORES = ReloadableRegistry.create(Ore.class, LibRegistries.ORE);
        
        RegistryManager.COMPOST = ReloadableRegistry.create(CompostRecipe.class, LibRegistries.COMPOST);
        RegistryManager.FLUID_CRAFTING = ReloadableRegistry.create(FluidCraftingRecipe.class, LibRegistries.FLUID_CRAFTING);
        RegistryManager.FLUID_MIXING = ReloadableRegistry.create(FluidMixingRecipe.class, LibRegistries.MIXING);
        RegistryManager.FERMENTING = ReloadableRegistry.create(FermentingRecipe.class, LibRegistries.FERMENTING);
        
        RegistryManager.SIFTING = ReloadableRegistry.create(SieveRecipe.class, LibRegistries.SIEVE);
        RegistryManager.HAMMERING = ReloadableRegistry.create(HammerRecipe.class, LibRegistries.HAMMER);
        
        RegistryManager.MELTING = ReloadableRegistry.create(MeltingRecipe.class, LibRegistries.MELTING);
        RegistryManager.HEAT = ReloadableRegistry.create(HeatSource.class, LibRegistries.HEAT_SOURCE);
    }
}
