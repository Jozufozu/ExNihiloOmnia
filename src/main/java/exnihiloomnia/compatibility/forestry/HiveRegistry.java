package exnihiloomnia.compatibility.forestry;

import exnihiloomnia.ENO;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.Loader;

import java.util.*;

public class HiveRegistry {
    public static final Map<String, Hive> hives = new HashMap<>();
    public static final Random rand = new Random();

    public static void registerHive(Hive hive) {
        hives.put(hive.state.toString(), hive);
    }

    public static Hive getHive(Biome biome, Surroundings local, boolean canSeeSky) {
        List<Hive> found = new ArrayList<>();

        for (Hive hive : hives.values()) {
            if (hive != null) {
                if (hive.areAllRequirementsMet(biome, local, canSeeSky))
                    found.add(hive);
            }
        }

        if (!found.isEmpty()) {
            int index = rand.nextInt(found.size());

            return found.get(index);
        }

        return null;
    }

    public static void registerHives() {
        ENO.log.info("Beginning Hive Registry...");

        HiveList.generateEndHive();
        HiveList.generateJungleHive();
        HiveList.generateSnowHive();
        HiveList.generateForestHive();
        HiveList.generateMeadowHive();
        HiveList.generateDesertHive();
        HiveList.generateMarshHive();

        registerHive(HiveList.FOREST);
        registerHive(HiveList.MEADOW);
        registerHive(HiveList.DESERT);
        registerHive(HiveList.JUNGLE);
        registerHive(HiveList.END);
        registerHive(HiveList.SNOW);
        registerHive(HiveList.MARSH);

        if (Loader.isModLoaded("morebees")) {
            HiveList.generateRockHive();
            registerHive(HiveList.ROCK);
        }

        ENO.log.info("Hive Registry Completed!");
    }
}
