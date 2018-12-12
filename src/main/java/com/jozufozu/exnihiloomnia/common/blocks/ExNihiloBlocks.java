package com.jozufozu.exnihiloomnia.common.blocks;

import com.jozufozu.exnihiloomnia.ExNihilo;
import com.jozufozu.exnihiloomnia.common.blocks.barrel.*;
import com.jozufozu.exnihiloomnia.common.blocks.crucible.BlockCrucible;
import com.jozufozu.exnihiloomnia.common.blocks.crucible.BlockCrucibleRaw;
import com.jozufozu.exnihiloomnia.common.blocks.sieve.BlockSieve;
import com.jozufozu.exnihiloomnia.common.lib.LibBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = ExNihilo.MODID)
@GameRegistry.ObjectHolder(ExNihilo.MODID)
public class ExNihiloBlocks
{
    private static boolean registeredBlocks = false;

    public static ArrayList<Block> modBlocks = new ArrayList<>();

    public static final Block SIEVE = null;
    public static final Block BARREL_WOOD = null;
    public static final Block BARREL_STAINED_GLASS = null;
    public static final Block BARREL_CONCRETE = null;
    public static final Block BARREL_TERRACOTTA = null;
    public static final Block BARREL_STONE = null;
    public static final Block BARREL_GLASS = null;

    public static final Block CRUCIBLE = null;
    public static final Block RAW_CRUCIBLE = null;

    public static final Block KINDLING = null;

    public static final Block DUST = null;
    public static final Block NETHER_GRAVEL = null;
    public static final Block END_GRAVEL = null;

    public static boolean hasRegisteredBlocks()
    {
        return registeredBlocks;
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
        register(new BlockSieve());
        register(new BlockBarrelWood());
        register(new BlockBarrelStainedGlass());
        register(new BlockBarrelColored(LibBlocks.CONCRETE_BARREL, Material.ROCK).setHardness(1.8f));
        register(new BlockBarrelColored(LibBlocks.TERRACOTTA_BARREL, Material.ROCK).setHardness(1.8f));
        register(new BlockBarrel(LibBlocks.STONE_BARREL, Material.ROCK).setHardness(1.5F).setResistance(10.0F));
        register(new BlockBarrelGlass());

        register(new BlockBaseFalling(LibBlocks.END_GRAVEL, Material.GROUND, SoundType.GROUND).setHardness(0.6f));
        register(new BlockBaseFalling(LibBlocks.NETHER_GRAVEL, Material.GROUND, SoundType.GROUND).setHardness(0.6f));
        register(new BlockBaseFalling(LibBlocks.DUST, Material.SAND, SoundType.SNOW).setHardness(0.4f));

        register(new BlockCrucible());
        register(new BlockCrucibleRaw());

        register(new BlockKindling());

        for (Block block : modBlocks)
            event.getRegistry().register(block);

        registeredBlocks = true;
    }

    public static Block register(Block block) {
        modBlocks.add(block);
        return block;
    }
}
