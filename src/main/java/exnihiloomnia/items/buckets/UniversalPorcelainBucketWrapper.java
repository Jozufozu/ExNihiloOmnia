package exnihiloomnia.items.buckets;

import exnihiloomnia.ENOConfig;
import exnihiloomnia.fluids.ENOFluids;
import exnihiloomnia.items.ENOItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;

import javax.annotation.Nullable;

public class UniversalPorcelainBucketWrapper extends FluidBucketWrapper {

    public UniversalPorcelainBucketWrapper(ItemStack container) {
        super(container);
    }

    @Override
    protected void setFluid(Fluid fluid) {
        if (fluid == null)
            container.deserializeNBT(new ItemStack(ENOItems.BUCKET_PORCELAIN_EMPTY).serializeNBT());

        else if (fluid == FluidRegistry.WATER)
            container.deserializeNBT(new ItemStack(ENOItems.BUCKET_PORCELAIN_WATER).serializeNBT());

        else if (fluid == FluidRegistry.LAVA)
            container.deserializeNBT(new ItemStack(ENOItems.BUCKET_PORCELAIN_LAVA).serializeNBT());

        else if (fluid == ENOFluids.WITCHWATER)
            container.deserializeNBT(new ItemStack(ENOItems.BUCKET_PORCELAIN_WITCHWATER).serializeNBT());

        else if (fluid.getName().equals("milk"))
            container.deserializeNBT(new ItemStack(ENOItems.BUCKET_PORCELAIN_MILK).serializeNBT());

        else if (FluidRegistry.getBucketFluids().contains(fluid) && ENOConfig.universal_bucket) {
            ItemStack filledBucket = UniversalPorcelainBucket.getFilledBucket(ENOItems.BUCKET_PORCELAIN, fluid);
            container.deserializeNBT(filledBucket.serializeNBT());
        }
    }

    @Nullable
    @Override
    public FluidStack getFluid() {
        Item item = container.getItem();

        if (item == ENOItems.BUCKET_PORCELAIN_WATER)
            return new FluidStack(FluidRegistry.WATER, Fluid.BUCKET_VOLUME);

        else if (item == ENOItems.BUCKET_PORCELAIN_LAVA)
            return new FluidStack(FluidRegistry.LAVA, Fluid.BUCKET_VOLUME);

        else if (item == ENOItems.BUCKET_PORCELAIN_WITCHWATER)
            return new FluidStack(ENOFluids.WITCHWATER, Fluid.BUCKET_VOLUME);

        else if (item == ENOItems.BUCKET_PORCELAIN_MILK)
            return FluidRegistry.getFluidStack("milk", Fluid.BUCKET_VOLUME);

        else if (item == ENOItems.BUCKET_PORCELAIN && ENOConfig.universal_bucket)
            return ENOItems.BUCKET_PORCELAIN.getFluid(container);

        else
            return null;
    }
}
