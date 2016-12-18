package exnihiloomnia.registries.ore;

import exnihiloomnia.ENO;
import exnihiloomnia.util.enums.EnumOreBlockType;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemRemap extends ItemBlock {

    private final EnumOreBlockType type;

    public ItemRemap(BlockRemap block) {
        super(block);

        this.type = block.type;
        setUnlocalizedName("remap_" + type.getName());
        GameRegistry.register(this, new ResourceLocation(ENO.MODID, "ore_" + type.getName()));
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        Ore target = OreRegistry.getOre(stack.getMetadata());

        if (target != null) {
            stack.deserializeNBT(new ItemStack(target.getBlock(), stack.stackSize, this.type.ordinal()).serializeNBT());
        }
    }
}
