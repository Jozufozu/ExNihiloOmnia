package com.jozufozu.exnihiloomnia.common.registries.ingredients;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.jozufozu.exnihiloomnia.common.lib.LibRegistries;
import com.jozufozu.exnihiloomnia.common.registries.RegistryLoader;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import java.util.function.Predicate;

public class WorldIngredient implements Predicate<IBlockState>
{
    private final Block block;
    private final int data;
    
    public WorldIngredient(Block block, int data)
    {
        this.block = block;
        this.data = data;
    }
    
    @Override
    public boolean test(IBlockState iBlockState)
    {
        if (data == OreDictionary.WILDCARD_VALUE)
        {
            return iBlockState.getBlock() == block;
        }
        
        return iBlockState.getBlock() == block && block.getMetaFromState(iBlockState) == data;
    }
    
    public ItemStack getStack()
    {
        return new ItemStack(block, 1, data);
    }
    
    public static WorldIngredient deserialize(JsonObject object)
    {
        boolean isAbsolute = object.has(LibRegistries.ITEM_ID);
        boolean isOredict = object.has(LibRegistries.OREDICT);
    
        if (isAbsolute && isOredict)
        {
            throw new JsonSyntaxException("Cannot have both oredict and item tags in input!");
        }
        
        if (isAbsolute)
        {
            Block block = Block.REGISTRY.getObject(new ResourceLocation(JsonUtils.getString(object, LibRegistries.ITEM_ID)));
            int data = JsonUtils.getInt(object, LibRegistries.DATA, OreDictionary.WILDCARD_VALUE);
            
            return new WorldIngredient(block, data);
        }
        else if (isOredict)
        {
            String oreDict = JsonUtils.getString(object, LibRegistries.OREDICT);
    
            if (oreDict.contains(":"))
            {
                Block block = Block.REGISTRY.getObject(new ResourceLocation(oreDict));
        
                if (block == null)
                {
                    throw new JsonSyntaxException("Unknown block '" + oreDict + "'");
                }
        
                int[] oreIDs = OreDictionary.getOreIDs(new ItemStack(block));
        
                if (oreIDs.length == 0)
                {
                    RegistryLoader.error("Given block ingredient, '" + oreDict + "' has no Ore Dictionary entries! Using the block instead.");
            
                    return new WorldIngredient(block, OreDictionary.WILDCARD_VALUE);
                }
        
                return new OreWorldIngredient(OreDictionary.getOreName(oreIDs[0]));
            }
    
            return new OreWorldIngredient(oreDict);
        }
    
        throw new JsonSyntaxException("Recipe is missing input!");
    }
}
