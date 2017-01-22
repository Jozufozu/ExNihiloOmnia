package exnihiloomnia.registries.ore.pojos;

import java.util.ArrayList;
import java.util.List;

public class POJOre {

    private String name;
    private String color;
    private int rarity;
    private boolean hasGravel;
    private boolean hasNether;
    private boolean hasEnd;
    private String ingot;
    private int ingotMeta;

    private List<String> oreDictNames = new ArrayList<>();

    public POJOre() {
    }

    public String getName() {
        return name;
    }

    public POJOre setName(String name) {
        this.name = name;
        return this;
    }

    public String getColor() {
        return color;
    }

    public POJOre setColor(String color) {
        this.color = color;
        return this;
    }

    public int getRarity() {
        return rarity;
    }

    public POJOre setRarity(int rarity) {
        this.rarity = rarity;
        return this;
    }

    public boolean isHasGravel() {
        return hasGravel;
    }

    public POJOre setHasGravel(boolean hasGravel) {
        this.hasGravel = hasGravel;
        return this;
    }

    public boolean isHasNether() {
        return hasNether;
    }

    public POJOre setHasNether(boolean hasNether) {
        this.hasNether = hasNether;
        return this;
    }

    public boolean isHasEnd() {
        return hasEnd;
    }

    public POJOre setHasEnd(boolean hasEnd) {
        this.hasEnd = hasEnd;
        return this;
    }

    public String getIngot() {
        return ingot;
    }

    public POJOre setIngot(String ingot) {
        this.ingot = ingot;
        return this;
    }

    public int getIngotMeta() {
        return ingotMeta;
    }

    public POJOre setIngotMeta(int ingotMeta) {
        this.ingotMeta = ingotMeta;
        return this;
    }

    public List<String> getOreDictNames() {
        return oreDictNames;
    }

    public POJOre setOreDictNames(List<String> oreDictNames) {
        this.oreDictNames = oreDictNames;
        return this;
    }
}
