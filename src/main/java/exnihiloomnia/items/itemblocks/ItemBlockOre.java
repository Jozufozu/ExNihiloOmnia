package exnihiloomnia.items.itemblocks;

import exnihiloomnia.registries.ore.Ore;
import exnihiloomnia.registries.ore.OreRegistry;
import exnihiloomnia.util.enums.EnumOreBlockType;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
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
	    return "tile.ore_" + EnumOreBlockType.fromMetadata(stack.getItemDamage()).getName() + "." + super.getUnlocalizedName().substring(5);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		EnumOreBlockType type = EnumOreBlockType.fromMetadata(stack.getItemDamage());
		String display = super.getItemStackDisplayName(stack);

		if (display.equals(getUnlocalizedName(stack) + ".name")) {
			Ore ore = OreRegistry.getOre(stack.getItem());
			display = I18n.format("tile.ore_template." + type.getName() + ".name", ore.getOreDictName(""));
		}

		return display;
	}
	
	@Override
    public int getMetadata(int damage) {
        return damage;
    }
}
