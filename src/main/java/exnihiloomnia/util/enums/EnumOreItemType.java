package exnihiloomnia.util.enums;

import exnihiloomnia.blocks.ENOBlocks;
import exnihiloomnia.items.ENOItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.IStringSerializable;

public enum EnumOreItemType implements IStringSerializable{
	BROKEN("broken", ENOBlocks.ORE_GRAVEL, ENOItems.BROKEN_ORE),
	BROKEN_NETHER("broken_nether", ENOBlocks.ORE_GRAVEL_NETHER, ENOItems.BROKEN_ORE_NETHER),
	BROKEN_ENDER("broken_ender", ENOBlocks.ORE_GRAVEL_ENDER, ENOItems.BROKEN_ORE_ENDER),
	CRUSHED("crushed", ENOBlocks.ORE_SAND, ENOItems.CRUSHED_ORE),
	POWDERED("powder", ENOBlocks.ORE_DUST, ENOItems.POWDERED_ORE),
	INGOT("ingot", null, ENOItems.INGOT_ORE);

	private final String name;
    //which block this coresponds to
    private final Block block;
	private final Item item;

	EnumOreItemType(String name, Block block, Item item) {
		this.name = name;
        this.block = block;
		this.item = item;
	}

	@Override
	public String getName() {
		return this.name;
	}

	public Block getBlock() {
		return this.block;
	}
	
	public Item getItem() {
		return this.item;
	}

}
