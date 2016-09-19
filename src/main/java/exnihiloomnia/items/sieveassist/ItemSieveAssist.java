package exnihiloomnia.items.sieveassist;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemSieveAssist extends Item implements ISieveFaster {

    private ToolMaterial material;
    private int siftTime;

    public ItemSieveAssist(ToolMaterial mat) {
        material = mat;
        setCreativeTab(CreativeTabs.TOOLS);
        setMaxStackSize(1);
    }

    public void setSiftTime(int siftTime) {
        this.siftTime = siftTime;
    }
    public int getSiftTime() {
        return this.siftTime;
    }
    public void addSiftTime(int time) {
        this.siftTime += time;
    }

    public float getSpeedModifier() {
        return findSpeedModifier(material);
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
