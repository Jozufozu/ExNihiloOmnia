package com.jozufozu.exnihiloomnia.common.registries.ingredients

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import com.jozufozu.exnihiloomnia.common.lib.LibRegistries
import com.jozufozu.exnihiloomnia.common.registries.RegistryLoader
import com.jozufozu.exnihiloomnia.common.registries.ingredients.ExplicitWorldIngredient.Info
import com.jozufozu.exnihiloomnia.common.util.contains
import net.minecraft.block.Block
import net.minecraft.block.state.BlockState
import net.minecraft.item.ItemStack
import net.minecraft.util.JSONUtils
import net.minecraft.util.NonNullList
import net.minecraftforge.oredict.OreDictionary
import java.util.function.Predicate

abstract class WorldIngredient: Predicate<BlockState> {

    abstract val stacks: NonNullList<ItemStack>

    companion object {
        fun deserialize(ingredient: JsonElement): WorldIngredient {
            return when (ingredient) {
                is JsonObject -> deserialize(ingredient)
                is JsonArray -> {
                    val ingredients = ArrayList<WorldIngredient>()
                    ingredient.forEachIndexed { i, it ->
                        RegistryLoader.pushCtx(i.toString())
                        ingredients.add(deserialize(it))
                        RegistryLoader.popCtx()
                    }

                    CompoundWorldIngredient(ingredients)
                }
                else -> throw JsonSyntaxException("a world ingredient must be either a json object or an array of json objects")
            }
        }

        fun deserialize(ingredient: JsonObject): WorldIngredient {
            val isBlock = LibRegistries.BLOCK in ingredient
            val isOre = LibRegistries.OREDICT in ingredient

            if (isBlock && isOre) throw JsonSyntaxException("a world ingredient can have either \"${LibRegistries.BLOCK}\" or \"${LibRegistries.OREDICT}\", but not both")
            when {
                isOre -> return deserializeOre(ingredient)
                isBlock -> {
                    val blockName = JSONUtils.getString(ingredient, LibRegistries.BLOCK)
                    val block = Block.getBlockFromName(blockName) ?: throw JsonSyntaxException("$blockName is not a valid block")

                    val hasData = LibRegistries.DATA in ingredient
                    val hasVariants = LibRegistries.VARIANTS in ingredient

                    if (hasData && hasVariants) throw JsonSyntaxException("blockInput can have \"${LibRegistries.DATA}\" or \"${LibRegistries.VARIANTS}\", but not both")
                    return when {
                        hasData -> ExplicitWorldIngredient(block, Info.Data(JSONUtils.getInt(ingredient, LibRegistries.DATA)))
                        hasVariants -> deserializeWithBlockStateVariants(ingredient, block, blockName)
                        else -> ExplicitWorldIngredient(block)
                    }
                }
                else -> throw JsonSyntaxException("blockInput must have either \"${LibRegistries.BLOCK}\" or \"${LibRegistries.OREDICT}\"")
            }
        }

        private fun deserializeOre(ingredient: JsonObject): OreWorldIngredient {
            val oredict = JSONUtils.getString(ingredient, LibRegistries.OREDICT)

            if (!OreDictionary.doesOreNameExist(oredict)) throw JsonSyntaxException("Nothing called '$oredict' exists in the ore dictionary")

            return OreWorldIngredient(oredict)
        }

        private fun deserializeWithBlockStateVariants(ingredient: JsonObject, block: Block, blockName: String): ExplicitWorldIngredient {
            val variants = JSONUtils.getJsonObject(ingredient, LibRegistries.VARIANTS)

            val predicates = ArrayList<(BlockState) -> Boolean>()

            val ctx = RegistryLoader.pushCtx(LibRegistries.VARIANTS)
            for ((key, value) in variants.entrySet()) {
                RegistryLoader.restoreCtx(ctx)
                val property = block.blockState.getProperty(key)

                if (property == null) {
                    RegistryLoader.warn("'$key' is not a valid property of '$blockName', ignoring")
                    continue
                }

                RegistryLoader.pushCtx(key)

                if (!value.isJsonPrimitive && !value.isJsonArray) {
                    RegistryLoader.warn("Value of property must be either a single value or an array of values; ignoring")
                    continue
                }

                if (value.isJsonPrimitive) {
                    val optional = property.parseValue(value.asString)

                    if (!optional.isPresent) {
                        RegistryLoader.warn("${value.asString} is not a valid value for this property; ignoring")
                        continue
                    }

                    val comparable = optional.get()

                    predicates.add { comparable == it.properties[property] }
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

                    predicates.add { state -> possibleValues.any { it == state.properties[property] } }
                }
            }

            return ExplicitWorldIngredient(block, Info.Variants(predicates))
        }
    }
}