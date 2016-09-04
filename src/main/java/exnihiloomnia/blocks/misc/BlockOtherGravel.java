package exnihiloomnia.blocks.misc;

import exnihiloomnia.items.ENOItems;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockOtherGravel extends BlockFalling{

	public BlockOtherGravel()
	{
		super(Material.SAND);

		this.setCreativeTab(ENOItems.ENO_TAB);
		this.setHardness(0.6f);
		this.setSoundType(SoundType.GROUND);
		this.setHarvestLevel("shovel", 0);
	}
}
