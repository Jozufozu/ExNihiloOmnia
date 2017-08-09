package com.jozufozu.exnihiloomnia.common.registries;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;


public class WeightedRewards
{
    private final ArrayList<WeightedDrop> outputs = new ArrayList<>();
    
    public void addOutput(WeightedDrop output)
    {
        outputs.add(output);
    }
    
    /**
     * Gets a list of random drops based on the outputs
     * @param player
     * @param processor The item being used to generate these rewards
     * @param random
     */
    public NonNullList<ItemStack> roll(@Nullable EntityPlayer player, @Nonnull ItemStack processor, Random random)
    {
        NonNullList<ItemStack> ret = NonNullList.create();
        
        
        NonNullList<ItemStack> temp = NonNullList.create();
    
        for (WeightedDrop reward : outputs)
        {
            ItemStack roll = reward.roll(player, processor, random);
        
            if (roll.isEmpty())
                continue;
        
            temp.add(roll);
        }
    
        for (ItemStack stack : temp)
        {
            if (stack.getCount() > 1)
            {
                while (stack.getCount() > 0)
                {
                    ret.add(new ItemStack(stack.getItem(), 1, stack.getMetadata()));
                    stack.shrink(1);
                }
            }
            else
            {
                ret.add(stack);
            }
        }
        
        return ret;
    }
    
    public Map<ItemStack, List<WeightedDrop>> getEquivalencies()
    {
        Map<ItemStack, List<WeightedDrop>> temp = Maps.newLinkedHashMap();
        
        output:
        for (WeightedDrop reward : outputs)
        {
            ItemStack drop = reward.getDrop();
        
            if (drop == ItemStack.EMPTY)
                continue;
        
            for (ItemStack itemStack : temp.keySet())
            {
                if (itemStack.isItemEqual(drop))
                {
                    temp.get(itemStack).add(reward);
                    continue output;
                }
            }
            
            temp.put(ItemHandlerHelper.copyStackWithSize(drop, 1), Lists.newArrayList(reward));
        }
        
        List<ItemStack> sorted = new ArrayList<>(temp.keySet());
        sorted.sort(Comparator.comparingInt(o -> Item.REGISTRY.getIDForObject(o.getItem())));
    
        Map<ItemStack, List<WeightedDrop>> out = Maps.newLinkedHashMap();
    
        for (ItemStack stack : sorted)
        {
            out.put(stack, temp.get(stack));
        }
        
        return out;
    }
    
    public static WeightedRewards deserialize(JsonArray array) throws JsonParseException
    {
        if (array == null)
        {
            throw new JsonParseException("Recipe is missing rewards! Skipping");
        }
        
        WeightedRewards out = new WeightedRewards();
        
        RegistryLoader.pushCtx("Rewards");
        
        for (JsonElement jsonElement : array)
        {
            if (jsonElement.isJsonObject())
            {
                try
                {
                    out.addOutput(WeightedDrop.deserialize(jsonElement.getAsJsonObject()));
                }
                catch (JsonParseException e)
                {
                    RegistryLoader.error(e.getMessage());
                }
            }
            else if (jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isString())
            {
                String string = jsonElement.getAsJsonPrimitive().getAsString();
                Item itemMaybe = Item.REGISTRY.getObject(new ResourceLocation(string));
                
                if (itemMaybe == null)
                {
                    RegistryLoader.error("Unknown item '" + string + "'");
                    continue;
                }
                
                out.addOutput(new WeightedDrop(new ItemStack(itemMaybe), 1));
            }
            else
            {
                RegistryLoader.error("Null reward");
            }
        }
        
        RegistryLoader.popCtx();
        
        if (out.outputs.size() == 0)
        {
            throw new JsonSyntaxException("Recipe has no rewards!");
        }
        
        return out;
    }
}
