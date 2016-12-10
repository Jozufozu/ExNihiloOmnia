package exnihiloomnia.registries.ore;

import exnihiloomnia.ENO;
import exnihiloomnia.items.ENOItems;
import exnihiloomnia.util.Color;
import exnihiloomnia.util.enums.EnumOreBlockType;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class Ore {

    private static int META;

    private final int meta;
    private final String name;
    private final int color;
    private final int rarity;
    private final boolean hasGravel;
    private final boolean hasNether;
    private final boolean hasEnd;

    private Item ingot = null;

    public Item getIngot() {
        return ingot;
    }

    public Ore setIngot(Item ingot) {
        if (this.ingot != null) this.ingot = ingot;

        return this;
    }

    public Ore(String name, Color color, int rarity, boolean hasGravel, boolean hasNether, boolean hasEnd) {
        this.name = name;
        this.color = color.toInt();
        this.rarity = rarity;
        this.hasGravel = hasGravel;
        this.hasNether = hasNether;
        this.hasEnd = hasEnd;

        this.meta = META++;
    }

    public ResourceLocation getOreName(EnumOreBlockType type) {
        return new ResourceLocation(ENO.MODID + ":ore_" + type.getName() + "_" + this.getName());
    }

    public boolean hasType(EnumOreBlockType type) {
        return  (this.hasGravel && type == EnumOreBlockType.GRAVEL) ||
                (this.hasNether && type == EnumOreBlockType.GRAVEL_NETHER) ||
                (this.hasEnd && type == EnumOreBlockType.GRAVEL_ENDER) ||
                type == EnumOreBlockType.SAND ||
                type == EnumOreBlockType.DUST;
    }

    public Block getBlock() {
        return OreRegistry.blocks.get(name);
    }

    public void addCrafting() {
        Block oreBlock = getBlock();

        for (EnumOreBlockType type : EnumOreBlockType.values())
            if (hasType(type))
                GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(oreBlock, 1, type.ordinal()),
                    "xx",
                    "xx",
                    'x', new ItemStack(type.getCrafting(), 1, this.meta)));
    }

    public void addSmelting() {
        Block oreBlock = getBlock();

        ItemStack output = ingot == null ? new ItemStack(ENOItems.INGOT_ORE, 1, this.meta) : new ItemStack(ingot);

        for (EnumOreBlockType type : EnumOreBlockType.values())
            if (hasType(type)) GameRegistry.addSmelting(new ItemStack(oreBlock, 1, type.ordinal()), output, 0);
    }

    public int getMetadata() {
        return meta;
    }

    public String getName() {
        return name;
    }

    public int getColor() {
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
}
