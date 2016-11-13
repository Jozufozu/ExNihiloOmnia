package exnihiloomnia.items.itemblocks;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemInfestedLeaves extends ItemBlock {

    public ItemInfestedLeaves(Block block) {
        super(block);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();

        String extra = "";

        if (tag != null && tag.hasKey("block") && tag.hasKey("meta")) {
            ItemStack itemStack = new ItemStack(Item.getItemFromBlock(Block.getBlockFromName(tag.getString("block"))), 1, tag.getInteger("meta"));
            extra = " (" + itemStack.getItem().getItemStackDisplayName(itemStack) + ")";
        }

        return super.getItemStackDisplayName(stack) + extra;
    }
}
