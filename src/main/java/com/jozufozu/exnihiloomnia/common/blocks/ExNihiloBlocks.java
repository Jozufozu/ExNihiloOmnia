package com.jozufozu.exnihiloomnia.common.blocks;

import com.jozufozu.exnihiloomnia.common.blocks.barrel.*;
import com.jozufozu.exnihiloomnia.common.blocks.crucible.BlockCrucible;
import com.jozufozu.exnihiloomnia.common.blocks.crucible.BlockCrucibleRaw;
import com.jozufozu.exnihiloomnia.common.blocks.sieve.BlockSieve;
import com.jozufozu.exnihiloomnia.common.lib.LibBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

public class ExNihiloBlocks
{
    private static boolean registeredBlocks = false;
    
    public static final ArrayList<Block> modBlocks = new ArrayList<>();
    
    public static Block SIEVE;
    public static Block WOOD_BARREL;
    public static Block STAINED_GLASS_BARREL;
    public static Block CONCRETE_BARREL;
    public static Block TERRACOTTA_BARREL;
    public static Block STONE_BARREL;
    public static Block GLASS_BARREL;
    
    public static Block CRUCIBLE;
    public static Block RAW_CRUCIBLE;
    
    public static Block CAMP_FIRE;
    
    public static Block DUST;
    public static Block NETHER_GRAVEL;
    public static Block END_GRAVEL;
    
    public static boolean hasRegisteredBlocks()
    {
        return registeredBlocks;
    }
    
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
        SIEVE = new BlockSieve();
        WOOD_BARREL = new BlockBarrelWood();
        STAINED_GLASS_BARREL = new BlockBarrelStainedGlass();
        CONCRETE_BARREL = new BlockBarrelColored(LibBlocks.CONCRETE_BARREL, Material.ROCK).setHardness(1.8f);
        TERRACOTTA_BARREL = new BlockBarrelColored(LibBlocks.TERRACOTTA_BARREL, Material.ROCK).setHardness(1.8f);
        STONE_BARREL = new BlockBarrel(LibBlocks.STONE_BARREL, Material.ROCK).setHardness(1.5F).setResistance(10.0F);
        GLASS_BARREL = new BlockBarrelGlass();
    
        END_GRAVEL = new BlockBaseFalling(LibBlocks.END_GRAVEL, Material.GROUND, SoundType.GROUND).setHardness(0.6f);
        NETHER_GRAVEL = new BlockBaseFalling(LibBlocks.NETHER_GRAVEL, Material.GROUND, SoundType.GROUND).setHardness(0.6f);
        DUST = new BlockBaseFalling(LibBlocks.DUST, Material.SAND, SoundType.SNOW).setHardness(0.4f);
        
        CRUCIBLE = new BlockCrucible();
        RAW_CRUCIBLE = new BlockCrucibleRaw();
        
        CAMP_FIRE = new BlockKindling();
        
        for (Block block : modBlocks)
            event.getRegistry().register(block);
        
        registeredBlocks = true;
    }
}
