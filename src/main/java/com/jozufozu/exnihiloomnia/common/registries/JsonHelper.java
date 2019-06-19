package com.jozufozu.exnihiloomnia.common.registries;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.jozufozu.exnihiloomnia.common.lib.RegistriesLib;
import com.jozufozu.exnihiloomnia.common.registries.ingredients.IngredientNBT;
import com.jozufozu.exnihiloomnia.common.util.Color;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

import java.util.function.Predicate;

public class JsonHelper
{
    public static Ingredient deserializeBlockIngredient(JsonObject object) throws JsonParseException
    {
        return deserializeIngredient(object, item -> Block.getBlockFromItem(item.getItem()) != Blocks.AIR);
    }
    
    public static Ingredient deserializeIngredient(JsonObject object, Predicate<ItemStack> requirements) throws JsonParseException
    {
        Ingredient out = deserializeIngredient(object);
        
        boolean meetsReq = false;
    
        for (ItemStack itemStack : out.getMatchingStacks())
        {
            if (requirements.test(itemStack))
                meetsReq = true;
        }
        if (!meetsReq)
            throw new JsonSyntaxException("Given input " + object.toString() + "' is invalid!");
        
        return out;
    }
    
    /**
     * Makes an {@link Ingredient} object from Json. Will try to find an oredict entry from an item ID if the oredict tag has an item resource location
     */
    public static Ingredient deserializeIngredient(JsonObject input) throws JsonParseException
    {
        boolean isItem = input.has(RegistriesLib.ITEM_ID);
        boolean isOredict = input.has(RegistriesLib.OREDICT);
        boolean isNbt = input.has(RegistriesLib.NBT);
        
        if (isItem && isOredict)
        {
            throw new JsonSyntaxException("Cannot have both oredict and item tags in input!");
        }
    
        if (isNbt && isOredict)
        {
            throw new JsonSyntaxException("Cannot have both oredict and nbt tags in input!");
        }
        
        if (isItem)
        {
            ItemStack itemStack = deserializeItem(input, false);
            
            if (!input.has(RegistriesLib.DATA))
                itemStack = new ItemStack(itemStack.getItem(), 1, OreDictionary.WILDCARD_VALUE);
    
            if (isNbt)
            {
                return new IngredientNBT(itemStack);
            }
            
            return Ingredient.fromStacks(itemStack);
        }
        else if (isOredict)
        {
            String oreDict = JsonUtils.getString(input, RegistriesLib.OREDICT);
            
            if (oreDict.contains(":"))
            {
                Item item = Item.REGISTRY.getObject(new ResourceLocation(oreDict));
                
                if (item == null)
                {
                    throw new JsonSyntaxException("Unknown item '" + oreDict + "'");
                }
                
                ItemStack stack = new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE);
                int[] oreIDs = OreDictionary.getOreIDs(stack);
                
                if (oreIDs.length == 0)
                {
                    RegistryLoader.error("Given item ingredient, '" + oreDict + "' has no Ore Dictionary entries! Using the item instead.");
                    
                    return Ingredient.fromStacks(stack);
                }
                
                return new OreIngredient(OreDictionary.getOreName(oreIDs[0]));
            }
            
            return new OreIngredient(oreDict);
        }
        
        throw new JsonSyntaxException("Recipe is missing input!");
    }
    
    /**
     * Turns a {@link JsonObject} into an {@link ItemStack} using the standard id, data, count tags.
     * @param useCount Whether or not to use the provided count tag. ItemStack will have 1 item if false.
     */
    public static ItemStack deserializeItem(JsonObject itemObject, boolean useCount) throws JsonParseException
    {
        String resourceName = JsonUtils.getString(itemObject, RegistriesLib.ITEM_ID);
        Item item = Item.REGISTRY.getObject(new ResourceLocation(resourceName));
        
        if (item == null)
        {
            throw new JsonSyntaxException("Unknown item '" + resourceName + "'");
        }

        int data = JsonUtils.getInt(itemObject, RegistriesLib.DATA, 0);
        int count = useCount ? JsonUtils.getInt(itemObject, RegistriesLib.COUNT, 1) : 1;
    
        if (itemObject.has("nbt"))
        {
            try
            {
                JsonElement element = itemObject.get("nbt");
                NBTTagCompound nbt;
                if(element.isJsonObject())
                    nbt = JsonToNBT.getTagFromJson(RegistryLoader.GSON.toJson(element));
                else
                    nbt = JsonToNBT.getTagFromJson(element.getAsString());
            
                NBTTagCompound tmp = new NBTTagCompound();
                if (nbt.hasKey("ForgeCaps"))
                {
                    tmp.setTag("ForgeCaps", nbt.getTag("ForgeCaps"));
                    nbt.removeTag("ForgeCaps");
                }
            
                tmp.setTag("tag", nbt);
                tmp.setString("id", resourceName);
                tmp.setInteger("Count", count);
                tmp.setInteger("Damage", data);
            
                return new ItemStack(tmp);
            }
            catch (NBTException e)
            {
                throw new JsonSyntaxException("Invalid NBT Entry: " + e.toString());
            }
        }
        
        return new ItemStack(item, count, data);
    }
    
//    public static Fluid deserializeFluid(JsonObject object, String memberName) throws JsonParseException
//    {
//        String name = JsonUtils.getString(object, memberName);
//
//        if (!FluidRegistry.isFluidRegistered(name))
//        {
//            throw new JsonSyntaxException("Fluid '" + name + "' does not exist!");
//        }
//
//        return FluidRegistry.getFluid(name);
//    }
    
    /**
     * Turns a given string in either hex or r, g, b(, a) format into a packed color int
     */
    public static Color deserializeColor(String colorString) throws JsonParseException
    {
        int color = 0;
        
        if (colorString.contains(","))
        {
            colorString = colorString.replace(" ", "");
            String[] rgba = colorString.split(",");
            
            if (rgba.length > 4 || rgba.length < 3)
                throw new JsonSyntaxException("Could not parse color '" + colorString + "'");
            
            color |= Integer.parseInt(rgba[0]) << 16;
            color |= Integer.parseInt(rgba[1]) << 8;
            color |= Integer.parseInt(rgba[2]);
    
            boolean alpha = rgba.length == 4;
            if (alpha)
            {
                color |= Integer.parseInt(rgba[3]) << 24;
            }
            
            return new Color(color, alpha);
        }
        else
        {
            if (colorString.length() == 8 || colorString.length() == 6)
            {
                color = Integer.parseInt(colorString, 16);
    
                return new Color(color, colorString.length() == 8);
            }
        }
        
        throw new JsonSyntaxException("Could not parse color '" + colorString + "'");
    }
}
