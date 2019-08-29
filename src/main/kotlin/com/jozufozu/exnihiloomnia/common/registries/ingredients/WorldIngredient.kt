package com.jozufozu.exnihiloomnia.common.registries.ingredients

import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import com.jozufozu.exnihiloomnia.common.lib.LibRegistries
import com.jozufozu.exnihiloomnia.common.registries.RegistryLoader
import com.jozufozu.exnihiloomnia.common.util.contains
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.item.ItemStack
import net.minecraft.util.JsonUtils
import net.minecraft.util.NonNullList
import net.minecraftforge.oredict.OreDictionary
import java.util.*
import java.util.function.Predicate
import kotlin.collections.ArrayList

abstract class WorldIngredient: Predicate<IBlockState>, Comparable<WorldIngredient> {

    abstract val stacks: NonNullList<ItemStack>

    override fun compareTo(other: WorldIngredient): Int {
        return if (this is ExplicitWorldIngredient) {
            if (other is ExplicitWorldIngredient) {
                if (block === other.block) {
                    if (data.isPresent)
                        if (other.data.isPresent) data.get() - other.data.get()
                        else -other.predicates.size
                    else if (other.data.isPresent)
                        predicates.size
                    else
                        predicates.size - other.predicates.size
                } else
                    Block.getIdFromBlock(block) - Block.getIdFromBlock(other.block)
            } else 1
        } else if (other is ExplicitWorldIngredient) -1
        else 0
    }

    companion object {
        fun deserialize(json: JsonObject): WorldIngredient {
            RegistryLoader.pushCtx("Input")

            if (LibRegistries.ID in json && LibRegistries.OREDICT in json) throw JsonSyntaxException("blockInput can have \"id\" or \"oredict\", but not both")

            if (LibRegistries.OREDICT in json) {
                val oredict = JsonUtils.getString(json, LibRegistries.OREDICT)

                if (!OreDictionary.doesOreNameExist(oredict)) throw JsonSyntaxException("Nothing called '$oredict' exists in the ore dictionary")

                return OreWorldIngredient(oredict)
            }

            val blockName = JsonUtils.getString(json, LibRegistries.ID)
            val block = Block.getBlockFromName(blockName) ?: throw JsonSyntaxException("$blockName is not a valid block")

            if (LibRegistries.DATA in json && LibRegistries.VARIANTS in json) throw JsonSyntaxException("blockInput can have \"data\" or \"variants\", but not both")

            var out = ExplicitWorldIngredient(block)

            if (LibRegistries.DATA in json) {
                val data = JsonUtils.getInt(json, LibRegistries.DATA)

                out = ExplicitWorldIngredient(block, Optional.of(data))

            } else if (LibRegistries.VARIANTS in json) {
                val variants = JsonUtils.getJsonObject(json, LibRegistries.VARIANTS)

                out = ExplicitWorldIngredient(block)

                RegistryLoader.pushCtx("Blockstate Properties")
                for ((key, value) in variants.entrySet()) {
                    val property = block.blockState.getProperty(key)

                    if (property == null) {
                        RegistryLoader.warn("'$key' is not a valid property of '$blockName', ignoring")
                        continue
                    }

                    RegistryLoader.pushCtx("Property: $key")

                    if (!value.isJsonPrimitive || !value.isJsonArray) {
                        RegistryLoader.warn("Value of property must be either a single value or an array of values; ignoring")
                        continue
                    }

                    if (value.isJsonPrimitive && value.asJsonPrimitive.isString) {
                        val optional = property.parseValue(value.asString)

                        if (!optional.isPresent) {
                            RegistryLoader.warn("${value.asString} is not a valid value for this property; ignoring")
                            continue
                        }

                        val comparable = optional.get()

                        out.predicates.add(Predicate { comparable == it.properties[property] })
                    } else {
                        val possibleValues = ArrayList<Comparable<*>>()
                        for (element in value.asJsonArray) {
                            if (!value.isJsonPrimitive) {
                                RegistryLoader.warn("Value of property must be either a single value or an array of values")
                                continue
                            }

                            val optional = property.parseValue(value.asString)

                            if (!optional.isPresent) {
                                RegistryLoader.warn("${value.asString} is not a valid value for this property")
                                continue
                            }

                            possibleValues.add(optional.get())
                        }

                        out.predicates.add(Predicate { state -> possibleValues.any { it == state.properties[property] } })
                    }

                    RegistryLoader.popCtx()
                }

                RegistryLoader.popCtx()
            }

            RegistryLoader.popCtx()

            return out
        }
    }
}