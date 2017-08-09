package com.jozufozu.exnihiloomnia.common.registries;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.jozufozu.exnihiloomnia.common.lib.LibRegistries;
import com.jozufozu.exnihiloomnia.common.util.IRewardProcessor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;

import java.util.Random;

public class WeightedDrop
{
    private ItemStack drop;
    private float chance;
    
    private String category;
    
    public WeightedDrop() {}
    
    public WeightedDrop(ItemStack drop, float chance)
    {
        this.drop = drop;
        this.chance = chance;
    }
    
    public ItemStack getDrop()
    {
        return drop;
    }
    
    public float getChance()
    {
        return chance;
    }
    
    public String getType()
    {
        return category;
    }
    
    public ItemStack roll(EntityPlayer user, ItemStack activeStack, Random random)
    {
        float chance = this.chance;
        if (activeStack.getItem() instanceof IRewardProcessor)
            chance *= ((IRewardProcessor) activeStack.getItem()).getEffectivenessForType(this.category);
        
        return random.nextFloat() < chance ? drop.copy() : ItemStack.EMPTY;
    }
    
    public static WeightedDrop deserialize(JsonObject object) throws JsonParseException
    {
        WeightedDrop out = new WeightedDrop();
        
        out.drop = JsonHelper.deserializeItem(object, true);
        out.chance = JsonUtils.getFloat(object, LibRegistries.CHANCE, 1.0f);
        out.category = JsonUtils.getString(object, LibRegistries.DROP_CATEGORY, "");
        
        return out;
    }
}
