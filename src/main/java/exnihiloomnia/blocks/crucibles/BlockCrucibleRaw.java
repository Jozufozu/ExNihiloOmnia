package exnihiloomnia.blocks.crucibles;

import exnihiloomnia.items.ENOItems;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockCrucibleRaw extends Block{

	public BlockCrucibleRaw()
	{
		super(Material.CLAY);
		
		this.setHardness(0.6f);
		this.setHarvestLevel("shovel", 0);
		this.setSoundType(SoundType.GROUND);
		this.setCreativeTab(ENOItems.ENO_TAB);
	}

	@Override
	public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos)
	{ 
		return false; 
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
}
