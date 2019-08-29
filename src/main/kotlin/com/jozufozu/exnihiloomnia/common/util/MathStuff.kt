package com.jozufozu.exnihiloomnia.common.util

object MathStuff {
    fun lerp(f1: Float, f2: Float, fraction: Float): Float {
        return (1.0f - fraction) * f1 + f2 * fraction
    }

    fun lerp(f1: Double, f2: Double, fraction: Double): Double {
        return (1.0 - fraction) * f1 + f2 * fraction
    }
}
