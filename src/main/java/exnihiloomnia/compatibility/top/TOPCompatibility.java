package exnihiloomnia.compatibility.top;

import com.google.common.base.Function;
import exnihiloomnia.ENO;
import exnihiloomnia.ENOConfig;
import exnihiloomnia.blocks.barrels.BlockBarrel;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import exnihiloomnia.blocks.crucibles.BlockCrucible;
import exnihiloomnia.blocks.crucibles.tileentity.TileEntityCrucible;
import exnihiloomnia.blocks.leaves.BlockInfestedLeaves;
import exnihiloomnia.blocks.leaves.TileEntityInfestedLeaves;
import exnihiloomnia.blocks.sieves.BlockSieve;
import exnihiloomnia.blocks.sieves.tileentity.TileEntitySieve;
import exnihiloomnia.compatibility.forestry.Surroundings;
import exnihiloomnia.compatibility.forestry.beelure.BlockBeeTrapTreated;
import exnihiloomnia.compatibility.forestry.beelure.TileEntityBeeTrap;
import forestry.core.genetics.alleles.EnumAllele;
import mcjty.theoneprobe.api.*;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInterModComms;

import javax.annotation.Nullable;
import java.util.Map;

public class TOPCompatibility {
    private static boolean registered;

    public static void register() {
        if (registered)
            return;
        registered = true;
        FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", "exnihiloomnia.compatibility.top.TOPCompatibility$GetTheOneProbe");
    }

    public static class GetTheOneProbe implements Function<ITheOneProbe, Void> {
        public static ITheOneProbe probe;

        @Nullable
        @Override
        public Void apply(ITheOneProbe theOneProbe) {
            probe = theOneProbe;
            probe.registerProvider(new IProbeInfoProvider() {
                @Override
                public String getID() {
                    return ENO.MODID;
                }

                @Override
                public void addProbeInfo(ProbeMode mode, IProbeInfo info, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
                    TileEntity tileEntity = world.getTileEntity(data.getPos());
                    Block block = blockState.getBlock();

                    if (block instanceof BlockSieve && tileEntity instanceof TileEntitySieve)
                        provideSieveData((TileEntitySieve) tileEntity, info);

                    if (block instanceof BlockCrucible && tileEntity instanceof TileEntityCrucible)
                        provideCrucibleData((TileEntityCrucible) tileEntity, info);

                    if (block instanceof BlockBarrel && tileEntity instanceof TileEntityBarrel)
                        provideBarrelData((TileEntityBarrel) tileEntity, mode, info, player, world, blockState, data);

                    if (block instanceof BlockInfestedLeaves && tileEntity instanceof TileEntityInfestedLeaves)
                        provideLeavesData((TileEntityInfestedLeaves) tileEntity, info);

                    if (block instanceof BlockBeeTrapTreated && tileEntity instanceof TileEntityBeeTrap)
                        provideBeeLureData((TileEntityBeeTrap) tileEntity, mode, info);
                }

                private void provideSieveData(TileEntitySieve sieve, IProbeInfo info) {
                    if (!sieve.hasMesh() && !ENOConfig.classic_sieve) {
                        info.text(TextFormatting.RED.toString() + TextFormatting.BOLD.toString() + "No Mesh");
                    }
                    else {
                        if (!ENOConfig.classic_sieve)
                            info.text(sieve.getMesh().getDisplayName() + " " + (sieve.getMesh().getMaxDamage() - sieve.getMesh().getItemDamage() + 1) + "/" + (sieve.getMesh().getMaxDamage() + 1));

                        if (!sieve.canWork()) {
                            info.text(TextFormatting.RED + "Empty");
                        }
                        else {
                            info.text("Sifting: " + sieve.getContents().getDisplayName() + ": " + format(sieve.getProgress() * 100) + "%");
                            info.progress((int)(sieve.getProgress() * 100), 100);
                        }
                    }
                }

                private void provideCrucibleData(TileEntityCrucible crucible, IProbeInfo info) {
                    if (crucible.getLastItemAdded() != null) {
                        info.text("Solid: " + crucible.getLastItemAdded().getDisplayName());
                        info.progress(crucible.getSolidContent() / 200, 1000);
                    }
                    else
                        info.text(TextFormatting.RED + "No Material");

                    if (crucible.getMeltingSpeed() != 0)
                        info.text("Speed: " + Math.round(crucible.getTrueSpeed()) + "mB/s");
                    else
                        info.text(TextFormatting.RED + "No Heat Source");
                }

                private void provideBarrelData(TileEntityBarrel barrel, ProbeMode mode, IProbeInfo info, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
                    barrel.getState().provideInformation(barrel, mode, info, player, world, blockState, data);

                    if (mode == ProbeMode.DEBUG)
                        info.text("State: " + barrel.getState().getUniqueIdentifier());
                }

                private void provideLeavesData(TileEntityInfestedLeaves infestedLeaves, IProbeInfo info) {
                    if (infestedLeaves.getProgress() == 1.0f)
                        info.text("Infested");
                    else {
                        info.text("Infesting");
                        info.progress((int) (infestedLeaves.getProgress() * 100), 100, info.defaultProgressStyle().alternateFilledColor(0xffffffff));
                    }
                }

                private void provideBeeLureData(TileEntityBeeTrap beeTrap, ProbeMode mode, IProbeInfo info) {
                    Surroundings blocks = beeTrap.getBlocks();

                    info.progress(TileEntityBeeTrap.TIMER_MAX - beeTrap.getTimer(), TileEntityBeeTrap.TIMER_MAX, info.defaultProgressStyle().showText(false).filledColor(0xfff0f28e).alternateFilledColor(0xfff0f28e));

                    if (mode == ProbeMode.DEBUG) {
                        info.text("Flowers:");
                        for (Map.Entry<EnumAllele.Flowers, Integer> flowers : blocks.flowers.entrySet()) {
                            info.text(flowers.getKey().name() + ": " + flowers.getValue());
                        }
                        info.text("Surroundings:");
                        IProbeInfo h = info.horizontal();
                        for (Map.Entry<IBlockState, Integer> entry : blocks.blocks.entrySet()) {
                            IBlockState state = entry.getKey();
                            int count = entry.getValue();

                            Block block = state.getBlock();
                            int meta = block.getMetaFromState(state);

                            if (block != Blocks.AIR)
                                h.item(new ItemStack(block, count, meta));
                        }
                    }
                }
            });
            return null;
        }
    }

    public static String format(float input) {
        return String.format("%.0f", input);
    }
}
