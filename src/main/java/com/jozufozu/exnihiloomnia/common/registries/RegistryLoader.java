package com.jozufozu.exnihiloomnia.common.registries;

import com.google.gson.*;
import com.jozufozu.exnihiloomnia.ExNihilo;
import com.jozufozu.exnihiloomnia.common.registries.recipes.*;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.*;
import java.util.*;
import java.util.function.Function;

@Mod.EventBusSubscriber
public class RegistryLoader
{
    public static final boolean DEV_MODE = true; //Reloads all registries from assets every time
    
    public static Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private static final Logger LOGGER = LogManager.getLogger("Ex Nihilo");
    
    private static File ROOT;
    
    private static final Stack<String> CONTEXT = new Stack<>();
    
    public static void preInit()
    {
        ROOT = new File(ExNihilo.PATH, "registries/");
    }
    
    @SubscribeEvent
    public static void loadCompost(RegistryEvent.Register<CompostRecipe> event)
    {
        genericLoad(event, "/composting", CompostRecipe::deserialize);
    }
    
    @SubscribeEvent
    public static void loadFluidCrafting(RegistryEvent.Register<FluidCraftingRecipe> event)
    {
        genericLoad(event, "/fluidcrafting", FluidCraftingRecipe::deserialize);
    }
    
    @SubscribeEvent
    public static void loadFluidMixing(RegistryEvent.Register<FluidMixingRecipe> event)
    {
        genericLoad(event, "/fluidmixing", FluidMixingRecipe::deserialize);
    }
    
    @SubscribeEvent
    public static void loadFermenting(RegistryEvent.Register<FermentingRecipe> event)
    {
        genericLoad(event, "/fermenting", FermentingRecipe::deserialize);
    }
    
    @SubscribeEvent
    public static void loadSieve(RegistryEvent.Register<SieveRecipe> event)
    {
        genericLoad(event, "/sieve", SieveRecipe::deserialize);
    }
    
    @SubscribeEvent
    public static void loadHammer(RegistryEvent.Register<HammerRecipe> event)
    {
        genericLoad(event, "/hammering", HammerRecipe::deserialize);
    }
    
    @SubscribeEvent
    public static void loadMelting(RegistryEvent.Register<MeltingRecipe> event)
    {
        genericLoad(event, "/melting", MeltingRecipe::deserialize);
    }
    
    @SubscribeEvent
    public static void loadHeat(RegistryEvent.Register<HeatSource> event)
    {
        genericLoad(event, "/heat", HeatSource::deserialize);
    }
    
    private static <T extends IForgeRegistryEntry<T>> void genericLoad(RegistryEvent.Register<T> event, String resourcePath, Function<JsonObject, T> deserializer)
    {
        IForgeRegistry<T> registry = event.getRegistry();
        
        pushCtx("Registry: " + event.getName().toString());
        
        copyIfUnconfigured(resourcePath);
        
        try
        {
            Path registryRoot = new File(ROOT, resourcePath).toPath();
        
            Iterator<Path> iterator = Files.walk(registryRoot).iterator();
        
            while (iterator.hasNext())
            {
                Path filePath = iterator.next();
            
                if ("json".equals(FilenameUtils.getExtension(filePath.toString())))
                {
                    Path path2 = registryRoot.relativize(filePath);
                    String fileName = FilenameUtils.removeExtension(path2.toString()).replaceAll("\\\\", "/");
                    ResourceLocation resourceLocation = new ResourceLocation(ExNihilo.MODID, fileName);
                    BufferedReader reader = null;
                
                    try
                    {
                        try
                        {
                            reader = Files.newBufferedReader(filePath);
                            
                            pushCtx("Entry: " + resourceLocation.toString());
                            
                            T registryEntry = deserializer.apply(JsonUtils.fromJson(GSON, reader, JsonObject.class));
                            
                            registryEntry.setRegistryName(resourceLocation);
                        
                            registry.register(registryEntry);
                        }
                        catch (JsonParseException jsonparseexception)
                        {
                            error("Parsing error, " + jsonparseexception.getMessage());
                        }
                        catch (IOException ioexception)
                        {
                            error("Couldn't read " + event.getName() + " entries " + resourceLocation + " from " + filePath, ioexception);
                        }
                    }
                    finally
                    {
                        IOUtils.closeQuietly(reader);
                        popCtx();
                    }
                }
            }
        }
        catch (IOException ioException)
        {
            error("Couldn't get a list of all " + event.getName() + " registry files", ioException);
        }
        finally
        {
            clearCtx();
        }
    }
    
    public static void copyIfUnconfigured(String resourcePath)
    {
        File config = new File(ROOT, resourcePath);
        
        if (DEV_MODE)
        {
            recursiveDelete(config);
        }
        
        if (config.exists())
            return;
        
        config.mkdirs();
    
        FileSystem fileSystem = null;
        
        try
        {
            URI uri= RegistryLoader.class.getResource("/assets/exnihiloomnia").toURI();
            Path path;
        
            if ("file".equals(uri.getScheme()))
            {
                path = Paths.get(RegistryLoader.class.getResource("/assets/exnihiloomnia/registries" + resourcePath).toURI());
            }
            else
            {
                if (!"jar".equals(uri.getScheme()))
                {
                    error("Unsupported scheme " + uri);
                }
                fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
                path = fileSystem.getPath(resourcePath);
            }
        
            Iterator<Path> iterator = Files.walk(path).iterator();
        
            while (iterator.hasNext())
            {
                Path filePath = iterator.next();
            
                if ("json".equals(FilenameUtils.getExtension(filePath.toString())))
                {
                    File to = new File(config, filePath.getFileName().toString());
                    InputStream in = null;
                
                    try
                    {
                        in = new FileInputStream(filePath.toFile());
                        Files.copy(in, to.toPath());
                    }
                    finally
                    {
                        IOUtils.closeQuietly(in);
                    }
                }
            }
        }
        catch (IOException | URISyntaxException uriSyntaxException)
        {
        
        }
        finally
        {
            IOUtils.closeQuietly(fileSystem);
        }
    }
    
    private static void recursiveDelete(File file)
    {
        if (!file.exists() || !file.getAbsolutePath().contains("config"))
            return;
        
        if (file.isDirectory())
        {
            File[] files = file.listFiles();
            
            if (files == null)
                return;
            
            for (File file1 : files)
            {
                recursiveDelete(file1);
            }
            
            file.delete();
        }
        else
        {
            file.delete();
        }
    }
    
    public static void loadMesh()
    {
        FileSystem fileSystem = null;
        Gson gson = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
        
        try
        {
            URI uri= RegistryLoader.class.getResource("").toURI();
            Path path;
            
            if ("file".equals(uri.getScheme()))
            {
                path = Paths.get(RegistryLoader.class.getResource("/assets/exnihiloomnia/registries/mesh.json").toURI());
            }
            else
            {
                if (!"jar".equals(uri.getScheme()))
                {
                    error("Unsupported scheme " + uri + " trying to get mesh effectiveness table");
                }
                
                fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
                path = fileSystem.getPath("/assets/exnihiloomnia/registries/mesh.json");
            }
            
            if ("json".equals(FilenameUtils.getExtension(path.toString())))
            {
                BufferedReader reader = null;
                
                try
                {
                    try
                    {
                        reader = Files.newBufferedReader(path);
                        
                        JsonObject mesh = JsonUtils.fromJson(gson, reader, JsonObject.class);
                        
                        HashMap<String, HashMap<String, Float>> table = new HashMap<>();
                        
                        if (mesh != null)
                        {
                            Set<Map.Entry<String, JsonElement>> entries = mesh.entrySet();
                            
                            for (Map.Entry<String, JsonElement> tools : entries)
                            {
                                JsonElement toolsValue = tools.getValue();
                                if (!toolsValue.isJsonObject())
                                    continue;
                                
                                String toolName = tools.getKey();
                                
                                HashMap<String, Float> effectives = new HashMap<>();
                                
                                for (Map.Entry<String, JsonElement> effectiveness : toolsValue.getAsJsonObject().entrySet())
                                {
                                    String type = effectiveness.getKey();
                                    
                                    try
                                    {
                                        float aFloat = effectiveness.getValue().getAsJsonPrimitive().getAsFloat();
                                        effectives.put(type, aFloat);
                                    }
                                    catch (Exception e)
                                    {
                                        LOGGER.error("Could not read effectiveness for '" + toolName + "', '" + type + "'");
                                    }
                                }
                                table.put(toolName, effectives);
                            }
                        }
                        
                        JsonHelper.mesh = table;
                    }
                    catch (JsonParseException jsonparseexception)
                    {
                        error("Parsing error loading mesh.json", jsonparseexception);
                    }
                    catch (IOException ioexception)
                    {
                        error("Couldn't read mesh.json!", ioexception);
                    }
                }
                finally
                {
                    IOUtils.closeQuietly(reader);
                }
            }
        }
        catch (IOException | URISyntaxException uriSyntaxException)
        {
            error("Couldn't load mesh effectiveness table!", uriSyntaxException);
        }
        finally
        {
            IOUtils.closeQuietly(fileSystem);
            clearCtx();
        }
    }
    
    public static String pushCtx(String ctx)
    {
        return CONTEXT.push(ctx);
    }
    
    public static String popCtx()
    {
        return CONTEXT.pop();
    }
    
    public static void clearCtx()
    {
        CONTEXT.clear();
    }
    
    public static void error(String s)
    {
        StringBuilder out = new StringBuilder();
    
        for (String s1 : CONTEXT)
        {
            out.append("[").append(s1).append("]");
        }
        
        out.append(" ").append(s);
        
        LOGGER.error(out.toString());
    }
    
    public static void error(String s, Throwable t)
    {
        StringBuilder out = new StringBuilder();
        
        for (String s1 : CONTEXT)
        {
            out.append("[").append(s1).append("]");
        }
        
        out.append(" ").append(s);
        
        LOGGER.error(out.toString(), t);
    }
    
    public static void error(Throwable t)
    {
        StringBuilder out = new StringBuilder();
        
        for (String s1 : CONTEXT)
        {
            out.append("[").append(s1).append("]");
        }
        
        LOGGER.error(out.toString(), t);
    }
}
