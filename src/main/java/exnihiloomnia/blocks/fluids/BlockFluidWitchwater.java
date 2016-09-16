package exnihiloomnia.blocks.fluids;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.SkeletonType;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidWitchwater extends BlockFluidClassic {

	public BlockFluidWitchwater(Fluid fluid, Material material) {
		super(fluid, material);
	}

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
		if(!world.isRemote && !entity.isDead) {
			if (entity instanceof EntityVillager) {
				EntityVillager villager = (EntityVillager) entity;
				
				if (world.getDifficulty() != EnumDifficulty.PEACEFUL) {
					if (!villager.isChild()) {
						villager.setDead();
						
						EntityWitch witch = new EntityWitch(world);
						witch.setLocationAndAngles(villager.posX, villager.posY, villager.posZ, villager.rotationYaw, villager.rotationPitch);
						witch.renderYawOffset = villager.renderYawOffset;
						witch.setHealth(villager.getHealth());
						
						world.spawnEntityInWorld(witch);
					}
					else {
						villager.setDead();
						
						EntityZombie zombie = new EntityZombie(world);
						zombie.setLocationAndAngles(villager.posX, villager.posY, villager.posZ, villager.rotationYaw, villager.rotationPitch);
						zombie.renderYawOffset = villager.renderYawOffset;
						zombie.setHealth(villager.getHealth());
						zombie.setVillagerType(villager.getProfessionForge());
						zombie.setChild(villager.isChild());
						
						world.spawnEntityInWorld(zombie);
					}
				}
				else {
					villager.onStruckByLightning(null);
				}
			}
			
			if (entity instanceof EntitySkeleton) {
				if (((EntitySkeleton) entity).func_189771_df() == SkeletonType.NORMAL) {
					EntitySkeleton skeleton = (EntitySkeleton) entity;
					skeleton.func_189768_a(SkeletonType.WITHER);
					skeleton.setHealth(skeleton.getMaxHealth());
				}
			}
			
			if (entity instanceof EntityCreeper) {
				if (!((EntityCreeper) entity).getPowered()) {
					EntityCreeper creeper = (EntityCreeper) entity;
					creeper.onStruckByLightning(null);
					creeper.setHealth(creeper.getMaxHealth());
				}
			}
			
			if (entity instanceof EntitySpider && !(entity instanceof EntityCaveSpider)) {
				EntitySpider spider = (EntitySpider) entity;
				spider.setDead();
				
				EntityCaveSpider caveSpider = new EntityCaveSpider(world);
				caveSpider.setLocationAndAngles(spider.posX, spider.posY, spider.posZ, spider.rotationYaw, spider.rotationPitch);
				caveSpider.renderYawOffset = spider.renderYawOffset;
				caveSpider.setHealth(caveSpider.getMaxHealth());
				
				world.spawnEntityInWorld(caveSpider);
			}
			
			if (entity instanceof EntitySquid) {
				EntitySquid squid = (EntitySquid) entity;
				squid.setDead();
				
				EntityGhast ghast = new EntityGhast(world);
				ghast.setLocationAndAngles(squid.posX, squid.posY + 2, squid.posZ, squid.rotationYaw, squid.rotationPitch);
				ghast.renderYawOffset = squid.renderYawOffset;
				ghast.setHealth(ghast.getMaxHealth());
				
				world.spawnEntityInWorld(ghast);
			}
			
			if(entity instanceof EntityAnimal) {
				EntityAnimal animal = (EntityAnimal) entity;
				animal.onStruckByLightning(null);
			}
			
			if(entity instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) entity;
				player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 210, 0));
				player.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 210, 2));
				player.addPotionEffect(new PotionEffect(MobEffects.WITHER, 210, 0));
				player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 210, 0));
			}
		}
	}
}
