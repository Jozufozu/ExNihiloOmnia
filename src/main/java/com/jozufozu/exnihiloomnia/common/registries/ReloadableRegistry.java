package com.jozufozu.exnihiloomnia.common.registries;

import com.google.common.collect.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.security.InvalidParameterException;
import java.util.*;

public class ReloadableRegistry<T extends IForgeRegistryEntry<T>> implements IForgeRegistry<T>
{
    private static final BiMap<ResourceLocation, ReloadableRegistry> registries = HashBiMap.create();
    
    public final ResourceLocation registryName;
    private final Class<T> registryType;
    private BiMap<ResourceLocation, T> entries = HashBiMap.create();
    private List<T> values = Lists.newArrayList();
    
    public static <T extends IForgeRegistryEntry<T>> ReloadableRegistry<T> create(Class<T> registryType, ResourceLocation registryName)
    {
        if (registries.containsKey(registryName))
        {
            throw new InvalidParameterException("Cannot create two registries with the same name!");
        }
    
        ReloadableRegistry<T> registry = new ReloadableRegistry<>(registryName, registryType);
        registries.put(registryName, registry);
        
        return registry;
    }
    
    public static Set<ReloadableRegistry> getRegistries()
    {
        ArrayList<ReloadableRegistry> out = new ArrayList<>(registries.values());
        
        out.sort((reg1, reg2) -> String.CASE_INSENSITIVE_ORDER.compare(reg1.registryName.toString(), reg2.registryName.toString()));
        
        return ImmutableSet.copyOf(out);
    }
    
    private ReloadableRegistry(ResourceLocation registryName, Class<T> registryType)
    {
        this.registryName = registryName;
        this.registryType = registryType;
    }
    
    public void clear()
    {
        values = Lists.newArrayList();
        entries = HashBiMap.create();
    }
    
    public void load()
    {
        clear();
        MinecraftForge.EVENT_BUS.post(new RegistryEvent.Register<>(registryName, this));
    }
    
    @Override
    public Class<T> getRegistrySuperType()
    {
        return registryType;
    }
    
    @Override
    public void register(T value)
    {
        ResourceLocation delegate = value.getRegistryName();
        
        if (delegate == null)
        {
            throw new IllegalStateException("Cannot register entry without a registry name!");
        }
        
        if (containsKey(delegate))
        {
            throw new IllegalArgumentException("Entry with name '" + delegate + "' already exists!");
        }
        
        entries.put(delegate, value);
        values.add(value);
    }
    
    @Override
    public void registerAll(T[] values)
    {
        for (T value : values)
        {
            register(value);
        }
    }
    
    @Override
    public boolean containsKey(ResourceLocation key)
    {
        return entries.containsKey(key);
    }
    
    @Override
    public boolean containsValue(T value)
    {
        return entries.values().contains(value);
    }
    
    @Nullable
    @Override
    public T getValue(ResourceLocation key)
    {
        return entries.get(key);
    }
    
    @Nullable
    @Override
    public ResourceLocation getKey(T value)
    {
        return entries.inverse().get(value);
    }
    
    @Nonnull
    @Override
    public Set<ResourceLocation> getKeys()
    {
        return ImmutableSet.copyOf(entries.keySet());
    }
    
    @Nonnull
    @Override
    public List<T> getValues()
    {
        return ImmutableList.copyOf(entries.values());
    }
    
    @Nonnull
    @Override
    public Set<Map.Entry<ResourceLocation, T>> getEntries()
    {
        return entries.entrySet();
    }
    
    @Override
    public <T1> T1 getSlaveMap(ResourceLocation slaveMapName, Class<T1> type)
    {
        return null;
    }
    
    @Override
    public Iterator<T> iterator()
    {
        return new Iterator<T>()
        {
            int cur = -1;
            T next = null;
            { next(); }
        
            @Override
            public boolean hasNext()
            {
                return next != null;
            }
        
            @Override
            public T next()
            {
                T ret = next;
                
                cur++;
                
                if (cur >= values.size())
                    next = null;
                else
                    next = values.get(cur);
                
                return ret;
            }
        };
    }
}
