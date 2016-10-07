package exnihiloomnia.blocks.sieves;

import java.util.List;

import exnihiloomnia.util.enums.EnumWood;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSieveWood extends BlockSieve {
	public static final PropertyEnum WOOD = PropertyEnum.create("wood", EnumWood.class);
	
	public BlockSieveWood(Material material) {
		super(material);

		this.setSoundType(SoundType.WOOD);
		this.setDefaultState(this.blockState.getBaseState().withProperty(WOOD, EnumWood.OAK));
	}

	@Override
	public int damageDropped(IBlockState state) {
        return ((EnumWood) state.getValue(WOOD)).getMetadata();
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
		for (EnumWood wood : EnumWood.values()) {
			list.add(new ItemStack(itemIn, 1, wood.getMetadata()));
		}
    }
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(WOOD, EnumWood.fromMetadata(meta));
    }
	
	@Override
	public int getMetaFromState(IBlockState state) {
        return ((EnumWood) state.getValue(WOOD)).getMetadata();
    }

	@Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, WOOD);
    }
}
