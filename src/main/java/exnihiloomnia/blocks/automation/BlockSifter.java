package exnihiloomnia.blocks.automation;

import exnihiloomnia.blocks.automation.tile.TileSifter;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockSifter extends Block implements ITileEntityProvider {
    public BlockSifter() {
        super(Material.ANVIL);
        setUnlocalizedName("sifter");
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileSifter();
    }
}
