package com.jozufozu.exnihiloomnia.common.blocks;

import com.jozufozu.exnihiloomnia.ExNihilo;
import com.jozufozu.exnihiloomnia.common.items.ExNihiloItems;
import com.jozufozu.exnihiloomnia.common.items.ExNihiloTabs;
import com.jozufozu.exnihiloomnia.common.util.IItemBlockHolder;
import com.jozufozu.exnihiloomnia.common.util.IModelRegister;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBase extends Block implements IItemBlockHolder, IModelRegister
{
    public BlockBase(ResourceLocation registryName, Material materialIn)
    {
        this(registryName, materialIn, SoundType.STONE);
    }
    
    public BlockBase(ResourceLocation registryName, Material materialIn, SoundType soundType)
    {
        super(materialIn);
        
        this.setSoundType(soundType);
        this.setRegistryName(registryName);
        this.setUnlocalizedName(ExNihilo.MODID + "." + registryName.getResourcePath());
        this.setCreativeTab(ExNihiloTabs.BLOCKS);
        
        if (ExNihiloBlocks.hasRegisteredBlocks())
            ExNihilo.log.warn("Tried to make a block " + registryName + " after registering!");
        
        ExNihiloBlocks.modBlocks.add(this);
    }
    
    @Override
    public ItemBlock getItemBlock()
    {
        if (ExNihiloItems.hasRegisteredItems())
            return (ItemBlock) Item.getItemFromBlock(this);
        
        ItemBlock itemBlock = new ItemBlock(this);
        itemBlock.setRegistryName(getRegistryName());
        return itemBlock;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels()
    {
        ModelLoader.setCustomModelResourceLocation(getItemBlock(), 0, new ModelResourceLocation(this.getRegistryName(), "inventory"));
    }
}
