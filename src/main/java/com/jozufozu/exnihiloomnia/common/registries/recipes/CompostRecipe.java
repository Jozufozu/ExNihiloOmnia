package com.jozufozu.exnihiloomnia.common.registries.recipes;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.jozufozu.exnihiloomnia.common.lib.LibRegistries;
import com.jozufozu.exnihiloomnia.common.registries.JsonHelper;
import com.jozufozu.exnihiloomnia.common.util.Color;
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
    private Color color;
    
    /**
     * The item that will be given once composting is complete
     */
    private ItemStack output = new ItemStack(Blocks.DIRT);
    
    public Ingredient getInput()
    {
        return input;
    }
    
    public int getAmount()
    {
        return volume;
    }
    
    public Color getColor()
    {
        return color;
    }
    
    public ItemStack getOutput()
    {
        return output.copy();
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

        if (recipeObject.has(LibRegistries.OUTPUT_GENERIC))
        {
            out.output = JsonHelper.deserializeItem(recipeObject.getAsJsonObject(LibRegistries.OUTPUT_GENERIC), true);
        }

        return out;
    }
}
