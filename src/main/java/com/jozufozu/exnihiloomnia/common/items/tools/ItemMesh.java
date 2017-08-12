package com.jozufozu.exnihiloomnia.common.items.tools;

import com.google.gson.JsonElement;
import com.jozufozu.exnihiloomnia.ExNihilo;
import com.jozufozu.exnihiloomnia.common.items.ItemBaseTool;
import com.jozufozu.exnihiloomnia.common.registries.RegistryLoader;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ItemMesh extends ItemBaseTool implements IMesh
{
    public static HashMap<String, HashMap<String, Float>> masterTable;
    private HashMap<String, Float> effectiveness;
    
    public ItemMesh(ResourceLocation registryName, ToolMaterial material)
    {
        super(registryName, material);
        
        effectiveness = masterTable.get(registryName.getResourcePath());
        
        if (effectiveness == null)
            effectiveness = new HashMap<>();
    }
    
    public static void loadMeshTable()
    {
        RegistryLoader.loadSingleJson("/registries/mesh.json", mesh -> {
            HashMap<String, HashMap<String, Float>> table = new HashMap<>();
        
            if (mesh != null)
            {
                Set<Map.Entry<String, JsonElement>> entries = mesh.entrySet();
            
                for (Map.Entry<String, JsonElement> tools : entries)
                {
                    JsonElement toolsValue = tools.getValue();
                    if (!toolsValue.isJsonObject()) continue;
                
                    String toolName = tools.getKey();
                
                    HashMap<String, Float> effectives = new HashMap<>();
                
                    for (Map.Entry<String, JsonElement> effectiveness : toolsValue.getAsJsonObject().entrySet())
                    {
                        String type = effectiveness.getKey();
                    
                        try
                        {
                            float aFloat = effectiveness.getValue().getAsJsonPrimitive().getAsFloat();
                            effectives.put(type, aFloat);
                        } catch (Exception e)
                        {
                            ExNihilo.log.error("Could not read effectiveness for '" + toolName + "', '" + type + "'");
                        }
                    }
                    table.put(toolName, effectives);
                }
            }
        
            ItemMesh.masterTable = table;
        });
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
