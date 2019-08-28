package com.jozufozu.exnihiloomnia.common.registries.ingredients

import com.google.common.collect.Maps
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import com.jozufozu.exnihiloomnia.common.lib.LibRegistries
import com.jozufozu.exnihiloomnia.common.registries.RegistryLoader
import com.jozufozu.exnihiloomnia.common.registries.RegistryManager
import com.jozufozu.exnihiloomnia.common.util.contains
import net.minecraft.block.Block
import net.minecraft.block.properties.IProperty
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.JsonUtils
import net.minecraft.util.NonNullList
import java.util.*

import java.util.function.Predicate
import java.util.logging.LogManager
import kotlin.collections.ArrayList

class WorldIngredient(private val block: Block, private val data: Optional<Int> = Optional.empty()) : Predicate<IBlockState>, Comparable<WorldIngredient> {
    private val predicates = ArrayList<Predicate<IBlockState>>()

    val stacks: NonNullList<ItemStack> get() = if (Item.getItemFromBlock(block).hasSubtypes) {
        if (data.isPresent) NonNullList.from(ItemStack.EMPTY, ItemStack(block, 1, data.get()))
        else {
            NonNullList.create<ItemStack>().also {
                for (i in 0..16) {
                    if (test(block.getStateFromMeta(i)))
                        it.add(ItemStack(block, 1, i))
                }
            }
        }
    } else NonNullList.from(ItemStack.EMPTY, ItemStack(block, 1))

    override fun test(state: IBlockState): Boolean {
        if (state.block === this.block) {
            if (this.predicates.isEmpty()) {
                return true
            } else {
                for (predicate in predicates) {
                    if (!predicate.test(state)) {
                        return false
                    }
                }

                return true
            }
        } else {
            return false
        }
    }

    override fun compareTo(other: WorldIngredient): Int {
        return if (block === other.block) {
            if (data.isPresent)
                if (other.data.isPresent) data.get() - other.data.get()
                else -other.predicates.size
            else if (other.data.isPresent)
                predicates.size
            else
                predicates.size - other.predicates.size
        }
        else
            Block.getIdFromBlock(block) - Block.getIdFromBlock(other.block)
    }

    companion object {
        fun deserialize(json: JsonObject): WorldIngredient {
            RegistryLoader.pushCtx("Input")
            val blockName = JsonUtils.getString(json, LibRegistries.ID)
            val block = Block.getBlockFromName(blockName) ?: throw JsonSyntaxException("$blockName is not a valid block")

            if (LibRegistries.DATA in json && LibRegistries.VARIANTS in json) throw JsonSyntaxException("blockInput can have \"data\" or \"variants\", but not both")

            var out = WorldIngredient(block)

            if (LibRegistries.DATA in json) {
                val data = JsonUtils.getInt(json, LibRegistries.DATA)

                out = WorldIngredient(block, Optional.of(data))

            } else if (LibRegistries.VARIANTS in json) {
                val variants = JsonUtils.getJsonObject(json, LibRegistries.VARIANTS)

                out = WorldIngredient(block)

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
