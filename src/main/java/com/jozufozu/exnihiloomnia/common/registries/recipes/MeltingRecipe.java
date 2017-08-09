package com.jozufozu.exnihiloomnia.common.registries.recipes;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.jozufozu.exnihiloomnia.common.blocks.crucible.TileEntityCrucible;
import com.jozufozu.exnihiloomnia.common.lib.LibRegistries;
import com.jozufozu.exnihiloomnia.common.registries.JsonHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class MeltingRecipe extends IForgeRegistryEntry.Impl<MeltingRecipe>
{
    /**
     * The minimum heat level required for this to melt.
     * Every heat level over the Crucible is will add 1x to the speed multiplier
     */
    private int requiredHeat;
    
    private Ingredient input;
    
    private int inputVolume;
    
    private FluidStack output;
    
    public int getRequiredHeat()
    {
        return requiredHeat;
    }
    
    public int getInputVolume()
    {
        return inputVolume;
    }
    
    public FluidStack getOutput()
    {
        return output.copy();
    }
    
    public boolean matches(ItemStack input)
    {
        return this.input.apply(input);
    }
    
    public static MeltingRecipe deserialize(JsonObject object) throws JsonParseException
    {
        MeltingRecipe out = new MeltingRecipe();
    
        if (!object.has(LibRegistries.INPUT_GENERIC))
        {
            throw new JsonSyntaxException("Melting recipe is missing input!");
        }
    
        JsonObject input = object.getAsJsonObject(LibRegistries.INPUT_GENERIC);
        
        out.input = JsonHelper.deserializeIngredient(input);
        out.inputVolume = JsonUtils.getInt(input, LibRegistries.VOLUME, 1000);
        
        out.requiredHeat = JsonUtils.getInt(object, LibRegistries.HEAT, 1);
        out.output = new FluidStack(JsonHelper.deserializeFluid(object, LibRegistries.OUTPUT_FLUID), JsonUtils.getInt(object, LibRegistries.VOLUME, 1000));
    
        if (out.inputVolume <= 0 || out.inputVolume > TileEntityCrucible.crucibleCapacity)
        {
            throw new JsonSyntaxException("Melting recipe has invalid inputVolume, expected range: 1 - " + TileEntityCrucible.crucibleCapacity);
        }
        if (out.output.amount <= 0 || out.output.amount > TileEntityCrucible.crucibleCapacity)
        {
            throw new JsonSyntaxException("Melting recipe has invalid outputVolume, expected range: 1 - " + TileEntityCrucible.crucibleCapacity);
        }
        
        return out;
    }
}
