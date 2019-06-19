package com.jozufozu.exnihiloomnia.common.items;

import com.google.common.collect.Sets;
import com.jozufozu.exnihiloomnia.ExNihilo;
import net.minecraft.block.Block;
import net.minecraft.item.ToolItem;
import net.minecraft.util.ResourceLocation;

import java.util.Set;

public class ItemBaseTool extends ToolItem
{
    public ItemBaseTool(ResourceLocation registryName, ToolMaterial toolMaterial)
    {
        this(registryName, toolMaterial, Sets.newHashSet());
    }
    
    public ItemBaseTool(ResourceLocation registryName, ToolMaterial toolMaterial, Set<Block> effectiveBlocksIn)
    {
        this(registryName, 0, 0, toolMaterial, effectiveBlocksIn);
    }
    
    public ItemBaseTool(ResourceLocation registryName, float damage, float attackSpeed, ToolMaterial toolMaterial, Set<Block> effectiveBlocksIn)
    {
        super(damage, attackSpeed, toolMaterial, effectiveBlocksIn);
        this.setRegistryName(registryName);
        this.setUnlocalizedName(ExNihilo.MODID + "." + registryName.getResourcePath());
        this.setCreativeTab(ExNihiloTabs.ITEMS);
        
        if (ExNihiloItems.hasRegisteredItems())
        {
            ExNihilo.log.warn("Item '" + registryName + "' created after initialization!");
        }
        
        ExNihiloItems.modItems.add(this);
    }
}
