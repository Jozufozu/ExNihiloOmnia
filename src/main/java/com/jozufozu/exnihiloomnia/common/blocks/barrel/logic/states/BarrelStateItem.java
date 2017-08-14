package com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.states;

import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.BarrelState;
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.BarrelStates;
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.TileEntityBarrel;

public class BarrelStateItem extends BarrelState
{
    public BarrelStateItem()
    {
        super(BarrelStates.ID_ITEMS);
    }
    
    @Override
    public boolean canInteractWithFluids(TileEntityBarrel barrel)
    {
        return false;
    }
    
    @Override
    public boolean canExtractItems(TileEntityBarrel barrel)
    {
        return true;
    }
    
    @Override
    public void draw(TileEntityBarrel barrel, double x, double y, double z, float partialTicks)
    {
        super.draw(barrel, x, y, z, partialTicks);
        
        drawContents(barrel, x, y, z, partialTicks);
    }
}
