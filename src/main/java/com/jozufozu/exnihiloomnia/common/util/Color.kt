package com.jozufozu.exnihiloomnia.common.util

import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import net.minecraft.nbt.IntNBT

class Color {

    val r: Float
    val g: Float
    val b: Float
    val a: Float

    constructor(packed: Int, hasAlpha: Boolean) {
        this.r = (packed shr 16 and 255) / 255.0f
        this.g = (packed shr 8 and 255) / 255.0f
        this.b = (packed and 255) / 255.0f
        this.a = if (hasAlpha) (packed shr 24 and 255) / 255.0f else 1.0f
    }

    constructor(packed: Int) {
        this.r = (packed shr 16 and 255) / 255.0f
        this.g = (packed shr 8 and 255) / 255.0f
        this.b = (packed and 255) / 255.0f
        this.a = 1.0f
    }

    @JvmOverloads
    constructor(r: Float, g: Float, b: Float, a: Float = 1.0f) {
        this.r = r
        this.g = g
        this.b = b
        this.a = a
    }

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

        @Throws(JsonParseException::class)
        fun deserialize(colorString: String): Color {
            var color = 0

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
}
