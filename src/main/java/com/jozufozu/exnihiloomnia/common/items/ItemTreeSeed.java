package com.jozufozu.exnihiloomnia.common.items;

import com.jozufozu.exnihiloomnia.ExNihilo;
import com.jozufozu.exnihiloomnia.common.lib.LibItems;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTreeSeed extends ItemBase
{
    @GameRegistry.ObjectHolder("minecraft:sapling")
    public static Block sapling;
    
    public ItemTreeSeed()
    {
        super(LibItems.TREE_SEED);
        this.setHasSubtypes(true);
    }
    
    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if (this.isInCreativeTab(tab))
        {
            for (BlockPlanks.EnumType enumType : BlockPlanks.EnumType.values())
            {
                items.add(new ItemStack(this, 1, enumType.getMetadata()));
            }
        }
    }
    
    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName(stack) + "." + BlockPlanks.EnumType.byMetadata(stack.getMetadata()).getUnlocalizedName();
    }
    
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        Block block = iblockstate.getBlock();
        
        if(!block.isReplaceable(worldIn, pos))
        {
            pos = pos.offset(facing);
        }
    
        ItemStack itemstack = player.getHeldItem(hand);
        
        if (!itemstack.isEmpty() && player.canPlayerEdit(pos, facing, itemstack) && worldIn.mayPlace(sapling, pos, false, facing, null))
        {
            IBlockState iblockstate1 = sapling.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, itemstack.getMetadata(), player, hand);
            
            if (this.placeBlockAt(itemstack, player, worldIn, pos, facing, hitX, hitY, hitZ, iblockstate1))
            {
                iblockstate1 = worldIn.getBlockState(pos);
                SoundType soundtype = iblockstate1.getBlock().getSoundType(iblockstate1, worldIn, pos, player);
                worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                itemstack.shrink(1);
            }
        
            return EnumActionResult.SUCCESS;
        }
        else
        {
            return EnumActionResult.FAIL;
        }
    }
    
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState)
    {
        if (!world.setBlockState(pos, newState, 11)) return false;
        
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() == sapling)
        {
            sapling.onBlockPlacedBy(world, pos, state, player, stack);
            
            if (player instanceof EntityPlayerMP)
                CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos, stack);
        }
        
        return true;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels()
    {
        for (BlockPlanks.EnumType enumType : BlockPlanks.EnumType.values())
        {
            ModelLoader.setCustomModelResourceLocation(this, enumType.getMetadata(), new ModelResourceLocation(ExNihilo.MODID + ":seed_" + enumType.getUnlocalizedName(), "inventory"));
        }
    }
}
