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
import mcjty.theoneprobe.api.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInterModComms;

import javax.annotation.Nullable;

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
                    if (blockState.getBlock() instanceof BlockSieve && world.getTileEntity(data.getPos()) instanceof TileEntitySieve)
                        provideSieveData((TileEntitySieve) world.getTileEntity(data.getPos()), info);

                    if (blockState.getBlock() instanceof BlockCrucible && world.getTileEntity(data.getPos()) instanceof TileEntityCrucible)
                        provideCrucibleData((TileEntityCrucible) world.getTileEntity(data.getPos()), info);

                    if (blockState.getBlock() instanceof BlockBarrel && world.getTileEntity(data.getPos()) instanceof TileEntityBarrel)
                        provideBarrelData((TileEntityBarrel) world.getTileEntity(data.getPos()), mode, info, player, world, blockState, data);

                    if (blockState.getBlock() instanceof BlockInfestedLeaves && world.getTileEntity(data.getPos()) instanceof TileEntityInfestedLeaves)
                        provideLeavesData((TileEntityInfestedLeaves) world.getTileEntity(data.getPos()), info);
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
            });
            return null;
        }
    }

    public static String format(float input) {
        return String.format("%.0f", input);
    }
}
