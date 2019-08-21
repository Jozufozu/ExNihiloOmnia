package com.jozufozu.exnihiloomnia.common.registries

import com.github.salomonbrys.kotson.*
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSyntaxException
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import java.util.function.Predicate
import com.jozufozu.exnihiloomnia.common.util.contains
import net.minecraft.block.pattern.BlockStateMatcher
import net.minecraft.tag.BlockTags
import net.minecraft.tag.Tag
import net.minecraft.tag.ItemTags
import net.minecraft.tags.BlockTags
import net.minecraft.tags.Tag
import net.minecraft.util.Identifier
import net.minecraft.util.JSONUtils
import net.minecraft.util.JsonHelper
import net.minecraft.util.ResourceLocation

class WorldIngredient: Predicate<BlockState> {
    override fun test(t: BlockState): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        val serde = jsonDeserializer<WorldIngredient> {
            val json = it.json.asJsonObject
            if ("tag" in json && "block" in json) {
                throw JsonParseException("A world ingredient entry is either a tag or an block, not both")
            }

            if ("tag" in json) {
                val resourcelocation = ResourceLocation(JSONUtils.getString(json, "tag"))
                val tag = BlockTags.getCollection().get(resourcelocation)
                return if (tag == null) {
                    throw JsonSyntaxException("Unknown item tag '$resourcelocation'")
                } else {
                    TagMatcher(tag)
                }
            }

        }
    }

    interface IStateMatcher {
        fun matches(state: BlockState)
    }

    class SingleStateMatcher(private val matcher: BlockStateMatcher): IStateMatcher {
        override fun matches(state: BlockState) {
            matcher.test(state)
        }
    }

    class TagMatcher(private val matcher: Tag<Block>): IStateMatcher {
        override fun matches(state: BlockState) {
            matcher.contains(state.block)
        }
    }
}