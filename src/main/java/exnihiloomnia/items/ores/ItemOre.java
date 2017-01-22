package exnihiloomnia.items.ores;

import exnihiloomnia.items.ENOItems;
import exnihiloomnia.registries.ore.Ore;
import exnihiloomnia.registries.ore.OreRegistry;
import exnihiloomnia.util.enums.EnumOreItemType;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemOre extends Item {
    public final EnumOreItemType type;

    public ItemOre(EnumOreItemType type) {
        this.type = type;

        this.setUnlocalizedName("ore_" + type.getName());
        this.setHasSubtypes(true);
        this.setCreativeTab(ENOItems.ORE_TAB);
    }

    public EnumOreItemType getType() {
    	return type;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        Ore ore = OreRegistry.getOre(stack.getMetadata());

        String suff  = "";

        if (ore != null)
            suff = "." + ore.getName();

        return super.getUnlocalizedName() + suff;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String display = super.getItemStackDisplayName(stack);

        if (display.equals(getUnlocalizedName(stack) + ".name")) {
            Ore ore = OreRegistry.getOre(stack.getMetadata());
            if (ore != null)
                display = I18n.format("item.ore_template." + type.getName() + ".name", ore.getOreDictName(""));
        }

        return display;
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        for (Ore ore : OreRegistry.registry.values()) {
            if (    (ore.hasGravel() && getType() == EnumOreItemType.BROKEN)
                    || (ore.hasEnd() && getType() == EnumOreItemType.BROKEN_ENDER)
                    || (ore.hasNether() && getType() == EnumOreItemType.BROKEN_NETHER)
                    || (ore.getIngot() == null && getType() == EnumOreItemType.INGOT)
                    || getType() == EnumOreItemType.CRUSHED
                    || getType() == EnumOreItemType.POWDERED)

                subItems.add(new ItemStack(itemIn, 1, ore.getMetadata()));
        }
    }
}
