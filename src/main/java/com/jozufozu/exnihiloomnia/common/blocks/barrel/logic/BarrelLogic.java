package com.jozufozu.exnihiloomnia.common.blocks.barrel.logic;

import com.jozufozu.exnihiloomnia.common.blocks.barrel.TileEntityBarrel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class BarrelLogic
{
    /**
     * Called when a barrel state is set
     *
     * @param barrel the fresh new barrel
     */
    public void onActivate(TileEntityBarrel barrel)
    {
    
    }
    
    /**
     * @param barrel the barrel being ticked
     * @return true if the barrel state changed
     */
    public boolean onUpdate(TileEntityBarrel barrel)
    {
        return false;
    }
    
    /**
     * @param barrel    the barrel being tested
     * @param itemStack the item being tested
     * @return true if item can be used on barrel
     */
    public boolean canUseItem(@Nonnull TileEntityBarrel barrel, @Nullable EntityPlayer player, EnumHand hand, @Nonnull ItemStack itemStack)
    {
        return false;
    }
    
    /**
     * @param player    can be null if the item is inserted by pipes!
     * @param hand      the hand holding the item
     * @param barrel    the barrel
     * @param itemStack the item to be checked
     */
    public void useItem(@Nonnull TileEntityBarrel barrel, @Nullable EntityPlayer player, EnumHand hand, @Nonnull ItemStack itemStack)
    {

    }
}
