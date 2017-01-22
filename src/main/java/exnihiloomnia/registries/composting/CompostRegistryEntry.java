package exnihiloomnia.registries.composting;

import exnihiloomnia.registries.composting.pojos.CompostRecipe;
import exnihiloomnia.util.Color;
import exnihiloomnia.util.enums.EnumMetadataBehavior;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class CompostRegistryEntry {
	private static final Color DEFAULT_COLOR = new Color(1f, 1f, 1f, 1f);
	
	private final Color color;
	private final ItemStack input;
	private final int volume;
	private EnumMetadataBehavior behavior = EnumMetadataBehavior.SPECIFIC;
	
	public CompostRegistryEntry(ItemStack input, int value, Color color) {
		this.input = input;
		this.color = color;
		this.volume = value;
	}

	public CompostRegistryEntry(ItemStack input, int value, Color color, EnumMetadataBehavior behavior) {
		this(input, value, color);
		
		this.behavior = behavior;
	}
	
	public static CompostRegistryEntry fromRecipe(CompostRecipe recipe) {
		Item item = Item.REGISTRY.getObject(new ResourceLocation(recipe.getId()));
		Color color = new Color(recipe.getColor());
		
		if (item != null) {
			ItemStack input = new ItemStack(item, 1, recipe.getMeta()); 
			return new CompostRegistryEntry(input, recipe.getValue(), color, recipe.getBehavior());
		}
		else {
			return null;
		}
	}

	public CompostRecipe toRecipe() {
		ResourceLocation item = Item.REGISTRY.getNameForObject(input.getItem());
		if (item == null)
			item = Block.REGISTRY.getNameForObject(Block.getBlockFromItem(input.getItem()));

		return new CompostRecipe(item.toString(), getInput().getMetadata(), getMetadataBehavior(), getVolume(), color.toString());
	}
	
	public Color getColor() {
		if (this.color != null)
			return this.color;
		else
			return DEFAULT_COLOR;
	}
	
	public ItemStack getInput() {
		return input;
	}
	
	public int getVolume() {
		if (this.volume > 1000)
			return 1000;
		
		if (this.volume < 0)
			return 0;
		
		return this.volume;
	}
	
	public EnumMetadataBehavior getMetadataBehavior() {
		return this.behavior;
	}

	public String getKey() {
		ResourceLocation s = Item.REGISTRY.getNameForObject(input.getItem());

		if (s != null) {
            if (behavior == EnumMetadataBehavior.IGNORED) {
                return s.toString() + ":*";
            }
            else {
                return s.toString() + ":" + input.getMetadata();
            }
        }
        else
        	return null;
	}
}
