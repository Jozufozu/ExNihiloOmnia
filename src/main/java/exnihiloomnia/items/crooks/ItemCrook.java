package exnihiloomnia.items.crooks;

import exnihiloomnia.ENOConfig;
import exnihiloomnia.blocks.ENOBlocks;
import exnihiloomnia.items.ENOItems;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemCrook extends Item {
	public static final double pullingForce = 1d;
	public static final double pushingForce = 1d;

	private final Item.ToolMaterial material;

	public ItemCrook(Item.ToolMaterial material) {
		this.material = material;
		this.maxStackSize = 1;
		this.setMaxDamage((int)(material.getMaxUses() * 1.5));
		this.setCreativeTab(CreativeTabs.TOOLS);
	}

	@Override
	public boolean onLeftClickEntity(ItemStack item, EntityPlayer player, Entity entity) {
		if (!player.worldObj.isRemote) {
			double distance = Math.sqrt(Math.pow(player.posX - entity.posX, 2) + Math.pow(player.posZ - entity.posZ, 2));

			double scalarX = (player.posX - entity.posX) / distance;
			double scalarZ = (player.posZ - entity.posZ) / distance;

			double velX = 0 - scalarX * pushingForce;
			double velY = 0;
			double velZ = 0 - scalarZ * pushingForce;

			if (player.posY < entity.posY)
				velY = 0.5d;

			entity.addVelocity(velX, velY, velZ);
		}

		item.damageItem(1, player);

		return true; //to do no damage
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack item, EntityPlayer player, EntityLivingBase entity, EnumHand hand) {
		if (!player.worldObj.isRemote) {
			double distance = Math.sqrt(Math.pow(player.posX - entity.posX, 2) + Math.pow(player.posZ - entity.posZ, 2));

			double scalarX = (player.posX - entity.posX) / distance;
			double scalarZ = (player.posZ - entity.posZ) / distance;

			double velX = scalarX * pullingForce;
			double velY = 0;
			double velZ = scalarZ * pullingForce;

			if (player.posY > entity.posY)
				velY = 0.5d;

			entity.addVelocity(velX, velY, velZ);
		}

		item.damageItem(1, player);
		return true;
	}

	@Override
	public boolean canHarvestBlock(IBlockState block) {
		return block.getMaterial() == Material.LEAVES;
	}

	@Override
	public float getStrVsBlock(ItemStack item, IBlockState block) {
		if (block.getMaterial() == Material.LEAVES) {
			return material.getEfficiencyOnProperMaterial() + 1;
		}

		return 1.0F;
	}

	/*
	@Override
	public boolean onBlockStartBreak(ItemStack item, BlockPos pos, EntityPlayer player) {
		IBlockState block = player.worldObj.getBlockState(pos);
		
		if (!player.worldObj.isRemote) {
			if (block.getMaterial() == Material.LEAVES || block instanceof BlockTallGrass) {
				//Simulate a block break to cause the first round of items to drop.
				block.getBlock().dropBlockAsItem(player.worldObj, pos, player.worldObj.getBlockState(pos), EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, item));
				
				if ((block.getMaterial().equals(Material.LEAVES) && player.worldObj.rand.nextInt(100) == 0) || (block.getBlock() == ENOBlocks.INFESTED_LEAVES && player.worldObj.rand.nextFloat() < ENOConfig.silkworm_chnace)) {
					EntityItem silky = new EntityItem(player.worldObj, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, new ItemStack(ENOItems.SILKWORM));
					silky.setDefaultPickupDelay();
					player.worldObj.spawnEntityInWorld(silky);
				}
			}
		}

		//Returning false causes the leaves/grass to break as normal and causes items to drop a second time.
		return false;
	}
	*/

	@Override
	public boolean onBlockDestroyed(ItemStack item, World world, IBlockState block, BlockPos pos, EntityLivingBase player) {
		item.damageItem(1, player);
		return true;
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        ItemStack mat = this.material.getRepairItemStack();
        return mat != null && net.minecraftforge.oredict.OreDictionary.itemMatches(mat, repair, false) || super.getIsRepairable(toRepair, repair);
    }
}
