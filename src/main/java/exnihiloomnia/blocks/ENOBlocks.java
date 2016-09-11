package exnihiloomnia.blocks;

import exnihiloomnia.ENO;
import exnihiloomnia.blocks.automation.BlockSifter;
import exnihiloomnia.blocks.automation.tile.TileSifter;
import exnihiloomnia.blocks.barrels.BlockBarrel;
import exnihiloomnia.blocks.barrels.BlockBarrelGlassColored;
import exnihiloomnia.blocks.barrels.BlockBarrelWood;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import exnihiloomnia.blocks.crucibles.BlockCrucible;
import exnihiloomnia.blocks.crucibles.BlockCrucibleRaw;
import exnihiloomnia.blocks.crucibles.tileentity.TileEntityCrucible;
import exnihiloomnia.blocks.fluids.BlockFluidWitchwater;
import exnihiloomnia.blocks.leaves.BlockInfestedLeaves;
import exnihiloomnia.blocks.leaves.TileEntityInfestedLeaves;
import exnihiloomnia.blocks.misc.BlockDust;
import exnihiloomnia.blocks.misc.BlockOtherGravel;
import exnihiloomnia.blocks.ores.BlockOre;
import exnihiloomnia.blocks.sieves.BlockSieveWood;
import exnihiloomnia.blocks.sieves.tileentity.TileEntitySieve;
import exnihiloomnia.fluids.ENOFluids;
import exnihiloomnia.items.ENOItems;
import exnihiloomnia.items.itemblocks.ItemBarrelGlassColored;
import exnihiloomnia.items.itemblocks.ItemBarrelWood;
import exnihiloomnia.items.itemblocks.ItemBlockOre;
import exnihiloomnia.items.itemblocks.ItemSieveWood;
import exnihiloomnia.util.enums.EnumOreBlockType;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.List;

public class ENOBlocks {
	
	public static Block BARREL_WOOD;
	public static Block BARREL_STONE;
	public static Block BARREL_GLASS;
	public static Block BARREL_GLASS_COLORED;
	public static Block CRUCIBLE;
	public static Block CRUCIBLE_RAW;
	public static Block GRAVEL_NETHER;
	public static Block GRAVEL_ENDER;
	public static Block DUST;
	public static Block SIEVE_WOOD;
	public static Block INFESTED_LEAVES;
	public static Block WITCHWATER;

    //public static Block SIFTER;

    public static Block ORE_GRAVEL;
    public static Block ORE_GRAVEL_NETHER;
    public static Block ORE_GRAVEL_ENDER;
    public static Block ORE_SAND;
    public static Block ORE_DUST;

    public static List<Block> getBlocks() {
        List<Block> blocks = new ArrayList<Block>();

        blocks.add(SIEVE_WOOD);
        blocks.add(DUST);
        blocks.add(GRAVEL_ENDER);
        blocks.add(GRAVEL_NETHER);

        blocks.add(CRUCIBLE);
        blocks.add(CRUCIBLE_RAW);
        blocks.add(BARREL_GLASS_COLORED);
        blocks.add(BARREL_GLASS);
        blocks.add(BARREL_STONE);
        blocks.add(BARREL_WOOD);
        blocks.add(INFESTED_LEAVES);

        blocks.add(WITCHWATER);

        //blocks.add(SIFTER);

        blocks.add(ORE_GRAVEL);
        blocks.add(ORE_GRAVEL_ENDER);
        blocks.add(ORE_GRAVEL_NETHER);
        blocks.add(ORE_SAND);
        blocks.add(ORE_DUST);

        return blocks;
    }


	public static void init()
	{
	    BARREL_WOOD = new BlockBarrelWood().setUnlocalizedName("barrel_wood").setRegistryName("barrel_wood");
        BARREL_STONE = new BlockBarrel(Material.ROCK, SoundType.STONE).setUnlocalizedName("barrel_stone").setRegistryName("barrel_stone");
        BARREL_GLASS = new BlockBarrel(Material.GLASS, SoundType.GLASS).setUnlocalizedName("barrel_glass").setRegistryName("barrel_glass");
        BARREL_GLASS_COLORED = new BlockBarrelGlassColored().setUnlocalizedName("barrel_glass_colored").setRegistryName("barrel_glass_colored");
        CRUCIBLE = new BlockCrucible().setUnlocalizedName("crucible").setRegistryName("crucible");
        CRUCIBLE_RAW = new BlockCrucibleRaw().setUnlocalizedName("crucible_raw").setRegistryName("crucible_raw");
        GRAVEL_ENDER = new BlockOtherGravel().setUnlocalizedName("gravel_ender").setRegistryName("gravel_ender").setCreativeTab(ENOItems.ENO_TAB);
        GRAVEL_NETHER = new BlockOtherGravel().setUnlocalizedName("gravel_nether").setRegistryName("gravel_nether").setCreativeTab(ENOItems.ENO_TAB);
        DUST = new BlockDust().setUnlocalizedName("dust").setRegistryName("dust");
        SIEVE_WOOD = new BlockSieveWood(Material.WOOD).setUnlocalizedName("sieve_wood").setRegistryName("sieve_wood");
        INFESTED_LEAVES = new BlockInfestedLeaves().setUnlocalizedName("infested_leaves").setRegistryName("infested_leaves");

        WITCHWATER = new BlockFluidWitchwater(ENOFluids.WITCHWATER, Material.WATER).setUnlocalizedName("witchwater").setRegistryName("witchwater");

        //SIFTER = new BlockSifter().setRegistryName("sifter");

        ORE_GRAVEL = new BlockOre(EnumOreBlockType.GRAVEL).setUnlocalizedName("ore_gravel").setRegistryName("ore_gravel");
        ORE_GRAVEL_ENDER = new BlockOre(EnumOreBlockType.GRAVEL_ENDER).setUnlocalizedName("ore_gravel_ender").setRegistryName("ore_gravel_ender");
        ORE_GRAVEL_NETHER = new BlockOre(EnumOreBlockType.GRAVEL_NETHER).setUnlocalizedName("ore_gravel_nether").setRegistryName("ore_gravel_nether");
        ORE_SAND = new BlockOre(EnumOreBlockType.SAND).setUnlocalizedName("ore_sand").setRegistryName("ore_sand");
        ORE_DUST = new BlockOre(EnumOreBlockType.DUST).setUnlocalizedName("ore_dust").setRegistryName("ore_dust");

        for (Block block : getBlocks()) {
            GameRegistry.register(block);
            if (block == BARREL_WOOD)
                GameRegistry.register(new ItemBarrelWood(block).setRegistryName(block.getRegistryName()));
            else if (block == BARREL_GLASS_COLORED)
                GameRegistry.register(new ItemBarrelGlassColored(block).setRegistryName(block.getRegistryName()));
            else if (block == SIEVE_WOOD)
                GameRegistry.register(new ItemSieveWood(block).setRegistryName(block.getRegistryName()));
            else if (block instanceof BlockOre)
                GameRegistry.register(new ItemBlockOre(block).setRegistryName(block.getRegistryName()));
            else if (block != WITCHWATER)
                GameRegistry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
        }

		GameRegistry.registerTileEntity(TileEntityBarrel.class, ENO.MODID + ":tile_entity_barrel");
		GameRegistry.registerTileEntity(TileEntitySieve.class, ENO.MODID + ":tile_entity_sieve");
		GameRegistry.registerTileEntity(TileEntityCrucible.class, ENO.MODID + ":tile_entity_crucible");
		GameRegistry.registerTileEntity(TileEntityInfestedLeaves.class, ENO.MODID + ":tile_entity_infested_leaves");
        //GameRegistry.registerTileEntity(TileSifter.class, ENO.MODID + ":tile_entity_sifter");
	}
}
