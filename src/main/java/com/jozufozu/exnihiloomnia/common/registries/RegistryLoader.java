package com.jozufozu.exnihiloomnia.common.registries;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
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
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.MissingResourceException;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.function.Function;

@Mod.EventBusSubscriber(modid = ExNihilo.MODID)
public class RegistryLoader
{
    public static final boolean DEV_MODE = true; //Reloads all registries from assets every time
    
    public static Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private static final Logger LOGGER = LogManager.getLogger("Ex Nihilo");
    
    private static final Stack<String> CONTEXT = new Stack<>();
    
    @SubscribeEvent
    public static void loadCompost(RegistryEvent.Register<CompostRecipe> event)
    {
        genericLoad(event, "/registries/composting", CompostRecipe::deserialize);
    }
    
    @SubscribeEvent
    public static void loadFluidCrafting(RegistryEvent.Register<FluidCraftingRecipe> event)
    {
        genericLoad(event, "/registries/fluidcrafting", FluidCraftingRecipe::deserialize);
    }
    
    @SubscribeEvent
    public static void loadFluidMixing(RegistryEvent.Register<FluidMixingRecipe> event)
    {
        genericLoad(event, "/registries/fluidmixing", FluidMixingRecipe::deserialize);
    }
    
    @SubscribeEvent
    public static void loadFermenting(RegistryEvent.Register<FermentingRecipe> event)
    {
        genericLoad(event, "/registries/fermenting", FermentingRecipe::deserialize);
    }
    
    @SubscribeEvent
    public static void loadSieve(RegistryEvent.Register<SieveRecipe> event)
    {
        genericLoad(event, "/registries/sieve", SieveRecipe::deserialize);
    }
    
    @SubscribeEvent
    public static void loadHammer(RegistryEvent.Register<HammerRecipe> event)
    {
        genericLoad(event, "/registries/hammering", HammerRecipe::deserialize);
    }
    
    @SubscribeEvent
    public static void loadMelting(RegistryEvent.Register<MeltingRecipe> event)
    {
        genericLoad(event, "/registries/melting", MeltingRecipe::deserialize);
    }
    
    @SubscribeEvent
    public static void loadHeat(RegistryEvent.Register<HeatSource> event)
    {
        genericLoad(event, "/registries/heat", HeatSource::deserialize);
    }
    
    private static <T extends IForgeRegistryEntry<T>> void genericLoad(RegistryEvent.Register<T> event, String resourcePath, Function<JsonObject, T> deserializer)
    {
        IForgeRegistry<T> registry = event.getRegistry();
        
        pushCtx("Registry: " + event.getName().toString());
        
        copyIfUnconfigured(resourcePath);
        
        try
        {
            File root = new File(ExNihilo.PATH, resourcePath);
    
            File[] files = root.listFiles((dir, name) -> "json".equals(FilenameUtils.getExtension(name)));
            
            if (files == null)
            {
                return;
            }
            
            for (File file : files)
            {
                String fileName = FilenameUtils.getName(FilenameUtils.removeExtension(file.getName())).replaceAll("\\\\", "/");
                ResourceLocation resourceLocation = new ResourceLocation(ExNihilo.MODID, fileName);
                BufferedReader reader = null;
    
                try
                {
                    try
                    {
                        reader = new BufferedReader(new FileReader(file));
            
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
                        error("Couldn't read " + event.getName() + " entries " + resourceLocation + " from " + file.getPath(), ioexception);
                    }
                }
                finally
                {
                    IOUtils.closeQuietly(reader);
                    popCtx();
                }
            }
        }
        catch (Exception ioException)
        {
            error("Couldn't get a list of all " + event.getName() + " registry files", ioException);
        }
        finally
        {
            clearCtx();
        }
    }
    
    public static void loadSingleJson(String resourcePath, Consumer<JsonObject> toDo)
    {
        File toLoad = new File(ExNihilo.PATH, resourcePath);
    
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(toLoad));
    
            toDo.accept(JsonUtils.fromJson(GSON, reader, JsonObject.class));
        }
        catch (FileNotFoundException e)
        {
            LOGGER.error("Could not find resource: '" + resourcePath + "'");
        }
    }
    
    public static void copySingle(String resourcePath)
    {
        File to = new File(ExNihilo.PATH, resourcePath);
    
        if (DEV_MODE)
        {
            recursiveDelete(to);
        }
        
        if (to.exists())
        {
            return;
        }
        
        InputStream in = null;
        
        try
        {
            URL resource = RegistryLoader.class.getResource("/assets/exnihiloomnia" + resourcePath);
            
            if (resource == null)
            {
                return;
            }
            
            File file = new File(resource.toURI());
            
            in = new FileInputStream(file);
            Files.copy(in, to.toPath());
        }
        catch (IOException | URISyntaxException e)
        {
        
        }
        finally
        {
            IOUtils.closeQuietly(in);
        }
    }
    
    public static void copyIfUnconfigured(String resourcePath)
    {
        File config = new File(ExNihilo.PATH, resourcePath);
        
        if (DEV_MODE)
        {
            recursiveDelete(config);
        }
        
        if (config.exists())
            return;
        
        config.mkdirs();
        
        try
        {
            File registryPath = new File(RegistryLoader.class.getResource("/assets/exnihiloomnia" + resourcePath).toURI());
    
            File[] files = registryPath.listFiles((dir, name) -> "json".equals(FilenameUtils.getExtension(name)));
            
            if (files == null)
            {
                throw new MissingResourceException("No files to load!", "RegistryLoader", "/assets/exnihiloomnia" + resourcePath);
            }
    
            for (File file : files)
            {
                File to = new File(config, FilenameUtils.getName(file.getName()));
                InputStream in = null;
    
                try
                {
                    in = new FileInputStream(file);
                    Files.copy(in, to.toPath());
                }
                finally
                {
                    IOUtils.closeQuietly(in);
                }
            }
        }
        catch (IOException | URISyntaxException uriSyntaxException)
        {
            error("Error copying assets to config: " + uriSyntaxException.getMessage());
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
