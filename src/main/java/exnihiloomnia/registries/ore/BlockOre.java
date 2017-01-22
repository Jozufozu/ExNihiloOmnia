package exnihiloomnia.registries.ore;

import exnihiloomnia.ENO;
import exnihiloomnia.items.ENOItems;
import exnihiloomnia.items.itemblocks.ItemBlockOre;
import exnihiloomnia.util.enums.EnumOreBlockType;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings("deprecation")
public class BlockOre extends BlockFalling {

    public static final PropertyEnum TYPE = PropertyEnum.create("type", EnumOreBlockType.class);

    private final Ore ore;

    public BlockOre(Ore ore) {
        this.ore = ore;

        this.setHardness(0.6F);
        this.setUnlocalizedName(ore.getName());
        this.setCreativeTab(ENOItems.ORE_TAB);
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, EnumOreBlockType.GRAVEL));

        GameRegistry.register(this, new ResourceLocation(ENO.MODID, ore.getName()));
        GameRegistry.register(new ItemBlockOre(this), new ResourceLocation(ENO.MODID, "ore_" + ore.getName()));
    }

    @Override
    public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity) {
        EnumOreBlockType type = state.<EnumOreBlockType>getValue(TYPE);

        switch (type) {
            case DUST:
                return SoundType.SNOW;
            case SAND:
                return SoundType.SAND;
            default:
                return SoundType.GROUND;
        }
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        for (EnumOreBlockType type : EnumOreBlockType.values())
            if (this.ore.hasType(type)) list.add(new ItemStack(this, 1, type.ordinal()));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, TYPE) {};
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(TYPE, EnumOreBlockType.fromMetadata(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((Enum)state.getValue(TYPE)).ordinal();
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean isVisuallyOpaque() {
        return false;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }
}
