package exnihiloomnia.items.sieveassist;

import exnihiloomnia.ENOConfig;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemSifter extends Item implements ISieveFaster {

    private ToolMaterial material;

    public ItemSifter(ToolMaterial mat) {
        material = mat;
        setCreativeTab(CreativeTabs.TOOLS);
        setMaxStackSize(1);
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return super.hasEffect(stack);
    }

    @Override
    public int getItemEnchantability() {
        return material.getEnchantability();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        tooltip.add(Float.toString(((ISieveFaster) stack.getItem()).getSpeedModifier()) + I18n.format("exnihiloomnia.sifter.speedModifier") );
    }

    public void setSiftTime(ItemStack stack, int siftTime) {
        stack.getTagCompound().setInteger("siftTime", siftTime);
    }

    public int getSiftTime(ItemStack stack) {
        try {
            return stack.getTagCompound().getInteger("siftTime");
        }
        catch (NullPointerException e) {
            stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setInteger("siftTime", 0);
            return 0;
        }
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
        switch (material) {
            case WOOD:
                return 1f;
            case STONE:
                return 1.1f;
            case IRON:
                return 1.3f;
            case DIAMOND:
                return 1.6f;
            case GOLD:
                return 2;
            default:
                return 1;
        }
    }

    //Why did I do this? What's wrong with me
    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (ENOConfig.annoying_sifter) {
            if (isSelected && !entityIn.isSneaking()) {
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
}
