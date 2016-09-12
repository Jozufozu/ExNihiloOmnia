package exnihiloomnia.blocks.barrels.states.compost.logic;

import exnihiloomnia.blocks.barrels.architecture.BarrelLogic;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import exnihiloomnia.registries.composting.CompostRegistry;
import exnihiloomnia.registries.composting.CompostRegistryEntry;
import exnihiloomnia.util.Color;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;

public class CompostStateLogicItems extends BarrelLogic{
	
	@Override
	public boolean canUseItem(TileEntityBarrel barrel, ItemStack item) {
		return  (CompostRegistry.isCompostable(item) && barrel.getVolume() < barrel.getVolumeMax());
	}
	
	@Override
	public boolean onUseItem(EntityPlayer player, EnumHand hand, TileEntityBarrel barrel, ItemStack item) {
		CompostRegistryEntry recipe = CompostRegistry.getEntryForItemStack(item);
		
		if (recipe != null) {
			barrel.setVolume(barrel.getVolume() + recipe.getVolume());
			
			//Set the new color.
			Color colorA = recipe.getColor();
			Color colorB = barrel.getColor();
			
			float weightA = (float)recipe.getVolume() / (float)barrel.getVolume();
			float weightB = 1.0f - weightA;

			float r = weightA * colorA.r + weightB * colorB.r;
			float g = weightA * colorA.g + weightB * colorB.g;
			float b = weightA * colorA.b + weightB * colorB.b;
			float a = weightA * colorA.a + weightB * colorB.a;

			barrel.setColor(new Color(r,g,b,a));
			barrel.requestSync();
		}
		
		consumeItem(player, item);
		barrel.getWorld().playSound(null, barrel.getPos(), SoundEvents.BLOCK_SNOW_STEP, SoundCategory.BLOCKS, 0.7f, 0.15f);
		
		return false;
	}
	
	@Override
	public boolean onUpdate(TileEntityBarrel barrel) {
		if (barrel.getVolume() == barrel.getVolumeMax() && barrel.getTimerStatus() == -1)
			barrel.startTimer(1000);

		return false;
	}
}
