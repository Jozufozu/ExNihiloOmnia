package com.jozufozu.exnihiloomnia.common.registries;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.jozufozu.exnihiloomnia.common.lib.LibRegistries;
import com.jozufozu.exnihiloomnia.common.registries.ingredients.WorldIngredient;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class HeatSource extends IForgeRegistryEntry.Impl<HeatSource>
{
    private int heatLevel;
    
    private WorldIngredient heatSource;
    
    public int getHeatLevel()
    {
        return heatLevel;
    }
    
    public boolean matches(IBlockState state)
    {
        return heatSource.test(state);
    }
    
    public static HeatSource deserialize(JsonObject object)
    {
        if (!object.has("source"))
        {
            throw new JsonSyntaxException("Heat source is missing block!");
        }
        
        HeatSource out = new HeatSource();
        
        out.heatLevel = JsonUtils.getInt(object, LibRegistries.HEAT);
        
        out.heatSource = WorldIngredient.deserialize(object.getAsJsonObject("source"));
        
        return out;
    }
}
