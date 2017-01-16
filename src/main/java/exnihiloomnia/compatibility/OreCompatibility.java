package exnihiloomnia.compatibility;

import exnihiloomnia.compatibility.actuallyadditions.AACompatibility;
import exnihiloomnia.compatibility.enderio.EnderIOCompatibility;
import exnihiloomnia.registries.ore.Ore;
import exnihiloomnia.registries.ore.OreRegistry;
import exnihiloomnia.util.enums.EnumOreBlockType;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public class OreCompatibility {

    public static void initialize() {
        boolean mekanism = Loader.isModLoaded("Mekanism") && ENOCompatibility.mekanism_crusher;
        boolean aa = Loader.isModLoaded("actuallyadditions") && ENOCompatibility.aa_crusher;
        boolean enderIO = Loader.isModLoaded("EnderIO") && ENOCompatibility.sag_mill;

        if (mekanism || aa || enderIO) {
            for (Ore ore : OreRegistry.registry.values()) {

                Block oreBlock = OreRegistry.getBlockFromOre(ore);

                if (oreBlock != null) {
                    for (EnumOreBlockType type : EnumOreBlockType.values()) {
                        Item crushed = type.getSmashing();

                        if (crushed != null) {
                            ItemStack input = new ItemStack(oreBlock, 1, type.ordinal());

                            ItemStack output = new ItemStack(crushed, 1, ore.getMetadata());
                            ItemStack output2 = new ItemStack(crushed, 1, ore.getMetadata());

                            if (mekanism)
                                addMekanismCompatibility(input, output);
                            if (aa)
                                AACompatibility.addCompatibility(input, output, output2);
                            if (enderIO)
                                EnderIOCompatibility.addCompatibility(input, output, output2);
                        }
                    }
                }
            }
        }
    }

    public static void addMekanismCompatibility(ItemStack input, ItemStack output) {
        output.stackSize = 6;

        NBTTagCompound msg = new NBTTagCompound();
        msg.setTag("input", input.writeToNBT(new NBTTagCompound()));
        msg.setTag("output", output.writeToNBT(new NBTTagCompound()));

        FMLInterModComms.sendMessage("Mekanism", "CrusherRecipe", msg);
    }
}
