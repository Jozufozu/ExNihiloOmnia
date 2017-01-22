package exnihiloomnia.blocks.barrels.states.compost;

import exnihiloomnia.blocks.barrels.renderer.BarrelRenderer;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import exnihiloomnia.util.enums.EnumMetadataBehavior;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class BarrelStateGrass extends BarrelStateCompostSpecial {
	private static final ItemStack grass = new ItemStack(Blocks.GRASS, 1);
	
	public BarrelStateGrass() {
		super();
		
		addIngredient(new ItemStack(Items.GOLDEN_APPLE), EnumMetadataBehavior.IGNORED);
	}
	
	@Override
	public String getUniqueIdentifier() {
		return "barrel.grass";
	}
	
	@Override
	protected void renderBlockTexture(TileEntityBarrel barrel) {
		double timer = barrel.getTimerStatus();

		if (timer > 0.0d) {
			BarrelRenderer.renderContentsFromItemStack(grass);
		}
	}
}
