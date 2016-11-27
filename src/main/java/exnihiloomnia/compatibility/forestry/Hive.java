package exnihiloomnia.compatibility.forestry;

import exnihiloomnia.ENO;
import exnihiloomnia.compatibility.ENOCompatibility;
import forestry.core.genetics.alleles.EnumAllele;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

import java.util.ArrayList;
import java.util.List;

public class Hive {
    public int defaultSpawnBonus = 0;
    public EnumAllele.Flowers flowers = null;

    public IBlockState requiredSubstrate = null;
    private static final int REQUIRED_SUBSTRATE_COUNT = 15;

    public Boolean requiredCanSeeSky = null;
    public Boolean requiresTree = null;
    public String requiresBlockAbove = null;

    public List<Type> biomeTypes = new ArrayList<Type>();

    public final IBlockState state;

    public Hive(Block block, int meta) {
        this.state = block.getStateFromMeta(meta);
    }

    public boolean areAllRequirementsMet(Biome biome, Surroundings local, boolean canSeeSky) {

        if (requiredCanSeeSky != null && requiredCanSeeSky && !canSeeSky)
            return false;

        if (requiresBlockAbove != null && !requiresBlockAbove.equals(local.blockAbove))
            return false;

        if (requiresTree != null && requiresTree && local.leafCount < 20)
            return false;

        if (requiredSubstrate != null) {
            int substrateCount = 0;
            if (local.blocks.containsKey(requiredSubstrate))
                substrateCount = local.blocks.get(requiredSubstrate);

            if (substrateCount < REQUIRED_SUBSTRATE_COUNT)
                return false;
        }

        if (!biomeTypes.isEmpty() && ENOCompatibility.biome_required) {
            Type[] types = BiomeDictionary.getTypesForBiome(biome);


            for (Type type : biomeTypes) {
                boolean found = false;

                for (Type type1 : types) {
                    if (type1 == type)
                        found = true;
                }

                if (!found)
                    return false;
            }
        }

        if (flowers != null) {

            int flowerCount = local.getFlowerCount(flowers);

            if (flowerCount == 0)
                return false;
        }

        return true;
    }

    public int getSpawnChanceModifier(Surroundings local) {
        int flowerCount = local.getFlowerCount(flowers);

        return defaultSpawnBonus + flowerCount;
    }


    public void printDebug(Biome biome, Surroundings local, boolean canSeeSky) {
        String[] print = new String[15];
        int i = 3;

        print[0] = "";
        print[1] = "Hive: " + state;
        print[2] = "-------------------------------------------";

        if (requiredCanSeeSky != null) {
            print[i] = "Can see sky: " + (requiredCanSeeSky && canSeeSky);
            i++;
        }

        if (requiresBlockAbove != null) {
            print[i] = "Has Block Above: " + (requiresBlockAbove.equals(local.blockAbove));
            i++;
        }

        if (requiresTree != null) {
            print[i] = "Is In Tree: " + (requiresTree && local.leafCount > 20);
            i++;
        }

        if (requiredSubstrate != null) {

            int substrateCount = 0;
            if (local.blocks.containsKey(requiredSubstrate))
                substrateCount = local.blocks.get(requiredSubstrate);

            print[i] = "Has required substrate: " + (substrateCount > REQUIRED_SUBSTRATE_COUNT);

            i++;
        }

        if (!biomeTypes.isEmpty()) {
            Type[] types = BiomeDictionary.getTypesForBiome(biome);
            boolean found = false;

            for (Type type : biomeTypes) {
                for (Type type1 : types) {
                    if (type1 == type)
                        found = true;
                }
            }

            print[i] = "In Correct Biome: " + found;
            i++;
        }

        if (flowers != null) {

            int flowerCount = local.getFlowerCount(flowers);

            print[i] = "Has Flowers: " + (flowerCount > 0);
            i++;
        }

        for (String string : print) {
            if (string != null)
                ENO.log.info(string);
        }
    }
}

