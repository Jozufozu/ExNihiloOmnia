package com.jozufozu.exnihiloomnia.common.registries.recipes;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.jozufozu.exnihiloomnia.common.lib.LibRegistries;
import com.jozufozu.exnihiloomnia.common.registries.JsonHelper;
import com.jozufozu.exnihiloomnia.common.registries.WeightedRewards;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class SieveRecipe extends IForgeRegistryEntry.Impl<SieveRecipe>
{
    /**
     * What you have to put in the sieve
     */
    private Ingredient input;
    
    /**
     * The amount of time in ticks it takes to sift the input.
     * Default is 40 ticks, or 2 seconds
     */
    private int siftTime = 40;
    
    /**
     * What could drop when fully processed.
     */
    private WeightedRewards output;
    
    private SieveRecipe() {}
    
    public SieveRecipe(Ingredient input, int siftTime, WeightedRewards output)
    {
        this.input = input;
        this.siftTime = siftTime;
        this.output = output;
    }
    
    public Ingredient getInput()
    {
        return input;
    }
    
    public int getSiftTime()
    {
        return siftTime;
    }
    
    public WeightedRewards getOutput()
    {
        return output;
    }
    
    public boolean matches(ItemStack input)
    {
        return this.input.apply(input);
    }
    
    public static SieveRecipe deserialize(JsonObject object) throws JsonParseException
    {
        SieveRecipe recipe = new SieveRecipe();
        
        if (!object.has(LibRegistries.INPUT_BLOCK))
        {
            throw new JsonSyntaxException("Sieve recipe has no input!");
        }
    
        JsonObject input = object.getAsJsonObject(LibRegistries.INPUT_BLOCK);
    
        recipe.input = JsonHelper.deserializeBlockIngredient(input);
        recipe.siftTime = JsonUtils.getInt(object, LibRegistries.TIME, 80);

        recipe.output = WeightedRewards.deserialize(object.getAsJsonArray(LibRegistries.REWARDS));
    
        return recipe;
    }
}
