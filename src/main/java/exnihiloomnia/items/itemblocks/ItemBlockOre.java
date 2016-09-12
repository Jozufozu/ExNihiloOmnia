package exnihiloomnia.items.itemblocks;

import exnihiloomnia.util.enums.EnumOre;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockOre extends ItemBlock {
	
	public ItemBlockOre(Block block) {
		super(block);
		
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
	    return super.getUnlocalizedName() + "." + EnumOre.fromMetadata(stack.getItemDamage()).getName();
	}
	
	@Override
    public int getMetadata(int damage) {
        return damage;
    }
}
