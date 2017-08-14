package com.jozufozu.exnihiloomnia.common.registries.recipes;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.jozufozu.exnihiloomnia.common.lib.LibRegistries;
import com.jozufozu.exnihiloomnia.common.registries.JsonHelper;
import com.jozufozu.exnihiloomnia.common.registries.WeightedRewards;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class HammerRecipe extends IForgeRegistryEntry.Impl<HammerRecipe>
{
    private Ingredient block;
    private WeightedRewards rewards;

    public Ingredient getIngredient()
    {
        return block;
    }
    
    public WeightedRewards getRewards()
    {
        return rewards;
    }
    
    public boolean matches(IBlockState iBlockState)
    {
        return this.block.test(new ItemStack(iBlockState.getBlock(), 1, iBlockState.getBlock().getMetaFromState(iBlockState)));
    }
    
    public static HammerRecipe deserialize(JsonObject object) throws JsonParseException
    {
        HammerRecipe out = new HammerRecipe();
    
        if (!object.has(LibRegistries.INPUT_BLOCK))
        {
            throw new JsonSyntaxException("Hammer recipe is missing input!");
        }
        
        out.block = JsonHelper.deserializeBlockIngredient(object.getAsJsonObject(LibRegistries.INPUT_BLOCK));
        
        out.rewards = WeightedRewards.deserialize(object.getAsJsonArray(LibRegistries.REWARDS));
        
        return out;
    }
}
