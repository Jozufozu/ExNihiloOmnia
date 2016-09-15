package exnihiloomnia.items.misc;

import exnihiloomnia.items.ENOItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemSpores extends Item {

	public ItemSpores() {
		super();

		this.setCreativeTab(ENOItems.ENO_TAB);
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (worldIn.getBlockState(pos).getBlock() == Blocks.DIRT) {
			worldIn.setBlockState(pos, Blocks.MYCELIUM.getDefaultState(), 2);
			worldIn.playSound(null, pos, SoundEvents.BLOCK_GRASS_HIT, SoundCategory.BLOCKS, 0.3f, 1.5f);
			
			if (!playerIn.isCreative()) {
				stack.stackSize--;
			}
			
			return EnumActionResult.SUCCESS;
		}
		
		return EnumActionResult.PASS;
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack item, EntityPlayer player, EntityLivingBase entity, EnumHand hand) {
		if (!entity.worldObj.isRemote && entity instanceof EntityCow) {
			entity.setDead();
			EntityMooshroom mooshroom = new EntityMooshroom(entity.worldObj);
			mooshroom.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
			mooshroom.setHealth(entity.getHealth());
			mooshroom.renderYawOffset = entity.renderYawOffset;
			entity.worldObj.spawnEntityInWorld(mooshroom);
			entity.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, entity.posX, entity.posY + (double)(entity.height / 2.0F), entity.posZ, 0.0D, 0.0D, 0.0D);

			if (!player.isCreative()) {
				item.stackSize--;
			}
			
			return true;
		}
		
		return false;
	}
}
