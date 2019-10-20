package com.jozufozu.exnihiloomnia.common.commands

import com.google.gson.stream.JsonWriter
import com.jozufozu.exnihiloomnia.ExNihilo
import com.jozufozu.exnihiloomnia.common.lib.LibRegistries
import com.jozufozu.exnihiloomnia.common.ores.ItemType
import com.jozufozu.exnihiloomnia.common.ores.OreManager
import com.jozufozu.exnihiloomnia.common.registries.ores.Ore
import net.minecraft.command.CommandBase
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import net.minecraft.server.MinecraftServer
import java.io.File
import java.io.FileWriter

class CommandOreStuffGeneration : CommandBase() {
    override fun getName(): String {
        return "enorecipegen"
    }

    override fun getUsage(sender: ICommandSender): String {
        return "commands.exnihiloomnia.enorecipegen.usage"
    }

    @Throws(CommandException::class)
    override fun execute(server: MinecraftServer, sender: ICommandSender, args: Array<String>) {
        val hammerRoot = File(ExNihilo.PATH, "generated/hammering/").also { it.mkdirs() }
        val siftingRoot = File(ExNihilo.PATH, "generated/sifting/").also { it.mkdirs() }

        for ((ore, typeMap) in OreManager.items) {
            for (itemType in typeMap.keys) {
                if (itemType == ItemType.INGOT) continue

                generateHammerRecipe(hammerRoot, ore, itemType)
                generateSieveRecipe(siftingRoot, ore, itemType)
            }
        }
    }

    private fun generateHammerRecipe(root: File, ore: Ore, itemType: ItemType) {
        val blockType = itemType.blockEquivalent ?: return

        val blockName = ore.getName(blockType).toString()

        val writer = JsonWriter(FileWriter(File(root, "ore_generated_${blockName.replace(':', '_')}.json"), false))
        writer.setIndent("    ")

        with(writer) {
            beginObject()

            name(LibRegistries.WORLD_INPUT)
            beginObject()
            name(LibRegistries.BLOCK)
            value(blockName)
            endObject()

            name(LibRegistries.REWARDS)
            beginArray()
            for ((n, c) in arrayOf(Pair(4, 1f), Pair(1, 0.1f))) {
                beginObject()

                name(LibRegistries.ITEM)
                value(ore.getName(itemType).toString())

                name(LibRegistries.COUNT)
                value(n)

                name(LibRegistries.CHANCE)
                value(c)

                endObject()
            }
            endArray()

            endObject()

            close()
        }
    }

    private fun generateSieveRecipe(root: File, ore: Ore, itemType: ItemType) {
        val siftedFrom = itemType.siftedFrom.value ?: return

        val itemName = ore.getName(itemType).toString()
        val writer = JsonWriter(FileWriter(File(root, "ore_generated_${itemName.replace(':', '_')}.json"), false))

        writer.setIndent("    ")

        with(writer) {
            beginObject()

            name(LibRegistries.INPUT)
            beginObject()
            name(LibRegistries.ITEM)
            value(siftedFrom.item.registryName.toString())

            if (siftedFrom.hasSubtypes) {
                name(LibRegistries.DATA)
                value(siftedFrom.itemDamage)
            }

            endObject()

            name(LibRegistries.REWARDS)
            beginArray()
            beginObject()

            name(LibRegistries.ITEM)
            value(itemName)

            name(LibRegistries.CHANCE)
            value(0.07)

            endObject()
            endArray()

            endObject()

            close()
        }
    }
}
