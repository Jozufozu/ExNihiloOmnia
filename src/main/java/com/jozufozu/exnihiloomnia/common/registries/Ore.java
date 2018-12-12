package com.jozufozu.exnihiloomnia.common.registries;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.jozufozu.exnihiloomnia.common.lib.LibRegistries;
import com.jozufozu.exnihiloomnia.common.util.Color;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.ArrayList;

/**
 * The registry name is the base {@link ResourceLocation} that all registered blocks and items will be given
 * This is usually going to be the json file name and location
 * Ex. mod:ore will create:
 * mod:ore_gravel_ore
 * mod:ore_sand_ore
 * mod:ore_dust_ore
 * mod:ore_chunk
 * mod:ore_crushed
 * mod:ore_powder
 * mod:ore_ingot
 */
public class Ore extends IForgeRegistryEntry.Impl<Ore>
{
    private Color color;
    
    /**
     * The chance that each type of item will drop
     * If negative, the reward will not be added
     */
    private float gravel_chance = -1;
    private float sand_chance = -1;
    private float dust_chance = -1;
    
    /**
     * The blocks are assigned %sOre
     * The ingot is assigned %sIngot, if an ingot is generated
     */
    private ArrayList<String> oreDictNames;
    
    /**
     * What all the blocks smelt into
     */
    private ItemStack ingot;

    public static Ore deserialize(JsonObject object) throws JsonParseException
    {
        Ore out = new Ore();
        
        out.color = JsonHelper.deserializeColor(JsonUtils.getString(object, LibRegistries.COLOR, "ffffff"));
        
        float chance = JsonUtils.getFloat(object, LibRegistries.CHANCE, -1);

        out.gravel_chance = JsonUtils.getFloat(object, LibRegistries.GRAVEL_CHANCE, chance);
        out.sand_chance = JsonUtils.getFloat(object, LibRegistries.SAND_CHANCE, chance);
        out.dust_chance = JsonUtils.getFloat(object, LibRegistries.DUST_CHANCE, chance);
        
        if (object.has(LibRegistries.ORE_INGOT))
        {
            out.ingot = JsonHelper.deserializeItem(object.getAsJsonObject(LibRegistries.ORE_INGOT), false);
        }
        
        if (object.has(LibRegistries.OREDICT_NAMES))
        {
            out.oreDictNames = new ArrayList<>();
    
            for (JsonElement jsonElement : object.getAsJsonArray(LibRegistries.OREDICT_NAMES))
            {
                out.oreDictNames.add(jsonElement.getAsJsonPrimitive().getAsString());
            }
        }
        
        return out;
    }
}
