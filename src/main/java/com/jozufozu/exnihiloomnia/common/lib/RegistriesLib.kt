package com.jozufozu.exnihiloomnia.common.lib

import com.jozufozu.exnihiloomnia.ExNihilo
import net.minecraft.util.ResourceLocation

object RegistriesLib {
    //IO
    const val INPUT = "input"
    const val OUTPUT = "output"

    const val FLUID = "fluid"
    const val ITEM = "item"
    const val BLOCK = "block"

    val INPUT_FLUID = get(FLUID, INPUT)
    val INPUT_ITEM = get(ITEM, INPUT)
    val INPUT_BLOCK = get(BLOCK, INPUT)

    val OUTPUT_FLUID = get(FLUID, OUTPUT)
    val OUTPUT_ITEM = get(ITEM, OUTPUT)
    val OUTPUT_BLOCK = get(BLOCK, OUTPUT)

    const val REWARDS = "rewards"     //Multiple weighted outputs

    //Generic fields
    const val VOLUME = "amount"       //How much of something, in mB
    const val TIME = "time"           //How long something takes, in ticks
    const val COLOR = "color"         //What color something is

    //Ingredient & ItemStack
    const val ITEM_ID = "id"          //The item or block's registry name
    const val COUNT = "count"         //How many things in this ItemStack
    const val DATA = "data"           //The metadata

    const val OREDICT = "oredict"     //The oredict name of the input, or an item that is registered to the oredict
    const val NBT = "nbt"             //An item NBT tag

    //Fields for WeightedRewards, in addition to the ItemStack fields
    const val CHANCE = "chance"       //The chance for this reward to drop
    const val DROP_CATEGORY = "type"  //What category this drop is. Used to modify drop rates based on tools

    //Fluid mixing cunts
    const val IN_BARREL = "lower"
    const val ON_BARREL = "upper"

    //Crucibles
    const val HEAT = "heat"
    val INPUT_VOLUME = get(INPUT, VOLUME)
    val OUTPUT_VOLUME = get(OUTPUT, VOLUME)

    //Ores
    val ORE_INGOT = get("ingot", ITEM) //The ItemStack that you get when you smelt the ore

    //Registries
    val ORE = ResourceLocation(ExNihilo.MODID, "ores")

    val COMPOST = ResourceLocation(ExNihilo.MODID, "composting")
    val FERMENTING = ResourceLocation(ExNihilo.MODID, "fermenting")
    val FLUID_CRAFTING = ResourceLocation(ExNihilo.MODID, "fluid_crafting")
    val MIXING = ResourceLocation(ExNihilo.MODID, "fluid_mixing")

    val SIEVE = ResourceLocation(ExNihilo.MODID, "sifting")
    val HAMMER = ResourceLocation(ExNihilo.MODID, "hammering")

    val MELTING = ResourceLocation(ExNihilo.MODID, "melting")
    val HEAT_SOURCE = ResourceLocation(ExNihilo.MODID, "heat")

    private fun get(vararg name: String) = name.joinToString("_")
}
