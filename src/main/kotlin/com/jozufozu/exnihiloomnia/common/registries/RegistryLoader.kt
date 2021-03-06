package com.jozufozu.exnihiloomnia.common.registries

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.jozufozu.exnihiloomnia.ExNihilo
import com.jozufozu.exnihiloomnia.common.ModConfig
import net.minecraft.util.JsonUtils
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.crafting.JsonContext
import net.minecraftforge.registries.IForgeRegistryEntry
import org.apache.commons.io.FilenameUtils
import org.apache.commons.io.IOUtils
import org.apache.logging.log4j.LogManager
import java.io.*
import java.net.URISyntaxException
import java.nio.file.*
import java.util.*
import kotlin.streams.asSequence


object RegistryLoader {
    val GSON: Gson = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()
    val CONTEXT = JsonContext(ExNihilo.MODID)

    private val LOGGER = LogManager.getLogger("Ex Nihilo")

    private val loggerLayers = Stack<String>()

    fun loadOres() {
        val timeThen = System.nanoTime()
        RegistryManager.ORES.load()
        val timeNow = System.nanoTime()

        clearCtx()
        LOGGER.info("Finished loading ores, took ${(timeNow - timeThen).toFloat() * 1e-9} seconds")
    }

    fun loadRecipes() {
        val timeThen = System.nanoTime()
        RegistryManager.COMPOST.load()
        RegistryManager.FLUID_CRAFTING.load()
        RegistryManager.FLUID_MIXING.load()
        RegistryManager.LEAKING.load()
        RegistryManager.FERMENTING.load()
        RegistryManager.SUMMONING.load()
        RegistryManager.SIFTING.load()
        RegistryManager.HAMMERING.load()
        RegistryManager.MELTING.load()
        RegistryManager.HEAT.load()
        RegistryManager.MESH.load()
        val timeNow = System.nanoTime()

        clearCtx()
        LOGGER.info("Finished loading all registries, took ${(timeNow - timeThen).toFloat() * 1e-9} seconds")
    }

    fun <T : IForgeRegistryEntry<T>> genericLoad(registry: ReloadableRegistry<T>, resourcePath: String, deserializer: (JsonObject) -> T?) {
        val clear = pushCtx(registry.registryName.resourcePath)

        try {
            getFilesToLoad(resourcePath) { stream, name ->
                restoreCtx(clear)
                val resourceLocation = ResourceLocation(ExNihilo.MODID, FilenameUtils.getName(FilenameUtils.removeExtension(name)).replace("\\\\".toRegex(), "/"))
                try {
                    pushCtx(resourceLocation.resourcePath)

                    JsonUtils.fromJson(GSON, stream, JsonObject::class.java)?.run(deserializer)?.let {
                        it.registryName = resourceLocation
                        registry.register(it)
                    }
                } catch (jsonparseexception: JsonParseException) {
                    error("Parsing error, ${jsonparseexception.message}")
                } catch (ioexception: IOException) {
                    error("Couldn't read '${registry.registryName}' entries '$resourceLocation' from $name", ioexception)
                } finally {
                    stream.close()
                }
            }
        } catch (ioException: Exception) {
            error("Couldn't get a list of all '${registry.registryName}' registry files", ioException)
        } finally {
            clearCtx()
        }
    }

    private fun getFilesToLoad(resourcePath: String, action: (Reader, String) -> Unit) {
        if (ModConfig.enableCraftingCustomization) {
            copyIfUnconfigured(resourcePath)

            val root = File(ExNihilo.PATH, resourcePath)

            (root.listFiles { _, name -> "json" == FilenameUtils.getExtension(name) } ?: return).map { BufferedReader(FileReader(it)) to it.name }.forEach { action(it.first, it.second) }
        } else {
            val path = getFullyQualifiedResourcePath(resourcePath)

            val uri = RegistryLoader::class.java.getResource(path).toURI()

            var fileSystem: FileSystem? = null

            val dir = when (uri.scheme) {
                "file" -> Paths.get(uri)
                "jar" -> {
                    fileSystem = FileSystems.newFileSystem(uri, emptyMap<String, Any>())
                    fileSystem.getPath(path)
                }
                else -> throw Error("Invalid uri scheme loading resources, this in a programmer error!")
            }

            Files.walk(dir).asSequence().filter { it.toString().endsWith(".json") }.map { Files.newBufferedReader(it) to it.toString() }.forEach { action(it.first, it.second) }

            IOUtils.closeQuietly(fileSystem)
        }
    }

    fun loadSingleJson(resourcePath: String, callback: (JsonObject) -> Unit): Boolean {
        val toLoad = if (ModConfig.enableCraftingCustomization) {
            val to = File(ExNihilo.PATH, resourcePath)

            if (!to.exists()) {

                var inputStream: InputStream? = null

                try {
                    val resource = RegistryLoader::class.java.getResource(getFullyQualifiedResourcePath(resourcePath))
                            ?: return false

                    val file = File(resource.toURI())

                    to.mkdirs()
                    inputStream = FileInputStream(file)
                    Files.copy(inputStream, to.toPath(), StandardCopyOption.REPLACE_EXISTING)
                } catch (e: Exception) {
                    error(e)
                    return false
                } finally {
                    IOUtils.closeQuietly(inputStream)
                }
            }

            to
        } else {
            val resource = RegistryLoader::class.java.getResource(getFullyQualifiedResourcePath(resourcePath))
                    ?: return false

            File(resource.toURI())
        }

        try {
            val reader = BufferedReader(FileReader(toLoad))

            val json = JsonUtils.fromJson(GSON, reader, JsonObject::class.java)
            if (json == null) {
                LOGGER.error("Could not load JSON file '$resourcePath'")
                return false
            }
            callback(json)
            return true
        } catch (e: FileNotFoundException) {
            LOGGER.error("Could not find resource: '$resourcePath'")
        }

        return false
    }

    private fun getFullyQualifiedResourcePath(path: String) = "/data/exnihiloomnia/exnihiloomnia$path"

    fun copyIfUnconfigured(resourcePath: String) {
        val config = File(ExNihilo.PATH, resourcePath)

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
