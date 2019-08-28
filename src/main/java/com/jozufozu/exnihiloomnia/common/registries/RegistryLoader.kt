package com.jozufozu.exnihiloomnia.common.registries

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.jozufozu.exnihiloomnia.ExNihilo
import com.jozufozu.exnihiloomnia.common.registries.recipes.*
import net.minecraft.util.JsonUtils
import net.minecraft.util.ResourceLocation
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.registries.IForgeRegistryEntry
import org.apache.commons.io.FilenameUtils
import org.apache.commons.io.IOUtils
import org.apache.logging.log4j.LogManager

import java.io.*
import java.net.URISyntaxException
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.MissingResourceException
import java.util.Stack

object RegistryLoader {
    val DEV_MODE = true //Reloads all registries from assets every time

    var GSON = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()
    private val LOGGER = LogManager.getLogger("Ex Nihilo")

    private val CONTEXT = Stack<String>()

    fun loadOres() {
        genericLoad(RegistryManager.ORES, "/registries/ores", Ore.Serde::deserialize)
    }

    fun loadRecipes() {
        genericLoad(RegistryManager.COMPOST, "/registries/composting", CompostRecipe.Serde::deserialize)
        genericLoad(RegistryManager.FLUID_CRAFTING, "/registries/fluidcrafting", FluidCraftingRecipe.Serde::deserialize)
        genericLoad(RegistryManager.FLUID_MIXING, "/registries/fluidmixing", FluidMixingRecipe.Serde::deserialize)
        genericLoad(RegistryManager.FERMENTING, "/registries/fermenting", FermentingRecipe.Serde::deserialize)
        genericLoad(RegistryManager.SIFTING, "/registries/sieve", SieveRecipe.Serde::deserialize)
        genericLoad(RegistryManager.HAMMERING, "/registries/hammering", HammerRecipe.Serde::deserialize)
        genericLoad(RegistryManager.MELTING, "/registries/melting", MeltingRecipe.Serde::deserialize)
        genericLoad(RegistryManager.HEAT, "/registries/heat", HeatSource.Serde::deserialize)
    }

    private fun <T : IForgeRegistryEntry<T>> genericLoad(registry: ReloadableRegistry<T>, resourcePath: String, deserializer: (JsonObject) -> T) {
        val clear = pushCtx("Registry: ${registry.registryName}")

        copyIfUnconfigured(resourcePath)

        try {
            val root = File(ExNihilo.PATH, resourcePath)

            val files = root.listFiles { dir, name -> "json" == FilenameUtils.getExtension(name) } ?: return

            for (file in files) {
                val fileName = FilenameUtils.getName(FilenameUtils.removeExtension(file.name)).replace("\\\\".toRegex(), "/")
                val resourceLocation = ResourceLocation(ExNihilo.MODID, fileName)
                var reader: BufferedReader? = null

                try {
                    reader = BufferedReader(FileReader(file))

                    pushCtx("Entry: $resourceLocation")

                    JsonUtils.fromJson(GSON, reader, JsonObject::class.java)?.let(deserializer)?.let {
                        it.registryName = resourceLocation
                        registry.register(it)
                    }
                } catch (jsonparseexception: JsonParseException) {
                    error("Parsing error, ${jsonparseexception.message}")
                } catch (ioexception: IOException) {
                    error("Couldn't read '${registry.registryName}' entries '$resourceLocation' from ${file.path}", ioexception)
                } finally {
                    IOUtils.closeQuietly(reader)
                    restoreCtx(clear)
                }
            }
        } catch (ioException: Exception) {
            error("Couldn't get a list of all '${registry.registryName}' registry files", ioException)
        } finally {
            clearCtx()
        }
    }

    fun loadSingleJson(resourcePath: String, toDo: (JsonObject) -> Unit) {
        val toLoad = File(ExNihilo.PATH, resourcePath)

        try {
            val reader = BufferedReader(FileReader(toLoad))

            val json = JsonUtils.fromJson(GSON, reader, JsonObject::class.java)
            if (json == null) {
                LOGGER.error("Could not load JSON file '$resourcePath'")
                return
            }
            toDo(json)
        } catch (e: FileNotFoundException) {
            LOGGER.error("Could not find resource: '$resourcePath'")
        }

    }

    fun copySingle(resourcePath: String) {
        val to = File(ExNihilo.PATH, resourcePath)

        if (DEV_MODE) {
            recursiveDelete(to)
        }

        if (to.exists()) {
            return
        }

        var inputStream: InputStream? = null

        try {
            val resource = RegistryLoader::class.java.getResource("/assets/exnihiloomnia$resourcePath") ?: return

            val file = File(resource.toURI())

            to.mkdirs()
            inputStream = FileInputStream(file)
            Files.copy(inputStream, to.toPath(), StandardCopyOption.REPLACE_EXISTING)
        } catch (e: Exception) {
            error(e)
        } finally {
            IOUtils.closeQuietly(inputStream)
        }
    }

    fun copyIfUnconfigured(resourcePath: String) {
        val config = File(ExNihilo.PATH, resourcePath)

        if (DEV_MODE) {
            recursiveDelete(config)
        }

        if (config.exists())
            return

        config.mkdirs()

        try {
            val registryPath = File(RegistryLoader::class.java.getResource("/assets/exnihiloomnia$resourcePath").toURI())

            val files = registryPath.listFiles { dir, name -> "json" == FilenameUtils.getExtension(name) }
                    ?: throw MissingResourceException("No files to load!", "RegistryLoader", "/assets/exnihiloomnia$resourcePath")

            for (file in files) {
                val to = File(config, FilenameUtils.getName(file.name))
                var `in`: InputStream? = null

                try {
                    `in` = FileInputStream(file)
                    Files.copy(`in`, to.toPath())
                } finally {
                    IOUtils.closeQuietly(`in`)
                }
            }
        } catch (uriSyntaxException: IOException) {
            error("Error copying assets to config: ${uriSyntaxException.message}")
        } catch (uriSyntaxException: URISyntaxException) {
            error("Error copying assets to config: ${uriSyntaxException.message}")
        }

    }

    private fun recursiveDelete(file: File) {
        if (!file.exists() || !file.absolutePath.contains("config"))
            return

        if (file.isDirectory) {
            val files = file.listFiles() ?: return

            for (file1 in files) {
                recursiveDelete(file1)
            }

            file.delete()
        } else {
            file.delete()
        }
    }

    fun pushCtx(ctx: String): Int {
        CONTEXT.push(ctx)
        return CONTEXT.size
    }

    fun restoreCtx(size: Int) {
        while (CONTEXT.size > size) CONTEXT.pop()
    }

    fun popCtx(): String {
        return CONTEXT.pop()
    }

    fun clearCtx() {
        CONTEXT.clear()
    }

    fun error(s: String?) {
        val out = StringBuilder()

        for (s1 in CONTEXT) {
            out.append("[").append(s1).append("]")
        }

        out.append(" ").append(s)

        LOGGER.error(out.toString())
    }

    fun warn(s: String?) {
        val out = StringBuilder()

        for (s1 in CONTEXT) {
            out.append("[").append(s1).append("]")
        }

        out.append(" ").append(s)

        LOGGER.warn(out.toString())
    }

    fun error(s: String, t: Throwable) {
        val out = StringBuilder()

        for (s1 in CONTEXT) {
            out.append("[").append(s1).append("]")
        }

        out.append(" ").append(s)

        LOGGER.error(out.toString(), t)
    }

    fun error(t: Throwable) {
        val out = StringBuilder()

        for (s1 in CONTEXT) {
            out.append("[").append(s1).append("]")
        }

        LOGGER.error(out.toString(), t)
    }
}
