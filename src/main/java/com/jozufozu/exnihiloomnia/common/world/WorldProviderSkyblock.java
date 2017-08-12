package com.jozufozu.exnihiloomnia.common.world;

import com.jozufozu.exnihiloomnia.ExNihilo;
import com.jozufozu.exnihiloomnia.common.registries.RegistryLoader;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProviderSurface;
import net.minecraftforge.common.DimensionManager;

public class WorldProviderSkyblock extends WorldProviderSurface
{
    public static void preInit()
    {
        RegistryLoader.copyIfUnconfigured("/spawn_island");
        try
        {
            DimensionManager.unregisterDimension(0);
            DimensionManager.registerDimension(0, DimensionType.register("Void Overworld", "overworld", 0, WorldProviderSkyblock.class, true));
        }
        catch (Exception e)
        {
            ExNihilo.log.error("Failed to hijack world provider for the Overworld.");
        }
    }
    
    @Override
    public BlockPos getRandomizedSpawnPoint()
    {
        if (world.getWorldInfo().getTerrainType() == WorldTypeSkyblock.SKY_BLOCK)
        {
            return world.getSpawnPoint();
        }
        return super.getRandomizedSpawnPoint();
    }
}
