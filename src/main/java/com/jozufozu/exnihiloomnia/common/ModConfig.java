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

    public static boolean enableCraftingCustomization = false;
    
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
        @Config.Comment("Options for barrels")
        @Config.RequiresWorldRestart
        public Barrel barrel = new Barrel();

        @Config.Comment("Options for crucibles")
        @Config.RequiresWorldRestart
        public Crucible crucible = new Crucible();

        public String[] silkwormInfestable = new String[] { "minecraft:leaves", "minecraft:leaves2", "minecraft:vines" };
        
        public static class Barrel
        {
            @Config.Comment("How much fluid content barrels can hold, in mB")
            public int fluidCapacity = 1000;
    
            @Config.Comment("How much compost material barrels can hold, in mB")
            public int compostCapacity = 1000;
    
            @Config.Comment("How much time in ticks it takes for compost to turn into its output")
            public int compostTime = 400;

            @Config.Comment("The fluid temperature required to make wooden barrels burn")
            public int burnTemperature = 873;

            @Config.Comment("The amount of time (in ticks) that a wooden barrel can hold liquids that are too hot")
            public int burnTime = 400;
        }
    
        public static class Crucible
        {
            @Config.Comment("How much fluid content crucibles can hold, in mB")
            public int fluidCapacity = 4000;
        
            @Config.Comment("How much solid material crucibles can hold, in mB")
            public int solidCapacity = 4000;
        }
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
