package com.jozufozu.exnihiloomnia.common.items;

import com.jozufozu.exnihiloomnia.common.blocks.ExNihiloBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class ExNihiloTabs
{
    public static final CreativeTabs BLOCKS = new CreativeTabs("exnihiloomnia.blocks")
    {
        @Override
        public ItemStack getTabIconItem()
        {
            return new ItemStack(ExNihiloBlocks.SIEVE);
        }
    };
    
    public static final CreativeTabs BARRELS = new CreativeTabs("exnihiloomnia.barrels")
    {
        @Override
        public ItemStack getTabIconItem()
        {
            return new ItemStack(ExNihiloBlocks.BARREL_WOOD);
        }
    };
    
    public static final CreativeTabs ITEMS = new CreativeTabs("exnihiloomnia.items")
    {
        @Override
        public ItemStack getTabIconItem()
        {
            return new ItemStack(ExNihiloBlocks.SIEVE);
        }
    };
}
