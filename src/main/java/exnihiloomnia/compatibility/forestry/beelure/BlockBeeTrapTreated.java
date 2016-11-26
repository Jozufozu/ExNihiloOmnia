package exnihiloomnia.compatibility.forestry.beelure;

import exnihiloomnia.items.ENOItems;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockBeeTrapTreated extends Block implements ITileEntityProvider {

    public BlockBeeTrapTreated() {
        super(Material.GROUND);

        setUnlocalizedName("bee_trap_treated");
        setRegistryName("bee_trap_treated");

        setHardness(1.0f);
        setSoundType(SoundType.GROUND);
        setCreativeTab(ENOItems.ENO_TAB);

        GameRegistry.registerTileEntity(TileEntityBeeTrap.class, this.getUnlocalizedName());
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityBeeTrap();
    }
}
