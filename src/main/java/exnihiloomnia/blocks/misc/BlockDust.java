package exnihiloomnia.blocks.misc;

import exnihiloomnia.items.ENOItems;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockDust extends BlockFalling{

	public BlockDust()
	{
		super(Material.SAND);

		this.setCreativeTab(ENOItems.ENO_TAB);
		this.setHardness(0.4f);
		this.setSoundType(SoundType.SNOW);
		this.setHarvestLevel("shovel", 0);
	}
}
