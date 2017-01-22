package exnihiloomnia.world;

import exnihiloomnia.ENO;
import exnihiloomnia.world.generation.WorldProviderVoidEnd;
import exnihiloomnia.world.generation.WorldProviderVoidHell;
import exnihiloomnia.world.generation.WorldProviderVoidSurface;
import exnihiloomnia.world.generation.templates.TemplateLoader;
import exnihiloomnia.world.generation.templates.defaults.TemplateExNihiloEasy;
import exnihiloomnia.world.generation.templates.defaults.TemplateExNihiloHard;
import exnihiloomnia.world.generation.templates.defaults.TemplateExNihiloModerate;
import exnihiloomnia.world.generation.templates.defaults.TemplateSkyblock21;
import exnihiloomnia.world.generation.templates.pojos.Template;
import exnihiloomnia.world.manipulation.Moss;
import exnihiloomnia.world.manipulation.Mycelium;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ENOWorld {
	public static final String CATEGORY_WORLD_GEN = "world generation";
	public static final String CATEGORY_WORLD_MOD = "world manipulation";
	
	private static Template template_overworld;
	private static Template template_nether;
	private static Template template_end;
	
	private static boolean gen_surface;
	private static boolean gen_nether;
	private static boolean gen_nether_allow_fortresses;
	private static boolean gen_end;
	private static boolean gen_end_allow_crystals;
	private static String surface_biome;

	private static boolean moss_spreads;
	private static boolean moss_spread_with_rain;
	private static boolean moss_spread_near_water;
	private static int moss_light_level;
	private static int moss_spread_speed;
	private static boolean mycelium_sprout_with_rain;
	private static int mycelium_sprout_speed;

	public static void configure(Configuration config) {
		gen_surface = config.get(CATEGORY_WORLD_GEN, "void overworld", false).getBoolean(false);
		surface_biome = config.get(CATEGORY_WORLD_GEN, "overworld biome", "all").getString();
		gen_nether = config.get(CATEGORY_WORLD_GEN, "void nether", false).getBoolean(false);
		gen_nether_allow_fortresses = config.get(CATEGORY_WORLD_GEN, "void nether generate fortresses", true).getBoolean(true);
		gen_end = config.get(CATEGORY_WORLD_GEN, "void end", false).getBoolean(false);
		gen_end_allow_crystals = config.get(CATEGORY_WORLD_GEN, "void end generate crystals", true).getBoolean(true);
		
		TemplateSkyblock21.generate(getTemplatePath());
		TemplateExNihiloEasy.generate(getTemplatePath());
		TemplateExNihiloModerate.generate(getTemplatePath());
		TemplateExNihiloHard.generate(getTemplatePath());
		
		String template_overworld_name = config.get(CATEGORY_WORLD_GEN, "void overworld template", "skyblock_2-1_overworld.json").getString();
		String template_nether_name = config.get(CATEGORY_WORLD_GEN, "void nether template", "skyblock_2-1_nether.json").getString();
		String template_end_name = config.get(CATEGORY_WORLD_GEN, "void end template", "none").getString();
		
		if (!template_overworld_name.equals("none") && template_overworld_name.trim().length() > 0)
			template_overworld = TemplateLoader.load(ENO.path + File.separator + "templates" + File.separator + template_overworld_name);
		
		if (!template_nether_name.equals("none") && template_nether_name.trim().length() > 0)
			template_nether = TemplateLoader.load(ENO.path + File.separator + "templates" + File.separator + template_nether_name);
		
		if (!template_end_name.equals("none") && template_end_name.trim().length() > 0)
			template_end = TemplateLoader.load(ENO.path + File.separator + "templates" + File.separator + template_end_name);
		
		moss_spreads = config.get(CATEGORY_WORLD_MOD, "can moss spread at all", true).getBoolean(true);
		moss_spread_with_rain = config.get(CATEGORY_WORLD_MOD, "moss spreads when raining", true).getBoolean(true);
		moss_spread_near_water = config.get(CATEGORY_WORLD_MOD, "moss spreads near water", true).getBoolean(true);
		moss_light_level = config.get(CATEGORY_WORLD_MOD, "moss max light level", 8).getInt();
		moss_spread_speed = config.get(CATEGORY_WORLD_MOD, "moss spread speed", Moss.DEFAULT_GROWTH_SPEED).getInt();
		mycelium_sprout_with_rain = config.get(CATEGORY_WORLD_MOD, "mycelium sprouts when raining", true).getBoolean(true);
		mycelium_sprout_speed = config.get(CATEGORY_WORLD_MOD, "mycelium sprout speed", Mycelium.DEFAULT_GROWTH_SPEED).getInt();
		
		Moss.setGrowth(moss_spread_speed);
		Moss.setSpreadsWhileRaining(moss_spread_with_rain);
		Mycelium.setGrowth(mycelium_sprout_speed);
		Mycelium.setSpreadsWhileRaining(mycelium_sprout_with_rain);
	}


	public static boolean mossSpreadsNearWater() {
		return moss_spread_near_water;
	}

	public static int getMossLightLevel() {
		return moss_light_level;
	}

	public static String getTemplatePath() {
		return ENO.path + File.separator + "templates";
	}
	
	public static Template getOverworldTemplate() {
		return template_overworld;
	}
	
	public static Template getNetherTemplate() {
		return template_nether;
	}
	
	public static boolean getNetherFortressesAllowed() {
		return gen_nether_allow_fortresses;
	}
	
	public static Template getEndTemplate() {
		return template_end;
	}
	
	public static boolean getEndCrystalsAllowed() {
		return gen_end_allow_crystals;
	}

	public static String getSurfaceBiome() {
		return surface_biome;
	}

	public static void registerWorldProviders() {
		if (gen_end)
			hijackEndGeneration();
		
		if (gen_surface)
			hijackSurfaceGeneration();
		
		if (gen_nether)
			hijackNetherGeneration();
	}
	
	private static void hijackEndGeneration() {
		try {
			DimensionManager.unregisterDimension(1);
			DimensionManager.registerDimension(1, DimensionType.register("Void End", "_end", 1, WorldProviderVoidEnd.class, true));
		}
		catch (Exception e) {
			ENO.log.error("Failed to hijack world provider for the End.");
		}
	}
	
	private static void hijackSurfaceGeneration() {
		try {
			DimensionManager.unregisterDimension(0);
			DimensionManager.registerDimension(0, DimensionType.register("Void Overworld", "", 0, WorldProviderVoidSurface.class, true));
		}
		catch (Exception e) {
			ENO.log.error("Failed to hijack world provider for the Overworld.");
		}
	}
	
	private static void hijackNetherGeneration() {
		try {
			DimensionManager.unregisterDimension(-1);
			DimensionManager.registerDimension(-1, DimensionType.register("Void Nether", "_nether", -1, WorldProviderVoidHell.class, true));
		}
		catch (Exception e) {
			ENO.log.error("Failed to hijack world provider for the Nether.");
		}
	}
	
	public static boolean isWorldGenerationOverridden(int dimension) {
		switch (dimension) {
			case -1:
				return gen_nether;
				
			case 0:
				return gen_surface;
				
			case 1:
				return gen_end;
				
			default:
				return false;
		}
	}
	
	public static void load(World world) {
		int x = (int)(world.getWorldInfo().getSpawnX() / world.provider.getMovementFactor() / 16);
        int z = (int)(world.getWorldInfo().getSpawnZ() / world.provider.getMovementFactor() / 16);
        
        world.getChunkProvider().provideChunk(x, z);  
	}
	
	public static void tick(World world) {
		//if (true) return;
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();

		if (server.isServerRunning() && !world.isRemote && world.playerEntities.size() > 0 && world.provider.getDimension() != -11325) {

			int distance = server.getPlayerList().getViewDistance();

			List<ChunkPos> processed = new ArrayList<>();

			for (EntityPlayer player : world.playerEntities) {
				final int maxX = player.chunkCoordX + distance / 2;
				final int maxZ = player.chunkCoordZ + distance / 2;

				int x = player.chunkCoordX - distance / 2;
				int z = player.chunkCoordZ - distance / 2;
				for (; x < maxX; x++) {
					for (; z < maxZ; z++) {
						if (world.getChunkProvider().getLoadedChunk(x, z) != null) {
							ChunkPos pos = new ChunkPos(x, z);

							if (!processed.contains(pos)) {
								if (moss_spreads)
									Moss.grow(world, x, z);
								Mycelium.grow(world, x, z);

								processed.add(pos);
							}
						}
					}
				}
			}
		}
	}
}
