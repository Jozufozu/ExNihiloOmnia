package com.jozufozu.exnihiloomnia.common.items;

import com.jozufozu.exnihiloomnia.ExNihilo;
import com.jozufozu.exnihiloomnia.common.util.IModelRegister;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBase extends Item implements IModelRegister
{
    public ItemBase(ResourceLocation registryName)
    {
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
