package com.jozufozu.exnihiloomnia.common.items;

import com.jozufozu.exnihiloomnia.common.lib.LibItems;
import com.jozufozu.exnihiloomnia.common.world.SpawnIsland;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemJadeAstrolabe extends ItemBase
{
    public ItemJadeAstrolabe()
    {
        super(LibItems.ASTROLABE);
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        if (!worldIn.isRemote)
        {
            BlockPos position = playerIn.getPosition();
    
            int radius = 16;
  
            SpawnIsland island = SpawnIsland.createFromWorld(worldIn, position.add(-radius, -radius, -radius), position.add(radius, radius, radius), position);
            island.save(playerIn);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
