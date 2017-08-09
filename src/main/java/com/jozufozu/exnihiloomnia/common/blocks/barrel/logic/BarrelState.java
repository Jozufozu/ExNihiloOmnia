package com.jozufozu.exnihiloomnia.common.blocks.barrel.logic;

import com.jozufozu.exnihiloomnia.common.blocks.barrel.TileEntityBarrel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BarrelState
{
    protected NonNullList<BarrelLogic> logic = NonNullList.create();
    
    public boolean canInteractWithFluids(TileEntityBarrel barrel)
    {
        return false;
    }
    
    public boolean canInteractWithItems(TileEntityBarrel barrel)
    {
        return false;
    }
    
    public void update(TileEntityBarrel barrel)
    {
        for (BarrelLogic barrelLogic : logic)
        {
            if (barrelLogic.onUpdate(barrel))
            {
                return;
            }
        }
    }
    
    public boolean canUseItem(@Nonnull TileEntityBarrel barrel, @Nullable EntityPlayer player, EnumHand hand, @Nonnull ItemStack itemStack)
    {
        for (BarrelLogic barrelLogic : logic)
        {
            if (barrelLogic.canUseItem(barrel, player, hand, itemStack))
            {
                return true;
            }
        }
        
        return false;
    }
    
    public void useItem(@Nonnull TileEntityBarrel barrel, @Nullable EntityPlayer player, EnumHand hand, @Nonnull ItemStack itemStack)
    {
        for (BarrelLogic barrelLogic : logic)
        {
            barrelLogic.useItem(barrel, player, hand, itemStack);
        }
    }
}
