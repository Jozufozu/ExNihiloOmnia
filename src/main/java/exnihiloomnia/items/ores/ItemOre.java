package exnihiloomnia.items.ores;

import exnihiloomnia.ENO;
import exnihiloomnia.items.ENOItems;
import exnihiloomnia.util.enums.EnumOre;
import exnihiloomnia.util.enums.EnumOreItemType;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemOre extends Item {
    public EnumOreItemType type;

    public ItemOre(EnumOreItemType type1) {
        type = type1;

        this.setHasSubtypes(true);
        this.setCreativeTab(ENOItems.ORE_TAB);
    }

    public EnumOreItemType getType() {
    	return type;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int i = stack.getMetadata();
        return super.getUnlocalizedName() + "." + EnumOre.fromMetadata(i).getName();
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        for (EnumOre ore : ENO.oreList) {
            if (!(this.getType().equals(EnumOreItemType.INGOT) && (ore.equals(EnumOre.GOLD) || ore.equals(EnumOre.IRON))))
                if (       (ore.hasGravel() && getType() == EnumOreItemType.BROKEN)
                        || (ore.hasEnd() && getType() == EnumOreItemType.BROKEN_ENDER)
                        || (ore.hasNether() && getType() == EnumOreItemType.BROKEN_NETHER)
                        || getType() == EnumOreItemType.CRUSHED
                        || getType() == EnumOreItemType.POWDERED
                        || getType() == EnumOreItemType.INGOT)
                    subItems.add(new ItemStack(itemIn, 1, ore.getMetadata()));
        }
    }
}
