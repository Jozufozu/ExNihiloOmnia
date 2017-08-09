package com.jozufozu.exnihiloomnia.common.registries.recipes;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.jozufozu.exnihiloomnia.common.lib.LibRegistries;
import com.jozufozu.exnihiloomnia.common.registries.JsonHelper;
import com.jozufozu.exnihiloomnia.common.registries.ingredients.WorldIngredient;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class FermentingRecipe extends IForgeRegistryEntry.Impl<FermentingRecipe>
{
    private WorldIngredient block;
    private FluidStack toFerment;
    private FluidStack output;
    
    public static FermentingRecipe deserialize(JsonObject object) throws JsonParseException
    {
        if (!object.has(LibRegistries.INPUT_BLOCK))
        {
            throw new JsonSyntaxException("Fermenting recipe is missing block input!");
        }
        
        FermentingRecipe out = new FermentingRecipe();
        
        out.block = WorldIngredient.deserialize(object.getAsJsonObject(LibRegistries.INPUT_BLOCK));
        
        out.toFerment = new FluidStack(JsonHelper.deserializeFluid(object, LibRegistries.INPUT_FLUID), 1000);
        out.output = new FluidStack(JsonHelper.deserializeFluid(object, LibRegistries.OUTPUT_FLUID), 1000);
        
        return out;
    }
}
