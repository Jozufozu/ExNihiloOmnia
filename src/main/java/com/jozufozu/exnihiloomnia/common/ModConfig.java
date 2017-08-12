package com.jozufozu.exnihiloomnia.common;

import com.jozufozu.exnihiloomnia.ExNihilo;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = ExNihilo.MODID, name = "/exnihiloomnia/ExNihiloOmnia")
@Config.LangKey("config.exnihiloomnia.title")
public class ModConfig
{
    @Config.Comment("Options for the world")
    public static World world = new World();
    
    public static class World
    {
        @Config.Comment("The file name of the spawn island to load. Files are located in /config/exnihiloomnia/spawn_island")
        @Config.RequiresWorldRestart
        public String spawnIsland = "spawn_oak";
    
        @Config.Comment("What biome SkyBlock worlds will generate with. Blank means every biome")
        @Config.RequiresWorldRestart
        public String biome = "minecraft:taiga";
        
        @Config.Comment("How high clouds are")
        @Config.RequiresWorldRestart
        @Config.RangeInt(min = 0)
        public int cloudLevel = 0;
    
        @Config.Comment("What y level the spawn island is generated")
        @Config.RequiresWorldRestart
        @Config.RangeInt(min = 0, max = 255)
        public int spawnLevel = 96;
    }
    
    public static final Blocks blocks = new Blocks();
    
    public static class Blocks
    {
        @Config.Comment("How likely kindling is to light when using a stick")
        @Config.RangeDouble(min = 0.0, max = 1.0)
        public double kindlingLightChance = 0.05;
    }
    
    @Mod.EventBusSubscriber(modid = ExNihilo.MODID)
    public static class Handler
    {
        @SubscribeEvent
        public static void onConfigChangedOnConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
        {
            if (ExNihilo.MODID.equals(event.getModID()))
            {
                ConfigManager.sync(ExNihilo.MODID, Config.Type.INSTANCE);
            }
        }
    }
}
