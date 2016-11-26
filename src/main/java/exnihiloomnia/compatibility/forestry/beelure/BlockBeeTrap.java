package exnihiloomnia.compatibility.forestry.beelure;

import exnihiloomnia.items.ENOItems;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockBeeTrap extends Block {

    public BlockBeeTrap() {
        super(Material.GROUND);
        setSoundType(SoundType.GROUND);

        setHardness(0.8f);
        setCreativeTab(ENOItems.ENO_TAB);

        setUnlocalizedName("bee_trap");
        setRegistryName("bee_trap");
    }
}
