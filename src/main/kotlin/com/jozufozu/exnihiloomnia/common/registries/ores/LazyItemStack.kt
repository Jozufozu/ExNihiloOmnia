package com.jozufozu.exnihiloomnia.common.registries.ores

import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import com.jozufozu.exnihiloomnia.common.lib.LibRegistries
import com.jozufozu.exnihiloomnia.common.registries.RegistryLoader
import com.jozufozu.exnihiloomnia.common.util.contains
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.JsonToNBT
import net.minecraft.nbt.NBTException
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.JSONUtils

class LazyItemStack(val itemName: String, val count: Int, val data: Int, val nbt: NBTTagCompound?) {
    lateinit var stack: ItemStack

    fun get(): ItemStack = if (::stack.isInitialized) stack else {
        Item.getByNameOrId(itemName)?.let {
            ItemStack(it, count, data, nbt)
        } ?: throw IllegalArgumentException("$itemName is not registered!")
    }

    companion object {
        fun deserialize(itemStack: JsonObject): LazyItemStack {
            val registryName = JSONUtils.getString(itemStack, LibRegistries.ITEM)

            val data = JSONUtils.getInt(itemStack, LibRegistries.DATA, 0)
            val count = JSONUtils.getInt(itemStack, LibRegistries.COUNT, 1)

            val nbt = if ("nbt" in itemStack) {
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

                    tmp
                } catch (e: NBTException) {
                    throw JsonSyntaxException("Invalid NBT Entry: $e")
                }
            } else null

            return LazyItemStack(registryName, count, data, nbt)
        }
    }
}