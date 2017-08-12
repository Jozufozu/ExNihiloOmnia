package com.jozufozu.exnihiloomnia.proxy;

import com.jozufozu.exnihiloomnia.ExNihilo;
import com.jozufozu.exnihiloomnia.advancements.ExNihiloTriggers;
import com.jozufozu.exnihiloomnia.common.blocks.crucible.TileEntityCrucible;
import com.jozufozu.exnihiloomnia.common.blocks.sieve.TileEntitySieve;
import com.jozufozu.exnihiloomnia.common.entity.EntityThrownStone;
import com.jozufozu.exnihiloomnia.common.items.ExNihiloMaterials;
import com.jozufozu.exnihiloomnia.common.items.tools.ItemMesh;
import com.jozufozu.exnihiloomnia.common.lib.LibMisc;
import com.jozufozu.exnihiloomnia.common.registries.ReloadableRegistry;
import com.jozufozu.exnihiloomnia.common.world.WorldProviderSkyblock;
import com.jozufozu.exnihiloomnia.common.world.WorldTypeSkyblock;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.io.File;

@Mod.EventBusSubscriber(modid = ExNihilo.MODID)
public class CommonProxy
{
    public void preInit(FMLPreInitializationEvent event)
    {
        ExNihilo.PATH = new File(event.getModConfigurationDirectory().getAbsolutePath() + File.separator + "exnihiloomnia" + File.separator);
    
        ConfigManager.sync(ExNihilo.MODID, Config.Type.INSTANCE);
        
        ExNihiloMaterials.preInit();
        ExNihiloTriggers.preInit();
        
        TileEntity.register("exnihiloomnia:sieve", TileEntitySieve.class);
        TileEntity.register("exnihiloomnia:crucible", TileEntityCrucible.class);
    
        EntityRegistry.registerModEntity(LibMisc.ENTITY_STONE, EntityThrownStone.class, "thrown_stone", 0, ExNihilo.INSTANCE, 64, 3, true);
    
        WorldTypeSkyblock.SKY_BLOCK = new WorldTypeSkyblock();
        ItemMesh.loadMeshTable();
        WorldProviderSkyblock.preInit();
    }
    
    public void init(FMLInitializationEvent event)
    {
        ExNihiloMaterials.init();
    
        for (ReloadableRegistry registry : ReloadableRegistry.getRegistries())
        {
            registry.load();
        }
    }
    
    public void postInit(FMLPostInitializationEvent event)
    {
    
    }
}
