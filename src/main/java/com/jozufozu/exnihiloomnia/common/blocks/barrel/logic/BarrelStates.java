package com.jozufozu.exnihiloomnia.common.blocks.barrel.logic;

import com.jozufozu.exnihiloomnia.ExNihilo;
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.states.*;
import net.minecraft.util.ResourceLocation;

public class BarrelStates
{
    public static final ResourceLocation ID_EMPTY = get("empty");
    public static final ResourceLocation ID_COMPOST_COLLECT = get("compost_collect");
    public static final ResourceLocation ID_COMPOSTING = get("composting");
    public static final ResourceLocation ID_FLUID = get("fluid");
    public static final ResourceLocation ID_ITEMS = get("items");
    
    public static BarrelState EMPTY;
    public static BarrelState COMPOST_COLLECT;
    public static BarrelState COMPOSTING;
    public static BarrelState FLUID;
    public static BarrelState ITEMS;
    
    public static void initializeBarrelStates()
    {
        BarrelState.STATES.clear();
        
        EMPTY = new BarrelStateEmpty();
        COMPOST_COLLECT = new BarrelStateCompostCollect();
        COMPOSTING = new BarrelStateComposting();
        ITEMS = new BarrelStateItem();
        FLUID = new BarrelStateFluid();
    }
    
    private static ResourceLocation get(String name)
    {
        return new ResourceLocation(ExNihilo.MODID, name);
    }
}
