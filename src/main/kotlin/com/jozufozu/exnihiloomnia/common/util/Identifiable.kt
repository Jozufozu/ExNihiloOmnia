package com.jozufozu.exnihiloomnia.common.util

import net.minecraft.util.ResourceLocation

interface Identifiable<T> {
    val identifier: ResourceLocation

    @Suppress("UNCHECKED_CAST")
    val identified: T get() = this as T
}