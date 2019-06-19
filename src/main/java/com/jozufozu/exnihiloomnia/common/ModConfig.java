package com.jozufozu.exnihiloomnia.common;

public class ModConfig
{
    public static World world = new World();
    
    public static class World
    {
        public String spawnIsland = "spawn_oak";

        public String biome = "minecraft:taiga";

        public int cloudLevel = 0;

        public int spawnLevel = 96;
    }
    
    public static final Blocks blocks = new Blocks();
    
    public static class Blocks
    {
        public double kindlingLightChance = 0.05;

        public Barrel barrel = new Barrel();
        
        public static class Barrel
        {
            public int fluidCapacity = 1000;

            public int compostCapacity = 1000;

            public int compostTime = 400;
        }

        public Crucible crucible = new Crucible();
    
        public static class Crucible
        {
            public int fluidCapacity = 4000;

            public int solidCapacity = 4000;
        }
    }
}
