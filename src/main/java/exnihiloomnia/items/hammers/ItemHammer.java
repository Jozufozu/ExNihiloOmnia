package exnihiloomnia.items.hammers;

import com.google.common.collect.Sets;
import exnihiloomnia.registries.hammering.HammerRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Set;

public class ItemHammer extends ItemTool {
	private final static Set<Block> EMPTY_SET = Sets.newHashSet(new Block[]{});

	public ItemHammer(ToolMaterial material) {
		super(1.0f, 0f, material, EMPTY_SET);
		this.maxStackSize = 1;
		this.setMaxDamage(material.getMaxUses());
		this.setCreativeTab(CreativeTabs.TOOLS);
	}

	/*
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
		if (CrookRegistry.isHammerable(state) && canHarvestBlock(state)) {
			if (!world.isRemote) {
				world.setBlockToAir(pos);
				CrookRegistry.getEntryForBlockState(state).dropRewards((EntityPlayer) entityLiving, pos);
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
				|| block.getBlock().getHarvestLevel(block.getBlock().getDefaultState()) <= this.toolMaterial.getHarvestLevel());
    }
	
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        ItemStack mat = this.toolMaterial.getRepairItemStack();
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
