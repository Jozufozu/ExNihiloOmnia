package com.jozufozu.exnihiloomnia.common.lib

import com.jozufozu.exnihiloomnia.ExNihilo
import net.minecraft.util.ResourceLocation

object LibRegistries {
    // IO
    const val INPUT_SUFFIX = "Input"
    const val OUTPUT_SUFFIX = "Output"

    const val CONDITIONS = "conditions"

    const val FLUID = "fluid"
    const val ITEM = "item"
    const val BLOCK = "block"

    const val FLUID_INPUT = FLUID + INPUT_SUFFIX
    const val WORLD_INPUT = BLOCK + INPUT_SUFFIX
    const val INPUT = "input"

    const val FLUID_OUTPUT = FLUID + OUTPUT_SUFFIX
    const val OUTPUT = "output"
    const val RESULT = "result"

    const val REWARDS = "rewards"     // Multiple weighted outputs

    // Generic fields
    const val VOLUME = "volume"       // How much of something, in mB
    const val TIME = "time"           // How long something takes, in ticks
    const val COLOR = "color"         // What color something is

    // Ingredient & ItemStack
    const val COUNT = "count"         // How many things in this ItemStack
    const val DATA = "data"           // The metadata
    const val VARIANTS = "variants"   // The blockstate variants in the world ingredient

    const val OREDICT = "ore"     // The oredict name of the input, or an item that is registered to the oredict

    // Fields for WeightedRewards, in addition to the ItemStack fields
    const val CHANCE = "chance"       // The chance for this reward to drop
    const val DROP_CATEGORY = "type"  // What category this drop is. Used to modify drop rates based on tools

    // Fluid mixing cunts
    const val IN_BARREL = "lower"
    const val ON_BARREL = "upper"

    // Crucibles
    const val HEAT = "heat"
    const val SOURCE = "source"
    const val INPUT_VOLUME = "inputVolume"
    const val OUTPUT_VOLUME = "outputVolume"

    // Ores
    const val ORE_INGOT = "ingotItem" // The ItemStack that you get when you smelt the ore

    // The chance for the ore to be sifted from the different blocks
    const val GRAVEL_CHANCE = "gravelChance"
    const val SAND_CHANCE = "sandChance"
    const val DUST_CHANCE = "dustChance"

    // What an ore should be registered as
    const val OREDICT_NAMES = "oredict"

    // Registries
    @JvmField val ORE = ResourceLocation(ExNihilo.MODID, "ores")

    @JvmField val COMPOST = ResourceLocation(ExNihilo.MODID, "composting")
    @JvmField val FLUID_CRAFTING = ResourceLocation(ExNihilo.MODID, "fluid_crafting")
    @JvmField val MIXING = ResourceLocation(ExNihilo.MODID, "fluid_mixing")
    @JvmField val LEAKING = ResourceLocation(ExNihilo.MODID, "leaking")
    @JvmField val FERMENTING = ResourceLocation(ExNihilo.MODID, "fermenting")
    @JvmField val SUMMONING = ResourceLocation(ExNihilo.MODID, "summoning")

    @JvmField val SIEVE = ResourceLocation(ExNihilo.MODID, "sifting")
    @JvmField val HAMMER = ResourceLocation(ExNihilo.MODID, "hammering")

    @JvmField val MELTING = ResourceLocation(ExNihilo.MODID, "melting")
    @JvmField val HEAT_SOURCE = ResourceLocation(ExNihilo.MODID, "heat")

    @JvmField val MESH = ResourceLocation(ExNihilo.MODID, "mesh")
}
