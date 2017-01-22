package exnihiloomnia.registries.ore;

import exnihiloomnia.ENO;
import exnihiloomnia.util.enums.EnumOreBlockType;
import exnihiloomnia.util.enums.EnumOreLegacy;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Random;

@SuppressWarnings("deprecation")
public class BlockRemap extends Block {

    public static final PropertyEnum ORE = PropertyEnum.create("ore", EnumOreLegacy.class);

    public final EnumOreBlockType type;

    public BlockRemap(final EnumOreBlockType type) {
        super(Material.GROUND);
        this.setHardness(0.6f);
        this.type = type;
        setTickRandomly(true);

        setUnlocalizedName("remap_" + type.getName());
        GameRegistry.register(this, new ResourceLocation(ENO.MODID, "ore_" + type.getName()));
        new ItemRemap(this);

        setDefaultState(blockState.getBaseState().withProperty(ORE, EnumOreLegacy.IRON));

        ENO.proxy.setCustomStateMapper(this, ENO.MODID + ":remap_" + type.getName());
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        Ore target = OreRegistry.getOre(this.getMetaFromState(state));

        if (target != null)
            worldIn.setBlockState(pos, OreRegistry.blocks.get(target.getName()).getDefaultState().withProperty(BlockOre.TYPE, type));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, ORE) {};
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(ORE, EnumOreLegacy.values()[meta]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        EnumOreLegacy type = (EnumOreLegacy) state.getValue(ORE);
        return type.ordinal();
    }
}
