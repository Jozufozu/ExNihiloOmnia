package exnihiloomnia.items;

import exnihiloomnia.fluids.ENOFluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;

import javax.annotation.Nullable;

public class PorcelainBucketWrapper extends FluidBucketWrapper {

    public PorcelainBucketWrapper(ItemStack container) {
        super(container);
    }


    public boolean canFillFluidType(FluidStack fluid) {
        return fluid.getFluid() == FluidRegistry.WATER || fluid.getFluid() == FluidRegistry.LAVA || fluid.getFluid().getName().equals("milk") || fluid.getFluid() == ENOFluids.WITCHWATER;
    }

    @Nullable
    public FluidStack getFluid() {

        Item bucket = container.getItem();
        if (bucket == ENOItems.BUCKET_PORCELAIN_LAVA)
            return new FluidStack(FluidRegistry.LAVA, Fluid.BUCKET_VOLUME);
        else if (bucket == ENOItems.BUCKET_PORCELAIN_WATER)
            return new FluidStack(FluidRegistry.WATER, Fluid.BUCKET_VOLUME);
        else if (bucket == ENOItems.BUCKET_PORCELAIN_WITCHWATER)
            return new FluidStack(ENOFluids.WITCHWATER, Fluid.BUCKET_VOLUME);
        else if (bucket == ENOItems.BUCKET_PORCELAIN_MILK)
            return new FluidStack(FluidRegistry.getFluid("milk"), Fluid.BUCKET_VOLUME);
        else
            return null;
    }

    protected void setFluid(Fluid fluid) {
        if (fluid == null)
            container.setItem(ENOItems.BUCKET_PORCELAIN_EMPTY);
        else if (fluid == FluidRegistry.WATER)
            container.setItem(ENOItems.BUCKET_PORCELAIN_WATER);
        else if (fluid == FluidRegistry.LAVA)
            container.setItem(ENOItems.BUCKET_PORCELAIN_LAVA);
        else if (fluid == ENOFluids.WITCHWATER)
            container.setItem(ENOItems.BUCKET_PORCELAIN_WITCHWATER);
        else if (fluid.getName().equals("milk"))
            container.setItem(ENOItems.BUCKET_PORCELAIN_MILK);
    }
}
