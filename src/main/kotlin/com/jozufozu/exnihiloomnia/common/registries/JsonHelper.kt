package com.jozufozu.exnihiloomnia.common.registries

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.jozufozu.exnihiloomnia.common.lib.LibRegistries
import com.jozufozu.exnihiloomnia.common.util.Color
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.JsonToNBT
import net.minecraft.nbt.NBTException
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.JSONUtils
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fluids.FluidStack

object JsonHelper {
    /**
     * Turns a [JsonObject] into an [ItemStack] using the standard id, data, count tags.
     * @param useCount Whether or not to use the provided count tag. ItemStack will have 1 item if false.
     */
    @Throws(JsonParseException::class)
    fun deserializeItem(itemStack: JsonObject, useCount: Boolean): ItemStack {
        val registryName = JSONUtils.getString(itemStack, LibRegistries.ITEM)

        val item = Item.REGISTRY.getObject(ResourceLocation(registryName)) ?: throw JsonSyntaxException("Unknown item '$registryName'")

        val data = JSONUtils.getInt(itemStack, LibRegistries.DATA, 0)
        val count = if (useCount) JSONUtils.getInt(itemStack, LibRegistries.COUNT, 1) else 1

        if (itemStack.has("nbt")) {
            try {
                val element = itemStack.get("nbt")
                val nbt: NBTTagCompound = if (element.isJsonObject)
                    JsonToNBT.getTagFromJson(RegistryLoader.GSON.toJson(element))
                else
                    JsonToNBT.getTagFromJson(JSONUtils.getString(element, "nbt"))

                val tmp = NBTTagCompound()
                if (nbt.hasKey("ForgeCaps")) {
                    tmp.setTag("ForgeCaps", nbt.getTag("ForgeCaps"))
                    nbt.removeTag("ForgeCaps")
                }

                tmp.setTag("tag", nbt)
                tmp.setString("id", registryName)
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
    fun deserializeFluid(fluid: JsonElement, defaultVolume: Int = Fluid.BUCKET_VOLUME): FluidStack {

        val name: String
        val volume: Int

        when {
            fluid.isJsonPrimitive && fluid.asJsonPrimitive.isString -> {
                name = fluid.asString
                volume = defaultVolume
            }
            fluid.isJsonObject -> {
                name = JSONUtils.getString(fluid.asJsonObject, LibRegistries.FLUID)
                volume = JSONUtils.getInt(fluid.asJsonObject, LibRegistries.VOLUME, defaultVolume)
            }
            else -> throw JsonSyntaxException("Fluid must be either ")
        }

        if (!FluidRegistry.isFluidRegistered(name)) throw JsonSyntaxException("Fluid '$name' does not exist!")

        return FluidStack(FluidRegistry.getFluid(name), volume)
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
