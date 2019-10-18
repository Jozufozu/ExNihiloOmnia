package com.jozufozu.exnihiloomnia.common.commands

import com.jozufozu.exnihiloomnia.common.registries.RegistryManager
import com.jozufozu.exnihiloomnia.common.registries.ReloadableRegistry
import net.minecraft.command.CommandBase
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import net.minecraft.command.SyntaxErrorException
import net.minecraft.server.MinecraftServer
import net.minecraft.util.math.BlockPos
import java.util.*

class CommandRegistry : CommandBase() {
    override fun getName(): String {
        return "enoregistry"
    }

    override fun getUsage(sender: ICommandSender): String {
        return "commands.exnihiloomnia.enoreg.usage"
    }

    @Throws(CommandException::class)
    override fun execute(server: MinecraftServer, sender: ICommandSender, args: Array<String>) {
        if (args.size != 2) {
            throw SyntaxErrorException("commands.exnihiloomnia.enoreg.usage")
        }

        val all = "all" == args[1]

        val start = System.nanoTime()

        var count = 0

        for (registry in ReloadableRegistry.getRegistries()) {
            if (registry == RegistryManager.ORES) continue

            val resourcePath = registry.registryName.resourcePath
            if (all || resourcePath in args.slice(1 until args.size)) {
                if ("reload" == args[0]) {
                    registry.load()
                } else if ("clear" == args[0]) {
                    registry.clear()
                }
                count++
            }
        }

        val seconds = (System.nanoTime().toDouble() - start) * 1E-9

        CommandBase.notifyCommandListener(sender, this, "commands.exnihiloomnia.enoreg.done", count, seconds)
    }

    override fun getTabCompletions(server: MinecraftServer, sender: ICommandSender, args: Array<String>, targetPos: BlockPos?): List<String> {
        if (args.size == 1) {
            return CommandBase.getListOfStringsMatchingLastWord(args, "reload", "clear")
        }
        if (args.size == 2) {
            val completions = ArrayList<String>()
            completions.add("all")

            for (registry in ReloadableRegistry.getRegistries()) {
                completions.add(registry.registryName.resourcePath)
            }

            return CommandBase.getListOfStringsMatchingLastWord(args, completions)
        }

        return super.getTabCompletions(server, sender, args, targetPos)
    }
}
