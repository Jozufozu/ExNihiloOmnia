package exnihiloomnia.items;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.IFuelHandler;

public class ENOFuelHandler implements IFuelHandler{

	@Override
	public int getBurnTime(ItemStack fuel) {
		if (fuel.getItem() == ENOItems.BUCKET_PORCELAIN_LAVA)
			return 20000;
		
		return 0;
	}

}
