package com.jozufozu.exnihiloomnia.common.items;

import com.google.common.collect.Sets;
import com.jozufozu.exnihiloomnia.ExNihilo;
import com.jozufozu.exnihiloomnia.common.util.IModelRegister;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemTool;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Set;

public class ItemBaseTool extends ItemTool implements IModelRegister
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
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels()
    {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(this.getRegistryName(), "inventory"));
    }
}
