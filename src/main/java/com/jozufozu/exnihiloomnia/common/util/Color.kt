package com.jozufozu.exnihiloomnia.common.util

import com.github.salomonbrys.kotson.jsonDeserializer
import com.github.salomonbrys.kotson.jsonSerializer
import com.github.salomonbrys.kotson.typeAdapter
import com.google.gson.*
import net.minecraft.nbt.IntNBT
import net.minecraft.util.JSONUtils
import java.lang.reflect.Type

class Color(val r: Float, val g: Float, val b: Float, val a: Float = 1.0f) {
    constructor(packed: Int, hasAlpha: Boolean = false) : this(
            (packed shr 16 and 255) / 255.0f,
            (packed shr 8 and 255) / 255.0f,
            (packed and 255) / 255.0f,
            if (hasAlpha) (packed shr 24 and 255) / 255.0f else 1.0f)

    fun toInt(): Int {
        var i = 0

        i = i or ((this.r * 255).toInt() shl 16)
        i = i or ((this.g * 255).toInt() shl 8)
        i = i or (this.b * 255).toInt()
        i = i or ((this.a * 255).toInt() shl 24)

        return i
    }

    fun withAlpha(alpha: Float): Color {
        return Color(r, g, b, a * alpha)
    }

    fun average(other: Color): Color {
        return average(this, other)
    }

    fun serializeNBT(): IntNBT {
        return IntNBT(toInt())
    }

    companion object {
        val WHITE = Color(0xFFFFFF)

        fun deserialize(nbt: IntNBT): Color {
            return Color(nbt.int, true)
        }

        fun average(c1: Color, c2: Color): Color {
            return weightedAverage(c1, c2, 0.5f)
        }

        /**
         * Finds the weighted average of two colors
         * @param percentage between 0 and 1. 0 mean it will return c1, 0.5 indicates a normal average, 1 mean it will return c2
         */
        fun weightedAverage(from: Color, to: Color, percentage: Float): Color {
            val r = MathStuff.lerp(from.r, to.r, percentage)
            val g = MathStuff.lerp(from.g, to.g, percentage)
            val b = MathStuff.lerp(from.b, to.b, percentage)
            val a = MathStuff.lerp(from.a, to.a, percentage)

            return Color(r, g, b, a)
        }

        val serde = typeAdapter<Color> {
            jsonDeserializer {
                var color = 0

                if (it.json.isJsonPrimitive) (it.json as? JsonPrimitive)?.asString?.let { colorString ->
                    if (colorString.contains(",")) {
                        val rgba = colorString.replace(" ", "").split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                        if (rgba.size > 4 || rgba.size < 3)
                            throw JsonSyntaxException("Could not parse color '$colorString'")

                        color = color or (Integer.parseInt(rgba[0]) shl 16)
                        color = color or (Integer.parseInt(rgba[1]) shl 8)
                        color = color or Integer.parseInt(rgba[2])

                        val alpha = rgba.size == 4
                        if (alpha) {
                            color = color or (Integer.parseInt(rgba[3]) shl 24)
                        }

                        return@jsonDeserializer Color(color, alpha)
                    } else {
                        if (colorString.length == 8 || colorString.length == 6) {
                            color = Integer.parseInt(colorString, 16)

                            return@jsonDeserializer Color(color, colorString.length == 8)
                        }
                    }

                    throw JsonSyntaxException("Could not parse color '$colorString'")
                }

                throw JsonSyntaxException("Expected a string")
            }

            jsonSerializer<Color> {
                JsonPrimitive(Integer.toHexString(it.src.toInt()))
            }
        }
    }
}
