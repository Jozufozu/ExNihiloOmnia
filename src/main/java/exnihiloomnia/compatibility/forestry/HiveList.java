package exnihiloomnia.compatibility.forestry;

import forestry.apiculture.PluginApiculture;
import forestry.core.genetics.alleles.EnumAllele;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class HiveList {

    public static Block beehives = PluginApiculture.blocks.beehives;

    public static Hive FOREST;
    public static Hive MEADOW;
    public static Hive DESERT;
    public static Hive JUNGLE;
    public static Hive END;
    public static Hive SNOW;
    public static Hive MARSH;

    // MoreBees
    public static Hive ROCK;

    public static void generateForestHive() {
        FOREST = new Hive(beehives, 0);

        FOREST.requiredCanSeeSky = true;
        FOREST.requiresTree = true;

        FOREST.biomeTypes.add(BiomeDictionary.Type.FOREST);
        FOREST.defaultSpawnBonus = 20;
    }

    public static void generateMeadowHive() {
        MEADOW = new Hive(beehives, 1);

        MEADOW.requiredCanSeeSky = true;

        MEADOW.biomeTypes.add(BiomeDictionary.Type.PLAINS);
        MEADOW.flowers = EnumAllele.Flowers.VANILLA;
        MEADOW.defaultSpawnBonus = 20;
    }

    public static void generateDesertHive() {
        DESERT = new Hive(beehives, 2);

        DESERT.requiredCanSeeSky = true;

        DESERT.biomeTypes.add(BiomeDictionary.Type.SANDY);

        DESERT.flowers = EnumAllele.Flowers.CACTI;
    }

    public static void generateJungleHive() {
        JUNGLE = new Hive(beehives, 3);

        JUNGLE.requiredCanSeeSky = true;
        JUNGLE.requiresTree = true;

        JUNGLE.biomeTypes.add(BiomeDictionary.Type.JUNGLE);
        JUNGLE.flowers = EnumAllele.Flowers.JUNGLE;
    }

    public static void generateEndHive() {
        END = new Hive(beehives, 4);
        END.requiredCanSeeSky = true;
        END.requiredSubstrate =  Blocks.END_STONE.getDefaultState();
        END.biomeTypes.add(BiomeDictionary.Type.END);
        END.defaultSpawnBonus = -40;
    }

    public static void generateSnowHive() {
        SNOW = new Hive(beehives, 5);

        SNOW.requiredCanSeeSky = true;
        SNOW.requiredSubstrate = Blocks.SNOW.getDefaultState();

        SNOW.biomeTypes.add(BiomeDictionary.Type.COLD);
    }

    public static void generateMarshHive() {
        MARSH = new Hive(beehives, 6);

        MARSH.requiredCanSeeSky = true;
        MARSH.flowers = EnumAllele.Flowers.MUSHROOMS;

        MARSH.biomeTypes.add(BiomeDictionary.Type.SWAMP);
    }

    public static void generateRockHive() {
        Block moreBeesHives = GameRegistry.findBlock("morebees", "hive");

        if (moreBeesHives != null) {
            ROCK = new Hive(moreBeesHives, 0);

            ROCK.requiredCanSeeSky = false;
            ROCK.requiredSubstrate = Blocks.STONE.getDefaultState();

            ROCK.biomeTypes.add(BiomeDictionary.Type.HILLS);
        }
    }
}
