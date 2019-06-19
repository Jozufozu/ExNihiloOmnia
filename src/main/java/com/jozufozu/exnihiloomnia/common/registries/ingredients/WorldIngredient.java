package com.jozufozu.exnihiloomnia.common.registries.ingredients;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Predicate;

public class WorldIngredient implements Predicate<BlockState>, Comparable<WorldIngredient> {
    private final Block block;
    private final List<String> requirements;

    public WorldIngredient(@NotNull Block block, @NotNull List<String> requirements) {
        this.block = block;
        this.requirements = requirements;
    }

    @Override
    public boolean test(BlockState state) {
        if (state == null || state.getBlock() != this.block || this.requirements.isEmpty())
        {
            return false;
        }
        else
        {
            String stateString = state.toString();
            for (String requirement : this.requirements)
            {
                if (!stateString.contains(requirement))
                    return false;
            }

            return true;
        }
    }

    public int compareTo(@NotNull WorldIngredient other)
    {
        return this.block == other.block ? this.requirements.size() - other.requirements.size() : Block.(this.block) - Block.getIdFromBlock(other.block);
    }

    @NotNull
    public NonNullList getStacks() {
        return NonNullList.withSize(0, ItemStack.EMPTY);
    }

    @NotNull
    public static WorldIngredient deserialize(@NotNull JsonObject json) {
            Intrinsics.checkParameterIsNotNull(json, "json");
            Block block = Block.REGISTRY.getObject(new ResourceLocation(JsonUtils.getString(json, "id")));
            List args;
            if (json.has("data"))
            {
                int meta = JsonUtils.getInt(json, "data");
                String string = block.getStateFromMeta(meta).toString();
                args = Lists.newArrayList(string.substring(string.indexOf('['), string.length()-1).split(","));
                return new WorldIngredient(block, args);
            }
            else if (json.has("variants"))
            {
                JsonObject variants = JsonUtils.getJsonObject(json, "variants");
                Set set = variants.entrySet();
                if (!set.isEmpty()) {
                    Iterable $receiver$iv = (Iterable)set;
                    Collection destination$iv$iv = (Collection)(new ArrayList());
                    Iterator var9 = $receiver$iv.iterator();

                    Object element$iv$iv;
                    Entry $k_v;
                    while(var9.hasNext()) {
                        boolean var23;
                        label34: {
                            element$iv$iv = var9.next();
                            $k_v = (Entry)element$iv$iv;
                            JsonElement v = (JsonElement)$k_v.getValue();
                            Intrinsics.checkExpressionValueIsNotNull(v, "v");
                            if (v.isJsonPrimitive()) {
                                JsonPrimitive var10000 = v.getAsJsonPrimitive();
                                Intrinsics.checkExpressionValueIsNotNull(var10000, "v.asJsonPrimitive");
                                if (var10000.isString()) {
                                    var23 = true;
                                    break label34;
                                }
                            }

                            var23 = false;
                        }

                        if (var23) {
                            destination$iv$iv.add(element$iv$iv);
                        }
                    }

                    $receiver$iv = (Iterable)((List)destination$iv$iv);
                    destination$iv$iv = (Collection)(new ArrayList(CollectionsKt.collectionSizeOrDefault($receiver$iv, 10)));
                    var9 = $receiver$iv.iterator();

                    while(var9.hasNext()) {
                        element$iv$iv = var9.next();
                        $k_v = (Entry)element$iv$iv;
                        String k = (String)$k_v.getKey();
                        JsonElement v = (JsonElement)$k_v.getValue();
                        String var19 = "" + k + '=' + v;
                        destination$iv$iv.add(var19);
                    }

                    args = (List)destination$iv$iv;
                    Intrinsics.checkExpressionValueIsNotNull(block, "block");
                    return new WorldIngredient(block, args);
                }
            }

            Intrinsics.checkExpressionValueIsNotNull(block, "block");
            return new WorldIngredient(block, CollectionsKt.emptyList());
        }
}
