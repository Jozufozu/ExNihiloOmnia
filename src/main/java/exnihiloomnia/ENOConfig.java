package exnihiloomnia;

import net.minecraftforge.common.config.Configuration;

public class ENOConfig {
    public static boolean classic_sieve;
    public static double string_chance;
    public static boolean sieve_automation;
    public static boolean crucible_access;
    public static boolean fancy_crucible;
    public static boolean annoying_sifter;
    public static boolean end_cake;
    public static float sifter_strength;

    public static void configure(Configuration config) {
        classic_sieve = config.get("legacy options", "enable classic sieve", false).getBoolean(false);
        end_cake = config.get("misc", "enable end cake", true).getBoolean(true);

        annoying_sifter = config.get("misc", "sifter has small knockback effect", false).getBoolean(false);
        sifter_strength = config.getFloat("sifter knockback strength", "misc", 0.03f, 0, 1, "don't get too crazy");
        string_chance = config.get("misc", "infested leaves string chance", .6d).getDouble();

        fancy_crucible = config.get("misc", "fancy crucible renderer", true).getBoolean(true);

        sieve_automation = config.get("automation", "sieve hopper/pipe interaction", false).getBoolean(false);
        crucible_access = config.get("automation", "crucibles can only interact with pipes from the top", true).getBoolean(true);
    }
}
