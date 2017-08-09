package com.jozufozu.exnihiloomnia.common.registries.recipes;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.jozufozu.exnihiloomnia.common.lib.LibRegistries;
import com.jozufozu.exnihiloomnia.common.registries.JsonHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class FluidCraftingRecipe extends IForgeRegistryEntry.Impl<FluidCraftingRecipe>
{
    /**
     * The fluid that is in the barrel
     */
    private FluidStack fluid;
    
    /**
     * The item required to trigger the crafting
     */
    private Ingredient catalyst;
    
    /**
     * The item that you get from crafting
     */
    private ItemStack result;
    
    public FluidStack getFluid()
    {
        return fluid;
    }
    
    public Ingredient getCatalyst()
    {
        return catalyst;
    }
    
    public ItemStack getResult()
    {
        return result;
    }
    
    public static FluidCraftingRecipe deserialize(JsonObject object) throws JsonParseException
    {
        if (!object.has(LibRegistries.INPUT_GENERIC))
        {
            throw new JsonSyntaxException("Fluid crafting recipe is missing input!");
        }
        
        if (!object.has(LibRegistries.OUTPUT_GENERIC))
        {
            throw new JsonSyntaxException("Fluid crafting recipe is missing output!");
        }
    
        FluidCraftingRecipe out = new FluidCraftingRecipe();
        
        out.catalyst = JsonHelper.deserializeIngredient(object.getAsJsonObject(LibRegistries.INPUT_GENERIC));
        
        out.fluid = new FluidStack(JsonHelper.deserializeFluid(object, LibRegistries.INPUT_FLUID), 1000);
        
        out.result = JsonHelper.deserializeItem(object.getAsJsonObject(LibRegistries.OUTPUT_GENERIC), true);
        
        return out;
    }
}
