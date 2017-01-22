package exnihiloomnia.blocks.leaves;

import exnihiloomnia.ENOConfig;
import exnihiloomnia.blocks.ENOBlocks;
import exnihiloomnia.items.ENOItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BlockInfestedLeaves extends BlockLeaves implements ITileEntityProvider {

    private int[] surroundings;

    public BlockInfestedLeaves() {
        super();
        this.setCreativeTab(ENOItems.ENO_TAB);
        this.isBlockContainer = true;
        this.setDefaultState(this.blockState.getBaseState().withProperty(DECAYABLE, true).withProperty(CHECK_DECAY, true));
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        TileEntityInfestedLeaves leaves = (TileEntityInfestedLeaves) worldIn.getTileEntity(pos);

        if (leaves != null) {
            if (tag != null)
                leaves.state = Block.getBlockFromName(tag.getString("block")).getStateFromMeta(tag.getInteger("meta"));

            leaves.permanent = true;
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, DECAYABLE, CHECK_DECAY) {};
    }

    @Override
    public int getMetaFromState(IBlockState state) {
    	return 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
    	return this.getDefaultState();
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
        //no saplings for you
    }

    @Override
    public void beginLeavesDecay(IBlockState state, World world, BlockPos pos) {
        TileEntityInfestedLeaves te = (TileEntityInfestedLeaves) world.getTileEntity(pos);

        if (te != null) {
            te.dying = true;
        }
    }

    //do the same thing as leaves, but store stuff in the tile entity so it doesn't get reset every time
    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isRemote) {
            TileEntityInfestedLeaves te = (TileEntityInfestedLeaves) worldIn.getTileEntity(pos);

            if (te != null && te.dying && !te.permanent) {
                int posX = pos.getX();
                int posY = pos.getY();
                int posZ = pos.getZ();

                if (this.surroundings == null)
                    this.surroundings = new int[32768];

                if (worldIn.isAreaLoaded(new BlockPos(posX - 5, posY - 5, posZ - 5), new BlockPos(posX + 5, posY + 5, posZ + 5))) {
                    BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

                    for (int x = -4; x <= 4; ++x) {

                        for (int y = -4; y <= 4; ++y) {

                            for (int z = -4; z <= 4; ++z) {
                                IBlockState iblockstate = worldIn.getBlockState(mutableBlockPos.setPos(posX + x, posY + y, posZ + z));
                                Block block = iblockstate.getBlock();

                                if (!block.canSustainLeaves(iblockstate, worldIn, mutableBlockPos.setPos(posX + x, posY + y, posZ + z))) {

                                    if (block.isLeaves(iblockstate, worldIn, mutableBlockPos.setPos(posX + x, posY + y, posZ + z)))
                                        this.surroundings[(x + 16) * 1024 + (y + 16) * 32 + z + 16] = -2;
                                    else
                                        this.surroundings[(x + 16) * 1024 + (y + 16) * 32 + z + 16] = -1;
                                }
                                else
                                    this.surroundings[(x + 16) * 1024 + (y + 16) * 32 + z + 16] = 0;
                            }
                        }
                    }

                    for (int w = 1; w <= 4; ++w) {
                        for (int x = -4; x <= 4; ++x) {
                            for (int y = -4; y <= 4; ++y) {
                                for (int z = -4; z <= 4; ++z) {

                                    if (this.surroundings[(x + 16) * 1024 + (y + 16) * 32 + z + 16] == w - 1) {

                                        if (this.surroundings[(x + 16 - 1) * 1024 + (y + 16) * 32 + z + 16] == -2)
                                            this.surroundings[(x + 16 - 1) * 1024 + (y + 16) * 32 + z + 16] = w;

                                        if (this.surroundings[(x + 16 + 1) * 1024 + (y + 16) * 32 + z + 16] == -2)
                                            this.surroundings[(x + 16 + 1) * 1024 + (y + 16) * 32 + z + 16] = w;

                                        if (this.surroundings[(x + 16) * 1024 + (y + 16 - 1) * 32 + z + 16] == -2)
                                            this.surroundings[(x + 16) * 1024 + (y + 16 - 1) * 32 + z + 16] = w;

                                        if (this.surroundings[(x + 16) * 1024 + (y + 16 + 1) * 32 + z + 16] == -2)
                                            this.surroundings[(x + 16) * 1024 + (y + 16 + 1) * 32 + z + 16] = w;

                                        if (this.surroundings[(x + 16) * 1024 + (y + 16) * 32 + (z + 16 - 1)] == -2)
                                            this.surroundings[(x + 16) * 1024 + (y + 16) * 32 + (z + 16 - 1)] = w;

                                        if (this.surroundings[(x + 16) * 1024 + (y + 16) * 32 + z + 16 + 1] == -2)
                                            this.surroundings[(x + 16) * 1024 + (y + 16) * 32 + z + 16 + 1] = w;
                                    }
                                }
                            }
                        }
                    }
                }

                int magicNumber = this.surroundings[16912];

                if (magicNumber >= 0)
                    te.dying = false;
                else {

                    worldIn.setBlockToAir(pos);

                    if (worldIn.rand.nextFloat() < te.getProgress() * ENOConfig.string_chance / 10.0d)
                        Block.spawnAsEntity(worldIn, pos, new ItemStack(Items.STRING));
                }
            }
        }
    }

    @Override
    public BlockPlanks.EnumType getWoodType(int meta) {
        return BlockPlanks.EnumType.byMetadata((meta & 3) % 4);
    }

    @Override
    public List<ItemStack> onSheared(ItemStack item, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune) {
        TileEntityInfestedLeaves leaves = (TileEntityInfestedLeaves) world.getTileEntity(pos);

        if (leaves != null) {
            NBTTagCompound tag = new NBTTagCompound();

            if (leaves.state == null) {
                tag.setString("block", "");
            } else {
                tag.setString("block", Block.REGISTRY.getNameForObject(leaves.state.getBlock()).toString());
            }

            tag.setInteger("meta", leaves.state.getBlock().getMetaFromState(leaves.state));

            ItemStack drop = new ItemStack(ENOBlocks.INFESTED_LEAVES);
            drop.setTagCompound(tag);

            return Collections.singletonList(drop);
        }

        return null;
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        if (!world.isRemote) {
            TileEntityInfestedLeaves leaves = (TileEntityInfestedLeaves) world.getTileEntity(pos);

            if (leaves != null) {
                if ((float)(world.rand.nextInt(100)) / 100f < leaves.getProgress() * ENOConfig.string_chance / 4.0d)
                    Block.spawnAsEntity(world, pos, new ItemStack(Items.STRING));

                if ((float)(world.rand.nextInt(100)) / 100f < leaves.getProgress() * ENOConfig.string_chance)
                    Block.spawnAsEntity(world, pos, new ItemStack(Items.STRING));

                if (world.rand.nextFloat() < ENOConfig.silkworm_chance)
                    Block.spawnAsEntity(world, pos, new ItemStack(Items.STRING));
            }
        }

        world.removeTileEntity(pos);
        return world.setBlockState(pos, net.minecraft.init.Blocks.AIR.getDefaultState(), world.isRemote ? 11 : 3);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        worldIn.removeTileEntity(pos);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, ItemStack stack) {
        return this.getDefaultState().withProperty(DECAYABLE, false).withProperty(CHECK_DECAY, false);
    }

    @Override
    public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param) {
        super.eventReceived(state, worldIn, pos, id, param);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity != null && tileentity.receiveClientEvent(id, param);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return !(!Minecraft.getMinecraft().gameSettings.fancyGraphics && blockAccess.getBlockState(pos.offset(side)).getBlock() == this);
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return ENOConfig.pretty_leaves ? EnumBlockRenderType.INVISIBLE : EnumBlockRenderType.MODEL;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return Minecraft.getMinecraft().gameSettings.fancyGraphics ? BlockRenderLayer.CUTOUT_MIPPED : BlockRenderLayer.SOLID;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityInfestedLeaves();
    }
}

