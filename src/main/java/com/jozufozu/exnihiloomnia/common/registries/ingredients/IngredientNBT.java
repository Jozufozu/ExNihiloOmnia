package com.jozufozu.exnihiloomnia.common.registries.ingredients;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nullable;

/**
 * Forge's has a protected constructor
 */
public class IngredientNBT extends Ingredient
{
    private final ItemStack stack;
    public IngredientNBT(ItemStack stack)
    {
        super(stack);
        this.stack = stack;
    }
    
    @Override
    public boolean apply(@Nullable ItemStack input)
    {
        if (input == null)
            return false;
        return ItemStack.areItemStacksEqualUsingNBTShareTag(this.stack, input);
    }
}
