package exnihiloomnia.compatibility.jei;

import exnihiloomnia.blocks.ENOBlocks;
import exnihiloomnia.compatibility.jei.categories.*;
import exnihiloomnia.items.ENOItems;
import exnihiloomnia.registries.crucible.CrucibleRegistry;
import exnihiloomnia.registries.crucible.CrucibleRegistryEntry;
import exnihiloomnia.registries.hammering.HammerRegistry;
import exnihiloomnia.registries.hammering.HammerRegistryEntry;
import exnihiloomnia.registries.sifting.SieveRegistry;
import exnihiloomnia.registries.sifting.SieveRegistryEntry;
import exnihiloomnia.util.enums.EnumWood;
import mezz.jei.api.*;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;

@JEIPlugin
public class Plugin implements IModPlugin{
    @Override
    public void register(@Nonnull IModRegistry registry) {
        registry.addRecipeCategories(
                new SieveRecipeCategory(registry.getJeiHelpers().getGuiHelper())
        );

        registry.addRecipeHandlers(
                new SieveRecipeHandler()
        );

        ArrayList<JEISieveRecipe> sieveRecipes = new ArrayList<JEISieveRecipe>();
        ArrayList<JEICrucibleRecipe> crucibleRecipes = new ArrayList<JEICrucibleRecipe>();
        ArrayList<JEIHammerRecipe> hammerRecipes = new ArrayList<JEIHammerRecipe>();

        for (SieveRegistryEntry entry : SieveRegistry.getEntryMap().values()) {
            sieveRecipes.add(new JEISieveRecipe(entry));
        }
        registry.addRecipes(sieveRecipes);

        for (CrucibleRegistryEntry entry : CrucibleRegistry.getEntryMap().values()) {
            crucibleRecipes.add(new JEICrucibleRecipe(entry));
        }
        registry.addRecipes(crucibleRecipes);

        for (HammerRegistryEntry entry : HammerRegistry.getEntryMap().values()) {
            hammerRecipes.add(new JEIHammerRecipe(entry));
        }
        registry.addRecipes(hammerRecipes);

        for (EnumWood wood : EnumWood.values())
            registry.addRecipeCategoryCraftingItem(new ItemStack(ENOBlocks.SIEVE_WOOD, 1, wood.getMetadata()), SieveRecipeCategory.UID);
    }

    @Override
    public void onRuntimeAvailable(@Nonnull IJeiRuntime jeiRuntime) {

    }
}
