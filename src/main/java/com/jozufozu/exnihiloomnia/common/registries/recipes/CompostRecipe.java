package com.jozufozu.exnihiloomnia.common.registries.recipes;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.jozufozu.exnihiloomnia.common.lib.LibRegistries;
import com.jozufozu.exnihiloomnia.common.registries.JsonHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class CompostRecipe extends IForgeRegistryEntry.Impl<CompostRecipe>
{
    /**
     * The thing that you put in to compost
     */
    private Ingredient input;
    
    /**
     * How much stuff each item will fill the barrel with, in mB
     * Default is 125, or 8 items to fill the barrel
     */
    private int volume = 125;
    
    /**
     * The color that the compost will appear as in the
     */
    private int color;
    
    /**
     * The time in ticks this recipe takes to compost
     * Default is 200 ticks or 10 seconds
     */
    private int compostTime = 200;
    
    /**
     * The item that will be given once composting is complete
     */
    private ItemStack output = new ItemStack(Blocks.DIRT);
    
    private CompostRecipe() {}
    
    public CompostRecipe(Ingredient input, int volume, int color, int compostTime, ItemStack output)
    {
        this.input = input;
        this.volume = volume;
        this.color = color;
        this.compostTime = compostTime;
        this.output = output;
    }
    
    public int getVolume()
    {
        return volume;
    }
    
    public int getColor()
    {
        return color;
    }
    
    public int getCompostTime()
    {
        return compostTime;
    }
    
    public ItemStack getOutput()
    {
        return output;
    }
    
    public boolean matches(ItemStack stack)
    {
        return input.apply(stack);
    }
    
    public static CompostRecipe deserialize(JsonObject recipeObject) throws JsonParseException
    {
        if (!recipeObject.has(LibRegistries.INPUT_GENERIC))
        {
            throw new JsonSyntaxException("Compost recipe has no input!");
        }
    
        CompostRecipe out = new CompostRecipe();

        JsonObject input = recipeObject.getAsJsonObject(LibRegistries.INPUT_GENERIC);

        out.input = JsonHelper.deserializeIngredient(input);
        out.color = JsonHelper.deserializeColor(JsonUtils.getString(recipeObject, LibRegistries.COLOR, "ffffff"));

        out.volume = JsonUtils.getInt(input, LibRegistries.VOLUME, 125);
        out.compostTime = JsonUtils.getInt(recipeObject, LibRegistries.TIME, 200);

        if (recipeObject.has(LibRegistries.OUTPUT_BLOCK))
        {
            out.output = JsonHelper.deserializeItem(recipeObject.getAsJsonObject(LibRegistries.OUTPUT_BLOCK), true);
        }

        return out;
    }
}
