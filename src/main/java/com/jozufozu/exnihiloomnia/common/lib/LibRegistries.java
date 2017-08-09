package com.jozufozu.exnihiloomnia.common.lib;

import com.jozufozu.exnihiloomnia.ExNihilo;
import net.minecraft.util.ResourceLocation;

public class LibRegistries
{
    //IO
    public static final String INPUT_SUFFIX = "Input";
    public static final String OUTPUT_SUFFIX = "Output";
    
    public static final String FLUID = "fluid";
    public static final String ITEM = "item";
    public static final String BLOCK = "block";
    
    public static final String INPUT_FLUID = FLUID + INPUT_SUFFIX;
    public static final String INPUT_ITEM = ITEM + INPUT_SUFFIX;
    public static final String INPUT_BLOCK = BLOCK + INPUT_SUFFIX;
    public static final String INPUT_GENERIC = "input";
    
    public static final String OUTPUT_FLUID = FLUID + OUTPUT_SUFFIX;
    public static final String OUTPUT_ITEM = ITEM + OUTPUT_SUFFIX;
    public static final String OUTPUT_BLOCK = BLOCK + OUTPUT_SUFFIX;
    public static final String OUTPUT_GENERIC = "output";
    
    public static final String REWARDS = "rewards";     //Multiple weighted outputs
    
    //Generic fields
    public static final String VOLUME = "amount";       //How much of something, in mB
    public static final String TIME = "time";           //How long something takes, in ticks
    public static final String COLOR = "color";         //What color something is
    
    //Ingredient & ItemStack
    public static final String ITEM_ID = "id";          //The item or block's registry name
    public static final String COUNT = "count";         //How many things in this ItemStack
    public static final String DATA = "data";           //The metadata

    public static final String OREDICT = "oredict";     //The oredict name of the input, or an item that is registered to the oredict
    public static final String NBT = "nbt";             //An item NBT tag
    
    //Fields for WeightedRewards, in addition to the ItemStack fields
    public static final String CHANCE = "chance";       //The chance for this reward to drop
    public static final String DROP_CATEGORY = "type";  //What category this drop is. Used to modify drop rates based on tools
    
    //Fluid mixing cunts
    public static final String IN_BARREL = "lower";
    public static final String ON_BARREL = "upper";
    
    //Crucibles
    public static final String HEAT = "heat";
    public static final String INPUT_VOLUME = "inputVolume";
    public static final String OUTPUT_VOLUME = "outputVolume";
    
    //Ores
    public static final String ORE_INGOT = "ingotItem"; //The ItemStack that you get when you smelt the ore
    
    //The chance for the ore to be sifted from the different blocks
    public static final String GRAVEL_CHANCE = "gravelChance";
    public static final String SAND_CHANCE = "sandChance";
    public static final String DUST_CHANCE = "dustChance";
    
    //What an ore should be registered as
    public static final String OREDICT_NAMES = OREDICT + "Names";
    
    //Registries
    public static final ResourceLocation ORE = new ResourceLocation(ExNihilo.MODID, "ores");
    
    public static final ResourceLocation COMPOST = new ResourceLocation(ExNihilo.MODID, "composting");
    public static final ResourceLocation FERMENTING = new ResourceLocation(ExNihilo.MODID, "fermenting");
    public static final ResourceLocation FLUID_CRAFTING = new ResourceLocation(ExNihilo.MODID, "fluid_crafting");
    public static final ResourceLocation MIXING = new ResourceLocation(ExNihilo.MODID, "fluid_mixing");
    
    public static final ResourceLocation SIEVE = new ResourceLocation(ExNihilo.MODID, "sifting");
    public static final ResourceLocation HAMMER = new ResourceLocation(ExNihilo.MODID, "hammering");
    
    public static final ResourceLocation MELTING = new ResourceLocation(ExNihilo.MODID, "melting");
    public static final ResourceLocation HEAT_SOURCE = new ResourceLocation(ExNihilo.MODID, "heat");
}
