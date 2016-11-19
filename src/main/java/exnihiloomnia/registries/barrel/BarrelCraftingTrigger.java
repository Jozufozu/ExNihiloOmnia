package exnihiloomnia.registries.barrel;

import exnihiloomnia.blocks.barrels.architecture.BarrelLogic;
import exnihiloomnia.blocks.barrels.states.BarrelStates;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import exnihiloomnia.registries.barrel.pojos.BarrelCraftingRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import javax.annotation.Nullable;

public class BarrelCraftingTrigger extends BarrelLogic {

    public final Fluid fluid;
    public final ItemStack input;
    public final ItemStack output;

    public BarrelCraftingTrigger(Fluid fluid, ItemStack input, ItemStack output) {
        this.fluid = fluid;
        this.input = input;
        this.output = output;
    }

    public String getKey() {
        return FluidRegistry.getFluidName(fluid) + "/" + Item.REGISTRY.getNameForObject(input.getItem()) + "/" + Item.REGISTRY.getNameForObject(output.getItem());
    }

    @Override
    public boolean canUseItem(TileEntityBarrel barrel, ItemStack item) {
        return item != null && ItemStack.areItemsEqual(item, input)
                && barrel.getFluid().getFluid() == this.fluid
                && barrel.getFluidTank().getFluidAmount() == barrel.getFluidTank().getCapacity();
    }

    @Override
    public boolean onUseItem(@Nullable EntityPlayer player, EnumHand hand, TileEntityBarrel barrel, ItemStack item) {
        if (item != null && ItemStack.areItemsEqual(item, input)
                && barrel.getFluid().getFluid() == this.fluid
                && barrel.getFluidTank().getFluidAmount() == barrel.getFluidTank().getCapacity()) {

            barrel.setState(BarrelStates.OUTPUT);
            barrel.setContents(output.copy());

            SoundEvent event;
            if (fluid == FluidRegistry.LAVA)
                event = SoundEvents.BLOCK_FIRE_EXTINGUISH;
            else
                event = SoundEvents.ENTITY_GENERIC_SPLASH;

            barrel.getWorld().playSound(null, barrel.getPos(), event, SoundCategory.BLOCKS, 0.5f, 4.5f);

            return true;
        }

        return false;
    }

    public static BarrelCraftingTrigger fromRecipe(BarrelCraftingRecipe recipe) {
        Fluid fluid = FluidRegistry.getFluid(recipe.getFluid());

        if (fluid != null) {
            ResourceLocation in = new ResourceLocation(recipe.getInput());
            ResourceLocation out = new ResourceLocation(recipe.getOutput());

            Item input = Item.REGISTRY.getObject(in);
            Item output = Item.REGISTRY.getObject(out);

            if (input != null && output != null)
                return new BarrelCraftingTrigger(fluid, new ItemStack(input, 1, recipe.getInputMeta()), new ItemStack(output, 1, recipe.getOutputMeta()));
        }

        return null;
    }

    public BarrelCraftingRecipe toRecipe() {
        String fluid = FluidRegistry.getFluidName(this.fluid);
        String input = Item.REGISTRY.getNameForObject(this.input.getItem()).toString();
        String output = Item.REGISTRY.getNameForObject(this.output.getItem()).toString();

        return new BarrelCraftingRecipe(fluid, input, this.input.getItemDamage(), output, this.output.getItemDamage());
    }
}
