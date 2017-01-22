package exnihiloomnia;

import net.minecraftforge.common.config.Configuration;

public class ENOConfig {
    public static final String MISC = "misc";
    public static final String AUTOMATION = "automation";

    public static boolean classic_sieve;
    public static double string_chance;
    public static double silkworm_chance;
    public static boolean sieve_automation;
    public static boolean crucible_access;
    public static boolean fancy_crucible;
    public static boolean annoying_sifter;

    public static boolean universal_bucket;

    public static boolean end_cake;
    public static boolean end_cake_hunger;

    public static boolean pretty_leaves;

    public static float sifter_strength;

    public static void configure(Configuration config) {
        universal_bucket = config.get(Configuration.CATEGORY_GENERAL, "universal porcelain bucket", true).getBoolean();
        classic_sieve = config.get("legacy options", "enable classic sieve", false).getBoolean();

        end_cake = config.get(ENOConfig.MISC, "enable end cake", true).setRequiresMcRestart(true).getBoolean();
        end_cake_hunger = config.get(ENOConfig.MISC, "end cake can be eaten on a full stomach", false).getBoolean();

        annoying_sifter = config.get(ENOConfig.MISC, "sifter has small knockback effect", false).getBoolean();
        sifter_strength = config.getFloat("sifter knockback strength", ENOConfig.MISC, 0.03f, 0, 1, "don't get too crazy");
        string_chance = config.get(ENOConfig.MISC, "infested leaves string chance", .6d).getDouble();
        silkworm_chance = config.get(ENOConfig.MISC, "infested leaves silkworm chance", .5d).getDouble();

        fancy_crucible = config.get(ENOConfig.MISC, "fancy crucible renderer", true).getBoolean();

        sieve_automation = config.get(AUTOMATION, "sieve hopper/pipe interaction", false).getBoolean();
        crucible_access = config.get(AUTOMATION, "crucibles can only interact with pipes from the top", true).getBoolean();

        pretty_leaves = config.get(Configuration.CATEGORY_GENERAL, "fancy infested leaves renderer", false, "Renders the block the silkworms have infested if true, false renders colored oak leaves").setRequiresMcRestart(true).getBoolean();
    }
}
