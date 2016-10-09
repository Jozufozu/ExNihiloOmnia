package exnihiloomnia.items.hammers;

import java.util.Set;

import com.google.common.collect.Sets;

import exnihiloomnia.registries.hammering.HammerRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraftforge.oredict.OreDictionary;

public class ItemHammer extends ItemTool {
	private final static Set<Block> EMPTY_SET = Sets.newHashSet(new Block[]{});
	private final Item.ToolMaterial material;
	
	public ItemHammer(ToolMaterial material) {
		super(1.0f, 0f, material, EMPTY_SET);
		this.material = material;
		this.maxStackSize = 1;
		this.setMaxDamage(material.getMaxUses());
		this.setCreativeTab(CreativeTabs.TOOLS);
	}

	/*
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
		if (HammerRegistry.isHammerable(state) && canHarvestBlock(state)) {
			if (!world.isRemote) {
				world.setBlockToAir(pos);
				HammerRegistry.getEntryForBlockState(state).dropRewards((EntityPlayer) entityLiving, pos);
			}
			
			stack.damageItem(1, entityLiving);
			return true;
		}
		
		stack.damageItem(1, entityLiving);
		return false;
	}
	*/
	
	@Override
	public boolean canHarvestBlock(IBlockState block) {
		return (block.getMaterial().isToolNotRequired()
				|| block.getBlock().getHarvestLevel(block.getBlock().getDefaultState()) <= this.material.getHarvestLevel());
    }
	
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        ItemStack mat = this.material.getRepairItemStack();
        return mat != null && OreDictionary.itemMatches(mat, repair, false) || super.getIsRepairable(toRepair, repair);
    }

    @Override
	public float getStrVsBlock(ItemStack stack, IBlockState state) {
		if (HammerRegistry.isHammerable(state))
            return efficiencyOnProperMaterial;
        else
            return 1;
	}
}
