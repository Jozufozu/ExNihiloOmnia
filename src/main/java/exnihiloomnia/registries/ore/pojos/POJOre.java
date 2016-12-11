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

    private List<String> oreDictNames = new ArrayList<String>();

    public POJOre() {
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRarity() {
        return rarity;
    }

    public void setRarity(int rarity) {
        this.rarity = rarity;
    }

    public boolean isHasGravel() {
        return hasGravel;
    }

    public void setHasGravel(boolean hasGravel) {
        this.hasGravel = hasGravel;
    }

    public boolean isHasNether() {
        return hasNether;
    }

    public void setHasNether(boolean hasNether) {
        this.hasNether = hasNether;
    }

    public boolean isHasEnd() {
        return hasEnd;
    }

    public void setHasEnd(boolean hasEnd) {
        this.hasEnd = hasEnd;
    }

    public List<String> getOreDictNames() {
        return oreDictNames;
    }

    public void setOreDictNames(List<String> oreDictNames) {
        this.oreDictNames = oreDictNames;
    }

    public String getIngot() {
        return ingot;
    }

    public void setIngot(String ingot) {
        this.ingot = ingot;
    }
}
