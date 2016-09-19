package exnihiloomnia.items.sieveassist;

import exnihiloomnia.ENOConfig;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;

public class ItemSifter extends Item implements ISieveFaster {

    private ToolMaterial material;

    public ItemSifter(ToolMaterial mat) {
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

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (isSelected && !entityIn.isSneaking() && ENOConfig.annoying_sifter) {
            if (worldIn.getTotalWorldTime() % 4 == 0) {

                float yaw = (float) Math.toRadians(entityIn.rotationYaw), pitch = (float) Math.toRadians(entityIn.rotationPitch);
                float strength = ENOConfig.sifter_strength * getSpeedModifier();

                double x = strength * MathHelper.sin(yaw) * MathHelper.cos(pitch),
                        y = strength * MathHelper.sin(pitch),
                        z = strength * MathHelper.cos(yaw) * MathHelper.cos(pitch);

                entityIn.addVelocity(x, y, -z);
            }
        }
    }
}
