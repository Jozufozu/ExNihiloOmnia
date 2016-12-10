package exnihiloomnia.registries.ore;

import exnihiloomnia.ENO;
import exnihiloomnia.util.enums.EnumOreBlockType;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemBlockRemap extends Item {

    private EnumOreBlockType type;

    public ItemBlockRemap(EnumOreBlockType type) {
        this.type = type;

        GameRegistry.register(this, new ResourceLocation(ENO.MODID, "remap_" + type.getName()));
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        Ore target = OreRegistry.getOre(stack.getMetadata());

        stack.deserializeNBT(new ItemStack(target.getBlock(), stack.stackSize, this.type.ordinal()).serializeNBT());
    }
}
