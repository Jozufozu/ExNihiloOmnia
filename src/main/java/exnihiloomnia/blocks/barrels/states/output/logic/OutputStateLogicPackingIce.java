package exnihiloomnia.blocks.barrels.states.output.logic;

import exnihiloomnia.blocks.barrels.architecture.BarrelLogic;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import exnihiloomnia.util.helpers.PositionHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.Biome;

public class OutputStateLogicPackingIce extends BarrelLogic {
	
	@Override
	public boolean onUpdate(TileEntityBarrel barrel)  {
		if (barrel.getContents().getItem() == Item.getItemFromBlock(Blocks.ICE)
			&& PositionHelper.isRainingAt(barrel.getWorld(), barrel.getPos()) && barrel.getWorld().getBiomeForCoordsBody(barrel.getPos()).getTempCategory().equals(Biome.TempCategory.COLD)) {
			
			if (barrel.getWorld().rand.nextInt(1200) == 0) {
				barrel.setContents(new ItemStack(Blocks.PACKED_ICE));
			}
		}
		
		return false;
	}
}
