package com.jozufozu.exnihiloomnia.common.items.tools;

import com.jozufozu.exnihiloomnia.common.items.ItemBaseTool;
import com.jozufozu.exnihiloomnia.common.registries.JsonHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemMesh extends ItemBaseTool implements IMesh
{
    private HashMap<String, Float> effectiveness;
    
    public ItemMesh(ResourceLocation registryName, ToolMaterial material)
    {
        super(registryName, material);
        
        effectiveness = JsonHelper.mesh.get(registryName.getResourcePath());
        
        if (effectiveness == null)
            effectiveness = new HashMap<>();
    }
    
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        super.addInformation(stack, worldIn, tooltip, flagIn);
    
        tooltip.add("Effective on:");
        for (Map.Entry<String, Float> stringFloatEntry : effectiveness.entrySet())
        {
            tooltip.add(stringFloatEntry.getKey() + ": " + stringFloatEntry.getValue());
        }
    }
    
    @Override
    public float getEffectivenessForType(String type)
    {
        if (!effectiveness.containsKey(type))
            return 1.0f;
        
        return effectiveness.get(type);
    }
}
