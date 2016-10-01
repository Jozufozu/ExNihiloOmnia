package exnihiloomnia.blocks.barrels.states;

import java.util.HashMap;

import exnihiloomnia.blocks.barrels.architecture.BarrelLogic;
import exnihiloomnia.blocks.barrels.architecture.BarrelState;
import exnihiloomnia.blocks.barrels.states.compost.BarrelStateCoarseDirt;
import exnihiloomnia.blocks.barrels.states.compost.BarrelStateCompost;
import exnihiloomnia.blocks.barrels.states.compost.BarrelStateGrass;
import exnihiloomnia.blocks.barrels.states.compost.BarrelStatePodzol;
import exnihiloomnia.blocks.barrels.states.compost.logic.CoarseDirtStateLogicComplete;
import exnihiloomnia.blocks.barrels.states.compost.logic.CoarseDirtStateTrigger;
import exnihiloomnia.blocks.barrels.states.compost.logic.CompostStateLogicComplete;
import exnihiloomnia.blocks.barrels.states.compost.logic.CompostStateLogicItems;
import exnihiloomnia.blocks.barrels.states.compost.logic.GrassStateLogicComplete;
import exnihiloomnia.blocks.barrels.states.compost.logic.GrassStateTrigger;
import exnihiloomnia.blocks.barrels.states.compost.logic.PodzolStateLogicComplete;
import exnihiloomnia.blocks.barrels.states.compost.logic.PodzolStateTrigger;
import exnihiloomnia.blocks.barrels.states.dolls.BarrelStateBlaze;
import exnihiloomnia.blocks.barrels.states.dolls.BarrelStateEnd;
import exnihiloomnia.blocks.barrels.states.dolls.logic.BlazeStateLogic;
import exnihiloomnia.blocks.barrels.states.dolls.logic.EndStateLogic;
import exnihiloomnia.blocks.barrels.states.empty.BarrelStateEmpty;
import exnihiloomnia.blocks.barrels.states.empty.logic.CompostStateTrigger;
import exnihiloomnia.blocks.barrels.states.empty.logic.EmptyStateLogic;
import exnihiloomnia.blocks.barrels.states.empty.logic.FluidStateTriggerItem;
import exnihiloomnia.blocks.barrels.states.empty.logic.FluidStateTriggerWeather;
import exnihiloomnia.blocks.barrels.states.fluid.BarrelStateFluid;
import exnihiloomnia.blocks.barrels.states.fluid.logic.*;
import exnihiloomnia.blocks.barrels.states.output.BarrelStateOutput;
import exnihiloomnia.blocks.barrels.states.output.logic.OutputStateLogicGrowingGrass;
import exnihiloomnia.blocks.barrels.states.output.logic.OutputStateLogicGrowingMycelium;
import exnihiloomnia.blocks.barrels.states.output.logic.OutputStateLogicPackingIce;
import exnihiloomnia.blocks.barrels.states.slime.BarrelStateSlime;
import exnihiloomnia.blocks.barrels.states.slime.logic.SlimeStateLogic;
import exnihiloomnia.blocks.barrels.states.witchwater.BarrelStateTransformationWitchwater;
import exnihiloomnia.blocks.barrels.states.witchwater.logic.WitchwaterStateLogic;
import net.minecraftforge.common.config.Configuration;

public class BarrelStates {
	public static HashMap<String, BarrelState> states = new HashMap<String, BarrelState>();

	//States
	public static BarrelState EMPTY;
	public static BarrelState OUTPUT;
	public static BarrelState FLUID;
	public static BarrelState COMPOST;
	public static BarrelState PODZOL;
	public static BarrelState GRASS;
	public static BarrelState COARSE_DIRT;
	public static BarrelState SLIME_GREEN;
	public static BarrelState BLAZE;
	public static BarrelState ENDERMAN;
	public static BarrelState TRANSFORM_WITCHWATER;

	//Logic
	//-EMPTY
	public static BarrelLogic EMPTY_STATE_LOGIC;
	public static BarrelLogic EMPTY_STATE_TRIGGER_COMPOST_ITEM;
	public static BarrelLogic EMPTY_STATE_TRIGGER_FLUID_ITEM;
	public static BarrelLogic EMPTY_STATE_TRIGGER_FLUID_WEATHER;

	//-OUTPUT
	public static BarrelLogic OUTPUT_STATE_LOGIC_GROWING_GRASS;
	public static BarrelLogic OUTPUT_STATE_LOGIC_GROWING_MYCELIUM;
	public static BarrelLogic OUTPUT_STATE_LOGIC_PACKING_ICE;

	//-FLUID
	public static BarrelLogic FLUID_STATE_LOGIC_HOT;
	public static BarrelLogic FLUID_STATE_LOGIC_WEATHER;
	public static BarrelLogic FLUID_STATE_LOGIC_GAS;
	public static BarrelLogic FLUID_STATE_LOGIC_ITEMS;
	public static BarrelLogic FLUID_STATE_LOGIC_ICE;
	public static BarrelLogic FLUID_STATE_LOGIC_SPREADING_MOSS;
	public static BarrelLogic FLUID_STATE_TRIGGER_CRAFTING_NETHERRACK;
	public static BarrelLogic FLUID_STATE_TRIGGER_CRAFTING_END_STONE;
	public static BarrelLogic FLUID_STATE_TRIGGER_CRAFTING_CLAY;
	public static BarrelLogic FLUID_STATE_TRIGGER_CRAFTING_SOUL_SAND;
	public static BarrelLogic FLUID_STATE_TRIGGER_CRAFTING_OBSIDIAN;
	public static BarrelLogic FLUID_STATE_TRIGGER_CRAFTING_STONE;
	public static BarrelLogic FLUID_STATE_TRIGGER_SUMMON_SLIME;
	public static BarrelLogic FLUID_STATE_TRIGGER_SUMMON_BLAZE;
	public static BarrelLogic FLUID_STATE_TRIGGER_SUMMON_ENDERMAN;
	public static BarrelLogic FLUID_STATE_TRIGGER_TRANSFORM_WITCHWATER;

	//-COMPOST
	public static BarrelLogic COMPOST_STATE_LOGIC_ITEMS;
	public static BarrelLogic COMPOST_STATE_TRIGGER_COMPLETE;
	public static BarrelLogic COMPOST_STATE_TRIGGER_PODZOL;
	public static BarrelLogic COMPOST_STATE_TRIGGER_GRASS;
	public static BarrelLogic COMPOST_STATE_TRIGGER_COARSE_DIRT;
	public static BarrelLogic PODZOL_STATE_TRIGGER_COMPLETE;
	public static BarrelLogic MYCELIUM_STATE_TRIGGER_COMPLETE;
	public static BarrelLogic GRASS_STATE_TRIGGER_COMPLETE;
	public static BarrelLogic COARSE_DIRT_STATE_TRIGGER_COMPLETE;

	//-slime
	public static BarrelLogic SLIME_STATE_LOGIC;

    //-dolls
    public static BarrelLogic BLAZE_STATE_LOGIC;
    public static BarrelLogic ENDERMAN_STATE_LOGIC;

	//-witchwater
	public static BarrelLogic WITCHWATER_STATE_LOGIC;

	//configuration options
	private static final String CATEGORY_BARREL_OPTIONS = "barrel options";
	public static boolean ALLOW_COMPOST;
	public static boolean ALLOW_RAIN_FILLING;
	public static boolean ALLOW_ICE_FORMING;
	public static boolean ALLOW_PACKED_ICE_FORMING;
	public static boolean ALLOW_CRAFTING_NETHERRACK;
	public static boolean ALLOW_CRAFTING_END_STONE;
	public static boolean ALLOW_CRAFTING_CLAY;
	public static boolean ALLOW_CRAFTING_SOUL_SAND;
	public static boolean ALLOW_CRAFTING_OBSIDIAN;
	public static boolean ALLOW_CRAFTING_STONE;
	public static boolean ALLOW_SLIME_SUMMONING;
	public static boolean ALLOW_WITCHWATER;
	public static boolean ALLOW_BLAZE;
	public static boolean ALLOW_ENDERMAN;
	public static boolean ALLOW_MOSS_SPREAD;
	public static int MOSS_SPREAD_SPEED;

	public static void configure(Configuration config) {
		loadSettings(config);

		initializeLogic();
		initializeStates();

		buildBehaviorTree();
	}

	private static void loadSettings(Configuration config) {
		ALLOW_COMPOST = config.get(CATEGORY_BARREL_OPTIONS, "allow composting", true).getBoolean(true);
		ALLOW_RAIN_FILLING = config.get(CATEGORY_BARREL_OPTIONS, "allow rain to fill barrels", true).getBoolean(true);
		ALLOW_ICE_FORMING = config.get(CATEGORY_BARREL_OPTIONS, "allow ice to form in barrels", true).getBoolean(true);
		ALLOW_PACKED_ICE_FORMING = config.get(CATEGORY_BARREL_OPTIONS, "allow packed ice to form in cold biomes", false).getBoolean(false);
		ALLOW_CRAFTING_NETHERRACK = config.get(CATEGORY_BARREL_OPTIONS, "allow creating netherrack", true).getBoolean(true);
		ALLOW_CRAFTING_END_STONE = config.get(CATEGORY_BARREL_OPTIONS, "allow creating end stone", true).getBoolean(true);
		ALLOW_CRAFTING_CLAY = config.get(CATEGORY_BARREL_OPTIONS, "allow creating clay", true).getBoolean(true);
		ALLOW_CRAFTING_SOUL_SAND = config.get(CATEGORY_BARREL_OPTIONS, "allow creating soul sand", true).getBoolean(true);
		ALLOW_CRAFTING_OBSIDIAN = config.get(CATEGORY_BARREL_OPTIONS, "allow creating obsidian", true).getBoolean(true);
		ALLOW_CRAFTING_STONE = config.get(CATEGORY_BARREL_OPTIONS, "allow creating stone and cobblestone", true).getBoolean(true);
		ALLOW_SLIME_SUMMONING = config.get(CATEGORY_BARREL_OPTIONS, "allow creating slimes", true).getBoolean(true);
		ALLOW_WITCHWATER = config.get(CATEGORY_BARREL_OPTIONS, "allow creating witchwater", true).getBoolean(true);
		ALLOW_BLAZE = config.get(CATEGORY_BARREL_OPTIONS, "allow summoning blazes", true).getBoolean(true);
		ALLOW_ENDERMAN = config.get(CATEGORY_BARREL_OPTIONS, "allow summoning endermen", true).getBoolean(true);
		ALLOW_MOSS_SPREAD = config.get(CATEGORY_BARREL_OPTIONS, "allow moss spreading", true).getBoolean(true);
		MOSS_SPREAD_SPEED = config.get(CATEGORY_BARREL_OPTIONS, "moss speed factor", 1).getInt(1);
	}

	private static void initializeLogic() {
		EMPTY_STATE_LOGIC = new EmptyStateLogic();
		EMPTY_STATE_TRIGGER_COMPOST_ITEM = new CompostStateTrigger();
		EMPTY_STATE_TRIGGER_FLUID_ITEM = new FluidStateTriggerItem();
		EMPTY_STATE_TRIGGER_FLUID_WEATHER = new FluidStateTriggerWeather();

		OUTPUT_STATE_LOGIC_GROWING_GRASS = new OutputStateLogicGrowingGrass();
		OUTPUT_STATE_LOGIC_GROWING_MYCELIUM = new OutputStateLogicGrowingMycelium();
		OUTPUT_STATE_LOGIC_PACKING_ICE = new OutputStateLogicPackingIce();

		FLUID_STATE_LOGIC_HOT = new FluidStateLogicHot();
		FLUID_STATE_LOGIC_WEATHER = new FluidStateLogicRain();
		FLUID_STATE_LOGIC_GAS = new FluidStateLogicGas();
		FLUID_STATE_LOGIC_ITEMS = new FluidStateLogicItems();
		FLUID_STATE_LOGIC_ICE = new FluidStateLogicFreezingIce();
		FLUID_STATE_LOGIC_SPREADING_MOSS = new FluidStateLogicSpreadingMoss();
		FLUID_STATE_TRIGGER_CRAFTING_NETHERRACK = new FluidCraftNetherrackTrigger();
		FLUID_STATE_TRIGGER_CRAFTING_END_STONE = new FluidCraftEndstoneTrigger();
		FLUID_STATE_TRIGGER_CRAFTING_CLAY = new FluidCraftClayTrigger();
		FLUID_STATE_TRIGGER_CRAFTING_SOUL_SAND = new FluidCraftSoulsandTrigger();
		FLUID_STATE_TRIGGER_CRAFTING_OBSIDIAN = new FluidCraftObsidianTrigger();
		FLUID_STATE_TRIGGER_CRAFTING_STONE = new FluidCraftStoneTrigger();
		FLUID_STATE_TRIGGER_SUMMON_SLIME = new FluidSummonSlimeTrigger();
		FLUID_STATE_TRIGGER_SUMMON_BLAZE = new FluidSummonBlazeTrigger();
		FLUID_STATE_TRIGGER_SUMMON_ENDERMAN = new FluidSummonEndermanTrigger();
		FLUID_STATE_TRIGGER_TRANSFORM_WITCHWATER = new FluidTransformWitchwater();

		COMPOST_STATE_LOGIC_ITEMS = new CompostStateLogicItems();
		COMPOST_STATE_TRIGGER_COMPLETE = new CompostStateLogicComplete();
		COMPOST_STATE_TRIGGER_PODZOL = new PodzolStateTrigger();
		COMPOST_STATE_TRIGGER_GRASS = new GrassStateTrigger();
		COMPOST_STATE_TRIGGER_COARSE_DIRT = new CoarseDirtStateTrigger();
		PODZOL_STATE_TRIGGER_COMPLETE = new PodzolStateLogicComplete();
		GRASS_STATE_TRIGGER_COMPLETE = new GrassStateLogicComplete();
		COARSE_DIRT_STATE_TRIGGER_COMPLETE = new CoarseDirtStateLogicComplete();

		SLIME_STATE_LOGIC = new SlimeStateLogic();

		BLAZE_STATE_LOGIC = new BlazeStateLogic();
		ENDERMAN_STATE_LOGIC = new EndStateLogic();

		WITCHWATER_STATE_LOGIC = new WitchwaterStateLogic();
	}

	private static void initializeStates() {
		EMPTY = new BarrelStateEmpty();
		OUTPUT = new BarrelStateOutput();
		FLUID = new BarrelStateFluid();
		COMPOST = new BarrelStateCompost();
		PODZOL = new BarrelStatePodzol();
		GRASS = new BarrelStateGrass();
		COARSE_DIRT = new BarrelStateCoarseDirt();
		SLIME_GREEN = new BarrelStateSlime();
		BLAZE = new BarrelStateBlaze();
		ENDERMAN = new BarrelStateEnd();

		TRANSFORM_WITCHWATER = new BarrelStateTransformationWitchwater();

		BarrelStates.registerState(EMPTY);
		BarrelStates.registerState(OUTPUT);
		BarrelStates.registerState(FLUID);
		BarrelStates.registerState(COMPOST);
		BarrelStates.registerState(PODZOL);
		BarrelStates.registerState(GRASS);
		BarrelStates.registerState(COARSE_DIRT);
		BarrelStates.registerState(SLIME_GREEN);
		BarrelStates.registerState(BLAZE);
		BarrelStates.registerState(ENDERMAN);
		BarrelStates.registerState(TRANSFORM_WITCHWATER);
	}

	private static void buildBehaviorTree() {
		BarrelStates.EMPTY.addLogic(EMPTY_STATE_LOGIC);

		if (ALLOW_COMPOST)
			BarrelStates.EMPTY.addLogic(EMPTY_STATE_TRIGGER_COMPOST_ITEM);
		
		BarrelStates.EMPTY.addLogic(EMPTY_STATE_TRIGGER_FLUID_ITEM);
		
		if (ALLOW_RAIN_FILLING)
			BarrelStates.EMPTY.addLogic(EMPTY_STATE_TRIGGER_FLUID_WEATHER);

		BarrelStates.OUTPUT.addLogic(OUTPUT_STATE_LOGIC_GROWING_GRASS);
		BarrelStates.OUTPUT.addLogic(OUTPUT_STATE_LOGIC_GROWING_MYCELIUM);

		BarrelStates.FLUID.addLogic(FLUID_STATE_LOGIC_HOT);
		
		if (ALLOW_RAIN_FILLING)
			BarrelStates.FLUID.addLogic(FLUID_STATE_LOGIC_WEATHER);
		if (ALLOW_ICE_FORMING)
			BarrelStates.FLUID.addLogic(FLUID_STATE_LOGIC_ICE);
		if (ALLOW_PACKED_ICE_FORMING)
			BarrelStates.OUTPUT.addLogic(OUTPUT_STATE_LOGIC_PACKING_ICE);

		if (ALLOW_MOSS_SPREAD)
			BarrelStates.FLUID.addLogic(FLUID_STATE_LOGIC_SPREADING_MOSS);
		
		BarrelStates.FLUID.addLogic(FLUID_STATE_LOGIC_GAS);
		BarrelStates.FLUID.addLogic(FLUID_STATE_LOGIC_ITEMS);
		
		if (ALLOW_CRAFTING_NETHERRACK)
			BarrelStates.FLUID.addLogic(FLUID_STATE_TRIGGER_CRAFTING_NETHERRACK);
		if (ALLOW_CRAFTING_END_STONE)
			BarrelStates.FLUID.addLogic(FLUID_STATE_TRIGGER_CRAFTING_END_STONE);
		if (ALLOW_CRAFTING_CLAY)
			BarrelStates.FLUID.addLogic(FLUID_STATE_TRIGGER_CRAFTING_CLAY);
		if (ALLOW_CRAFTING_SOUL_SAND)
			BarrelStates.FLUID.addLogic(FLUID_STATE_TRIGGER_CRAFTING_SOUL_SAND);
		if (ALLOW_CRAFTING_OBSIDIAN)
			BarrelStates.FLUID.addLogic(FLUID_STATE_TRIGGER_CRAFTING_OBSIDIAN);
		if (ALLOW_CRAFTING_STONE)
			BarrelStates.FLUID.addLogic(FLUID_STATE_TRIGGER_CRAFTING_STONE);
		if (ALLOW_SLIME_SUMMONING)
			BarrelStates.FLUID.addLogic(FLUID_STATE_TRIGGER_SUMMON_SLIME);
		
        if (ALLOW_BLAZE) {
			BarrelStates.FLUID.addLogic(FLUID_STATE_TRIGGER_SUMMON_BLAZE);
			BarrelStates.BLAZE.addLogic(BLAZE_STATE_LOGIC);
			BarrelStates.BLAZE.addLogic(FLUID_STATE_LOGIC_HOT);
        }
        
        if (ALLOW_ENDERMAN) {
			BarrelStates.FLUID.addLogic(FLUID_STATE_TRIGGER_SUMMON_ENDERMAN);
			BarrelStates.ENDERMAN.addLogic(ENDERMAN_STATE_LOGIC);
		}

		if (ALLOW_WITCHWATER) {
			BarrelStates.FLUID.addLogic(FLUID_STATE_TRIGGER_TRANSFORM_WITCHWATER);
			BarrelStates.TRANSFORM_WITCHWATER.addLogic(WITCHWATER_STATE_LOGIC);
		}

		if (ALLOW_COMPOST) {
			BarrelStates.COMPOST.addLogic(COMPOST_STATE_LOGIC_ITEMS);
			BarrelStates.COMPOST.addLogic(COMPOST_STATE_TRIGGER_COMPLETE);
			BarrelStates.COMPOST.addLogic(COMPOST_STATE_TRIGGER_PODZOL);
			BarrelStates.COMPOST.addLogic(COMPOST_STATE_TRIGGER_GRASS);
			BarrelStates.COMPOST.addLogic(COMPOST_STATE_TRIGGER_COARSE_DIRT);

			BarrelStates.PODZOL.addLogic(COMPOST_STATE_LOGIC_ITEMS);
			BarrelStates.PODZOL.addLogic(COMPOST_STATE_TRIGGER_COARSE_DIRT);
			BarrelStates.PODZOL.addLogic(PODZOL_STATE_TRIGGER_COMPLETE);

			BarrelStates.GRASS.addLogic(COMPOST_STATE_LOGIC_ITEMS);
			BarrelStates.GRASS.addLogic(COMPOST_STATE_TRIGGER_COARSE_DIRT);
			BarrelStates.GRASS.addLogic(GRASS_STATE_TRIGGER_COMPLETE);

			BarrelStates.COARSE_DIRT.addLogic(COMPOST_STATE_LOGIC_ITEMS);
			BarrelStates.COARSE_DIRT.addLogic(COARSE_DIRT_STATE_TRIGGER_COMPLETE);
		}

		BarrelStates.SLIME_GREEN.addLogic(SLIME_STATE_LOGIC);
	}

	public static void registerState(BarrelState state) {
		if (state != null) {
			String key = state.getUniqueIdentifier();

			if (key != null && !key.isEmpty() && !key.trim().isEmpty() && !states.containsKey(key)) {
				states.put(key, state);
			}
		}
	}

	public static void unregisterState(BarrelState state) {
		states.remove(state.getUniqueIdentifier());
	}

	public static BarrelState getState(String key) {
		return states.get(key);
	}
}
