package exnihiloomnia.items.sieveassist;

import net.minecraft.item.ItemStack;

public interface ISieveFaster {
    float getSpeedModifier();

    int getSiftTime(ItemStack stack);
    void setSiftTime(ItemStack stack, int siftTime);
    void addSiftTime(ItemStack stack, int time);
}
