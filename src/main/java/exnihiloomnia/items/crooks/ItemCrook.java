package exnihiloomnia.items.crooks;

import com.google.common.collect.Sets;
import exnihiloomnia.blocks.ENOBlocks;
import exnihiloomnia.registries.crook.CrookRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Set;

public class ItemCrook extends ItemTool {
	private final static Set<Block> EMPTY_SET = Sets.newHashSet(new Block[]{});
	public static final double pullingForce = 1d;
	public static final double pushingForce = 1d;

	public ItemCrook(Item.ToolMaterial material) {
		super(0f, 0f, material, EMPTY_SET);
		this.maxStackSize = 1;
		this.setMaxDamage((int)(material.getMaxUses() * 1.5));
		this.setCreativeTab(CreativeTabs.TOOLS);
	}

	@Override
	public int getItemEnchantability() {
		return this.toolMaterial.getEnchantability();
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
			return toolMaterial.getEfficiencyOnProperMaterial() + 1;
		}

		return 1.0F;
	}

	@Override
	public boolean onBlockStartBreak(ItemStack item, BlockPos pos, EntityPlayer player) {
		IBlockState block = player.worldObj.getBlockState(pos);
		World world = player.worldObj;
		
		if (!world.isRemote) {
			if (CrookRegistry.isCrookable(block)) {
				//Simulate a block break to cause the first round of items to drop.
				int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, item);
				List<ItemStack> items = block.getBlock().getDrops(world, pos, block, fortune);

				for (ItemStack i : items) {
					if (world.rand.nextFloat() <= 1)
						Block.spawnAsEntity(world, pos, i);
				}

				if (!ENOBlocks.INFESTED_LEAVES.equals(block.getBlock()))
					CrookRegistry.getEntryForBlockState(block).dropRewards(world, item, pos);
			}
		}

		//Returning false causes the leaves/grass to break as normal and causes items to drop a second time.
		return false;
	}


	@Override
	public boolean onBlockDestroyed(ItemStack item, World world, IBlockState block, BlockPos pos, EntityLivingBase player) {
		item.damageItem(1, player);
		return true;
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        ItemStack mat = this.toolMaterial.getRepairItemStack();
        return mat != null && net.minecraftforge.oredict.OreDictionary.itemMatches(mat, repair, false) || super.getIsRepairable(toRepair, repair);
    }
}
