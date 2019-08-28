package com.jozufozu.exnihiloomnia.common.registries

import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.jozufozu.exnihiloomnia.common.lib.LibRegistries
import com.jozufozu.exnihiloomnia.common.registries.ingredients.IngredientNBT
import com.jozufozu.exnihiloomnia.common.util.Color
import net.minecraft.block.Block
import net.minecraft.init.Blocks
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraft.nbt.JsonToNBT
import net.minecraft.nbt.NBTException
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.JsonUtils
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.oredict.OreDictionary
import net.minecraftforge.oredict.OreIngredient

object JsonHelper {
    @Throws(JsonParseException::class)
    fun deserializeBlockIngredient(jsonObject: JsonObject): Ingredient {
        return deserializeIngredient(jsonObject) { Block.getBlockFromItem(it.item) !== Blocks.AIR }
    }

    @Throws(JsonParseException::class)
    fun deserializeIngredient(jsonObject: JsonObject, requirements: (ItemStack) -> Boolean): Ingredient {
        val out = deserializeIngredient(jsonObject)

        var meetsReq = false

        for (itemStack in out.matchingStacks) {
            if (requirements(itemStack))
                meetsReq = true
        }
        if (!meetsReq)
            throw JsonSyntaxException("Given input $jsonObject' is invalid!")

        return out
    }

    /**
     * Makes an [Ingredient] object from Json. Will try to find an oredict entry from an item ID if the oredict tag has an item resource location
     */
    @Throws(JsonParseException::class)
    fun deserializeIngredient(input: JsonObject): Ingredient {
        val isItem = input.has(LibRegistries.ID)
        val isOredict = input.has(LibRegistries.OREDICT)
        val isNbt = input.has(LibRegistries.NBT)

        if (isItem && isOredict) {
            throw JsonSyntaxException("Cannot have both oredict and item tags in input!")
        }

        if (isNbt && isOredict) {
            throw JsonSyntaxException("Cannot have both oredict and nbt tags in input!")
        }

        if (isItem) {
            var itemStack = deserializeItem(input, false)

            if (!input.has(LibRegistries.DATA))
                itemStack = ItemStack(itemStack.item, 1, OreDictionary.WILDCARD_VALUE)

            return if (isNbt) {
                IngredientNBT(itemStack)
            } else Ingredient.fromStacks(itemStack)

        } else if (isOredict) {
            val oreDict = JsonUtils.getString(input, LibRegistries.OREDICT)

            if (oreDict.contains(":")) {
                val item = Item.REGISTRY.getObject(ResourceLocation(oreDict))
                        ?: throw JsonSyntaxException("Unknown item '$oreDict'")

                val stack = ItemStack(item, 1, OreDictionary.WILDCARD_VALUE)
                val oreIDs = OreDictionary.getOreIDs(stack)

                if (oreIDs.isEmpty()) {
                    RegistryLoader.error("Given item ingredient, '$oreDict' has no Ore Dictionary entries! Using the item instead.")

                    return Ingredient.fromStacks(stack)
                }

                return OreIngredient(OreDictionary.getOreName(oreIDs[0]))
            }

            return OreIngredient(oreDict)
        }

        throw JsonSyntaxException("Recipe is missing input!")
    }

    /**
     * Turns a [JsonObject] into an [ItemStack] using the standard id, data, count tags.
     * @param useCount Whether or not to use the provided count tag. ItemStack will have 1 item if false.
     */
    @Throws(JsonParseException::class)
    fun deserializeItem(itemObject: JsonObject, useCount: Boolean): ItemStack {
        val resourceName = JsonUtils.getString(itemObject, LibRegistries.ID)
        val item = Item.REGISTRY.getObject(ResourceLocation(resourceName))
                ?: throw JsonSyntaxException("Unknown item '$resourceName'")

        val data = JsonUtils.getInt(itemObject, LibRegistries.DATA, 0)
        val count = if (useCount) JsonUtils.getInt(itemObject, LibRegistries.COUNT, 1) else 1

        if (itemObject.has("nbt")) {
            try {
                val element = itemObject.get("nbt")
                val nbt: NBTTagCompound = if (element.isJsonObject)
                    JsonToNBT.getTagFromJson(RegistryLoader.GSON.toJson(element))
                else
                    JsonToNBT.getTagFromJson(element.asString)

                val tmp = NBTTagCompound()
                if (nbt.hasKey("ForgeCaps")) {
                    tmp.setTag("ForgeCaps", nbt.getTag("ForgeCaps"))
                    nbt.removeTag("ForgeCaps")
                }

                tmp.setTag("tag", nbt)
                tmp.setString("id", resourceName)
                tmp.setInteger("Count", count)
                tmp.setInteger("Damage", data)

                return ItemStack(tmp)
            } catch (e: NBTException) {
                throw JsonSyntaxException("Invalid NBT Entry: $e")
            }

        }

        return ItemStack(item, count, data)
    }

    @Throws(JsonParseException::class)
    fun deserializeFluid(jsonObject: JsonObject, memberName: String): Fluid {
        val name = JsonUtils.getString(jsonObject, memberName)

        if (!FluidRegistry.isFluidRegistered(name)) {
            throw JsonSyntaxException("Fluid '$name' does not exist!")
        }

        return FluidRegistry.getFluid(name)
    }

    /**
     * Turns a given string in either hex or r, g, b(, a) format into a packed color int
     */
    @Throws(JsonParseException::class)
    fun deserializeColor(colorString: String): Color {
        var colorString = colorString
        var color = 0

        if (colorString.contains(",")) {
            colorString = colorString.replace(" ", "")
            val rgba = colorString.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            if (rgba.size > 4 || rgba.size < 3)
                throw JsonSyntaxException("Could not parse color '$colorString'")

            color = color or (Integer.parseInt(rgba[0]) shl 16)
            color = color or (Integer.parseInt(rgba[1]) shl 8)
            color = color or Integer.parseInt(rgba[2])

            val alpha = rgba.size == 4
            if (alpha) {
                color = color or (Integer.parseInt(rgba[3]) shl 24)
            }

            return Color(color, alpha)
        } else {
            if (colorString.length == 8 || colorString.length == 6) {
                color = Integer.parseInt(colorString, 16)

                return Color(color, colorString.length == 8)
            }
        }

        throw JsonSyntaxException("Could not parse color '$colorString'")
    }
}
