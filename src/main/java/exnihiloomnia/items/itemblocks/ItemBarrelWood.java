package exnihiloomnia.items.itemblocks;

import exnihiloomnia.util.enums.EnumWood;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBarrelWood extends ItemBlock{
	public ItemBarrelWood(Block block) {
		super(block);
		
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
	    return super.getUnlocalizedName() + "_" + EnumWood.fromMetadata(stack.getItemDamage()).getName();
	}
	
	@Override
    public int getMetadata(int damage)
    {
        return damage;
    }
}
