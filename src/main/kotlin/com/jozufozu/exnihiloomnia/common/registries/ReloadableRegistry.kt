package com.jozufozu.exnihiloomnia.common.registries

import com.google.common.collect.*
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.IForgeRegistryEntry
import java.security.InvalidParameterException
import java.util.*

class ReloadableRegistry<T : IForgeRegistryEntry<T>>(
        val registryName: ResourceLocation,
        private val registryType: Class<T>,
        private val onReload: (ReloadableRegistry<T>) -> Unit) : Iterable<T> {
    private var entries: BiMap<ResourceLocation, T> = HashBiMap.create()
    private var values: MutableList<T> = Lists.newArrayList()

    init {
        if (registries.containsKey(registryName)) {
            throw InvalidParameterException("Cannot create two registries with the same name!")
        }

        registries[registryName] = this
    }

    fun clear() {
        values = Lists.newArrayList()
        entries = HashBiMap.create()
    }

    fun load() {
        clear()
        onReload(this)
    }

    fun getRegistrySuperType(): Class<T> {
        return registryType
    }

    fun register(value: T) {
        val delegate = value.registryName ?: throw IllegalStateException("Cannot register entry without a registry name!")

        require(!containsKey(delegate)) { "Entry with name '$delegate' already exists!" }

        entries[delegate] = value
        values.add(value)
    }

    fun registerAll(values: Array<T>) {
        for (value in values) {
            register(value)
        }
    }

    fun containsKey(key: ResourceLocation): Boolean {
        return entries.containsKey(key)
    }

    fun containsValue(value: T): Boolean {
        return entries.values.contains(value)
    }

    fun getValue(key: ResourceLocation): T? {
        return entries[key]
    }

    fun getKey(value: T): ResourceLocation? {
        return entries.inverse()[value]
    }

    fun getKeys(): Set<ResourceLocation> {
        return ImmutableSet.copyOf(entries.keys)
    }

    fun getValues(): List<T> {
        return ImmutableList.copyOf(entries.values)
    }

    fun getEntries(): Set<Map.Entry<ResourceLocation, T>> {
        return entries.entries
    }

    override fun iterator(): Iterator<T> {
        return object : Iterator<T> {
            var cur = 0

            override fun hasNext() = cur < values.size

            override fun next(): T = values[cur++]
        }
    }

    companion object {
        private val registries = HashBiMap.create<ResourceLocation, ReloadableRegistry<*>>()

        fun getRegistries(): Set<ReloadableRegistry<*>> {
            val out = ArrayList(registries.values)

            out.sortWith(kotlin.Comparator { reg1, reg2 -> String.CASE_INSENSITIVE_ORDER.compare(reg1.registryName.toString(), reg2.registryName.toString()) })

            return ImmutableSet.copyOf(out)
        }
    }
}
