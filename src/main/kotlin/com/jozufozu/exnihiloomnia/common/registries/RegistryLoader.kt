package com.jozufozu.exnihiloomnia.common.registries

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.jozufozu.exnihiloomnia.ExNihilo
import net.minecraft.util.JsonUtils
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.crafting.JsonContext
import net.minecraftforge.registries.IForgeRegistryEntry
import org.apache.commons.io.FilenameUtils
import org.apache.commons.io.IOUtils
import org.apache.logging.log4j.LogManager
import java.io.*
import java.net.URISyntaxException
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.*

object RegistryLoader {
    private const val DEV_MODE = true //Reloads all registries from assets every time

    val GSON: Gson = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()
    val CONTEXT = JsonContext(ExNihilo.MODID)

    private val LOGGER = LogManager.getLogger("Ex Nihilo")

    private val loggerLayers = Stack<String>()

    fun loadOres() {
        RegistryManager.ORES.load()
    }

    fun loadRecipes() {
        RegistryManager.COMPOST.load()
        RegistryManager.FLUID_CRAFTING.load()
        RegistryManager.FLUID_MIXING.load()
        RegistryManager.FERMENTING.load()
        RegistryManager.SIFTING.load()
        RegistryManager.HAMMERING.load()
        RegistryManager.MELTING.load()
        RegistryManager.HEAT.load()
    }

    fun <T : IForgeRegistryEntry<T>> genericLoad(registry: ReloadableRegistry<T>, resourcePath: String, deserializer: (JsonObject) -> T?) {
        val clear = pushCtx(registry.registryName.resourcePath)

        copyIfUnconfigured(resourcePath)

        try {
            val root = File(ExNihilo.PATH, resourcePath)

            val files = root.listFiles { _, name -> "json" == FilenameUtils.getExtension(name) } ?: return

            for (file in files) {
                val fileName = FilenameUtils.getName(FilenameUtils.removeExtension(file.name)).replace("\\\\".toRegex(), "/")
                val resourceLocation = ResourceLocation(ExNihilo.MODID, fileName)
                var reader: BufferedReader? = null

                try {
                    reader = BufferedReader(FileReader(file))

                    pushCtx(resourceLocation.resourcePath)

                    JsonUtils.fromJson(GSON, reader, JsonObject::class.java)?.run(deserializer)?.let {
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

    private fun getFullyQualifiedResourcePath(path: String) = "/data/exnihiloomnia/exnihiloomnia$path"

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
            val resource = RegistryLoader::class.java.getResource(getFullyQualifiedResourcePath(resourcePath)) ?: return

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
            val registryPath = File(RegistryLoader::class.java.getResource(getFullyQualifiedResourcePath(resourcePath)).toURI())

            val files = registryPath.listFiles { _, name -> "json" == FilenameUtils.getExtension(name) }
                    ?: throw MissingResourceException("No files to load!", "RegistryLoader", getFullyQualifiedResourcePath(resourcePath))

            for (file in files) {
                val to = File(config, FilenameUtils.getName(file.name))
                var inputStream: InputStream? = null

                try {
                    inputStream = FileInputStream(file)
                    Files.copy(inputStream, to.toPath())
                } finally {
                    IOUtils.closeQuietly(inputStream)
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
        loggerLayers.push(ctx)
        return loggerLayers.size
    }

    fun pushPopCtx(ctx: String): Int {
        popCtx()
        return pushCtx(ctx)
    }

    fun restoreCtx(size: Int) {
        while (loggerLayers.size > size) loggerLayers.pop()
    }

    fun popCtx(): String {
        return loggerLayers.pop()
    }

    fun clearCtx() {
        loggerLayers.clear()
    }

    fun error(s: String?) {
        val out = StringBuilder()

        for (s1 in loggerLayers) {
            out.append("[").append(s1).append("]")
        }

        out.append(" ").append(s)

        LOGGER.error(out.toString())
    }

    fun warn(s: String?) {
        val out = StringBuilder()

        for (s1 in loggerLayers) {
            out.append("[").append(s1).append("]")
        }

        out.append(" ").append(s)

        LOGGER.warn(out.toString())
    }

    fun error(s: String, t: Throwable) {
        val out = StringBuilder()

        for (s1 in loggerLayers) {
            out.append("[").append(s1).append("]")
        }

        out.append(" ").append(s)

        LOGGER.error(out.toString(), t)
    }

    fun error(t: Throwable) {
        val out = StringBuilder()

        for (s1 in loggerLayers) {
            out.append("[").append(s1).append("]")
        }

        LOGGER.error(out.toString(), t)
    }
}
