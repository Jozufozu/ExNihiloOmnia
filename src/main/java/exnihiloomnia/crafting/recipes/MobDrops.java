package exnihiloomnia.crafting.recipes;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

import java.util.Random;

public class MobDrops {
	private static final Random rand = new Random();
	
	public static void onMobDeath(LivingDropsEvent event) {
		if(event.getSource().getSourceOfDamage() instanceof EntityPlayer) {
			if (event.getEntity() instanceof EntityCreeper) {
				if (rand.nextInt(40) == 0) {
					EntityItem entityitem = createMobDrop(event.getEntity(), new ItemStack(Blocks.CACTUS, 1));
					
					event.getDrops().add(entityitem);
				}
			}
		}
	}
	
	public static EntityItem createMobDrop(Entity entity, ItemStack item) {
		EntityItem entityitem = new EntityItem(entity.getEntityWorld(), 
				entity.getPosition().getX() + 0.5f, 
				entity.getPosition().getY() + 0.5f, 
				entity.getPosition().getZ() + 0.5f,
				item);

		entityitem.motionX = rand.nextGaussian() * 0.05F;
		entityitem.motionY = (0.2d);
		entityitem.motionZ = rand.nextGaussian() * 0.05F;
		entityitem.setDefaultPickupDelay();
		
		return entityitem;
	}
}
