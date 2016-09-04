package exnihiloomnia.items.itemblocks;

import net.minecraft.block.Block;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBarrelGlassColored extends ItemBlock{
	public ItemBarrelGlassColored(Block block) {
		super(block);
		
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
	    return super.getUnlocalizedName() + "_" + EnumDyeColor.byMetadata(stack.getItemDamage()).getName();
	}
	
	@Override
    public int getMetadata(int damage)
    {
        return damage;
    }
}
