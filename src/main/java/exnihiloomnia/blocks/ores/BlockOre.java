package exnihiloomnia.blocks.ores;

import java.util.List;

import exnihiloomnia.ENO;
import exnihiloomnia.items.ENOItems;
import exnihiloomnia.util.enums.EnumOre;
import exnihiloomnia.util.enums.EnumOreBlockType;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockOre extends BlockFalling {
    public static final PropertyEnum ORE = PropertyEnum.create("ore", EnumOre.class);

    public EnumOreBlockType type;

    public BlockOre(EnumOreBlockType type1) {
        this.setHardness(0.6f);
        this.type = type1;
        
        if (type1 == EnumOreBlockType.DUST)
            this.setSoundType(SoundType.SNOW);
        else if (type1 == EnumOreBlockType.SAND)
            this.setSoundType(SoundType.SAND);
        else
            this.setSoundType(SoundType.GROUND);
        
        this.setCreativeTab(ENOItems.ORE_TAB);
        this.setDefaultState(this.blockState.getBaseState().withProperty(ORE, EnumOre.IRON));
    }

    public EnumOreBlockType getType() {
    	return this.type;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{ORE}) {};
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item item, CreativeTabs tabs, List<ItemStack> items) {
        for (EnumOre ore : ENO.oreList) {
            if ((ore.hasGravel() && getType() == EnumOreBlockType.GRAVEL)
                || (ore.hasEnd() && getType() == EnumOreBlockType.GRAVEL_ENDER)
                || (ore.hasNether() && getType() == EnumOreBlockType.GRAVEL_NETHER)
                || getType() == EnumOreBlockType.SAND
                || getType() == EnumOreBlockType.DUST)
                
            	items.add(new ItemStack(item, 1, ore.getMetadata()));
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(ORE, EnumOre.fromMetadata(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        EnumOre type = (EnumOre) state.getValue(ORE);
        return type.getMetadata();
    }

    @Override
    public int damageDropped(IBlockState state) {
        return ((EnumOre) state.getValue(ORE)).getMetadata();
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }
}
