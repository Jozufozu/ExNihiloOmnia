package exnihiloomnia.items.sieveassist;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.List;

public class ItemSieveAssist extends Item implements ISieveFaster {

    private ToolMaterial material;

    public ItemSieveAssist(ToolMaterial mat) {
        material = mat;
        setCreativeTab(CreativeTabs.TOOLS);
        setMaxStackSize(1);
    }

    public void setSiftTime(ItemStack stack, int siftTime) {
        stack.getTagCompound().setInteger("siftTime", siftTime);
    }

    public int getSiftTime(ItemStack stack) {
        return stack.getTagCompound().getInteger("siftTime");
    }

    //convenience
    public void addSiftTime(ItemStack stack, int time) {
        setSiftTime(stack, getSiftTime(stack) + time);
    }

    public float getSpeedModifier() {
        return findSpeedModifier(material);
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        ItemStack itemStack = new ItemStack(itemIn);
        itemStack.setTagCompound(new NBTTagCompound());
        itemStack.getTagCompound().setInteger("siftTime", 0);
        subItems.add(itemStack);
    }

    public static float findSpeedModifier(ToolMaterial material) {
        if (material == ToolMaterial.WOOD)
            return .5f;
        else if (material == ToolMaterial.STONE)
            return 1;
        else if (material == ToolMaterial.IRON)
            return 1.3f;
        else if (material == ToolMaterial.DIAMOND)
            return 1.6f;
        else if (material == ToolMaterial.GOLD)
            return 2;
        else
            return 1;
    }
}
