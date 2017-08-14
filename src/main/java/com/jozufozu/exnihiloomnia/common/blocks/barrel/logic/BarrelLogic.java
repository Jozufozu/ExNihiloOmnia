package com.jozufozu.exnihiloomnia.common.blocks.barrel.logic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

public abstract class BarrelLogic
{
    /**
     * Called when a barrel state is set
     */
    public void onActivate(TileEntityBarrel barrel, @Nullable BarrelState previousState)
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
    public boolean canUseItem(TileEntityBarrel barrel, @Nullable EntityPlayer player, @Nullable EnumHand hand, ItemStack itemStack)
    {
        return false;
    }
    
    /**
     * This is called BEFORE an item is inserted into the barrel
     * @param player    can be null if the item is inserted by pipes!
     * @param hand      the hand holding the item
     * @param barrel    the barrel
     * @param itemStack the item to be checked
     * @return true if the state changed
     */
    public EnumInteractResult onUseItem(TileEntityBarrel barrel, @Nullable EntityPlayer player, @Nullable EnumHand hand, ItemStack itemStack)
    {
        return EnumInteractResult.PASS;
    }
    
    /**
     * @param barrel     the barrel being tested
     * @param fluidStack the fluid being tested
     * @return true if the fluid can be put in the barrel
     */
    public boolean canFillFluid(TileEntityBarrel barrel, FluidStack fluidStack)
    {
        return false;
    }
    
    /**
     * Preforms additional actions when a barrel is filled with fluid
     * Only called if {@link BarrelState#canInteractWithFluids(TileEntityBarrel)} returns true
     * @return true if the state changed. Will prevent the tank from being filled
     */
    public EnumInteractResult onFillFluid(TileEntityBarrel barrel, FluidStack fluidStack)
    {
        return EnumInteractResult.PASS;
    }
}
