package exnihiloomnia.registries.ore;

import exnihiloomnia.util.Color;

public class OreRegistryEntry {

    private final String name;
    private final Color color;
    private final int rarity;
    private final boolean hasGravel;
    private final boolean hasNether;
    private final boolean hasIngot;

    private final boolean hasEnd;

    public OreRegistryEntry(String name, Color color, int rarity, boolean hasGravel, boolean hasNether, boolean hasEnd, boolean hasIngot) {
        this.name = name;
        this.color = color;
        this.hasGravel = hasGravel;
        this.hasEnd = hasEnd;
        this.hasNether= hasNether;
        this.rarity = rarity;
        this.hasIngot = hasIngot;
    }

    public OreRegistryEntry(String name, String color, int rarity, boolean hasGravel, boolean hasNether, boolean hasEnd, boolean hasIngot) {
        this(name, new Color(color), rarity, hasGravel, hasNether, hasEnd, hasIngot);
    }

    public String getName() {
        return name;
    }

    public String getOreDictName(String pre) {
        String name1 = name.substring(0, 1).toUpperCase() + name.substring(1);
        return pre + name1;
    }

    public Color getColor() {
        return color;
    }

    public int getRarity() {
        return rarity;
    }

    public boolean hasGravel() {
        return hasGravel;
    }

    public boolean hasNether() {
        return hasNether;
    }

    public boolean hasEnd() {
        return hasEnd;
    }

    public boolean hasIngot() {
        return hasIngot;
    }
}
