package exnihiloomnia.compatibility.jei;

import exnihiloomnia.blocks.ENOBlocks;
import exnihiloomnia.compatibility.jei.categories.*;
import exnihiloomnia.items.ENOItems;
import exnihiloomnia.registries.composting.CompostRegistry;
import exnihiloomnia.registries.composting.CompostRegistryEntry;
import exnihiloomnia.registries.crucible.CrucibleRegistry;
import exnihiloomnia.registries.crucible.CrucibleRegistryEntry;
import exnihiloomnia.registries.hammering.HammerRegistry;
import exnihiloomnia.registries.hammering.HammerRegistryEntry;
import exnihiloomnia.registries.sifting.SieveRegistry;
import exnihiloomnia.registries.sifting.SieveRegistryEntry;
import exnihiloomnia.util.enums.EnumWood;
import mezz.jei.api.*;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;

@JEIPlugin
public class Plugin implements IModPlugin{
    private static ItemStack dirt = new ItemStack(Blocks.DIRT);

    @Override
    public void registerIngredients(IModIngredientRegistration registry) {
    }

    @Override
    public void register(@Nonnull IModRegistry registry) {
        registry.addRecipeCategories(
                new SieveRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
                new HammerRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
                new CrucibleRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
                new CompostRecipeCategory(registry.getJeiHelpers().getGuiHelper())

        );

        registry.addRecipeHandlers(
                new SieveRecipeHandler(),
                new HammerRecipeHandler(),
                new CrucibleRecipeHandler(),
                new CompostRecipeHandler()
        );

        registerSieveRecipes(registry);
        registerCrucibleRecipes(registry);
        registerHammerRecipes(registry);

        registerCompostRecipes(registry);

        registerCraftingItems(registry);

    }

    private void registerCraftingItems(IModRegistry registry) {
        for (EnumWood wood : EnumWood.values()) {
            registry.addRecipeCategoryCraftingItem(new ItemStack(ENOBlocks.SIEVE_WOOD, 1, wood.getMetadata()), SieveRecipeCategory.UID);
            registry.addRecipeCategoryCraftingItem(new ItemStack(ENOBlocks.BARREL_WOOD, 1, wood.getMetadata()), CompostRecipeCategory.UID);
        }
        for (EnumDyeColor color : EnumDyeColor.values())
            registry.addRecipeCategoryCraftingItem(new ItemStack(ENOBlocks.BARREL_GLASS_COLORED, 1, color.getMetadata()), CompostRecipeCategory.UID);
        registry.addRecipeCategoryCraftingItem(new ItemStack(ENOBlocks.BARREL_GLASS), CompostRecipeCategory.UID);

        registry.addRecipeCategoryCraftingItem(new ItemStack(ENOItems.HAMMER_DIAMOND), HammerRecipeCategory.UID);
        registry.addRecipeCategoryCraftingItem(new ItemStack(ENOItems.HAMMER_GOLD), HammerRecipeCategory.UID);
        registry.addRecipeCategoryCraftingItem(new ItemStack(ENOItems.HAMMER_IRON), HammerRecipeCategory.UID);
        registry.addRecipeCategoryCraftingItem(new ItemStack(ENOItems.HAMMER_STONE), HammerRecipeCategory.UID);
        registry.addRecipeCategoryCraftingItem(new ItemStack(ENOItems.HAMMER_WOOD), HammerRecipeCategory.UID);

        registry.addRecipeCategoryCraftingItem(new ItemStack(ENOBlocks.CRUCIBLE), CrucibleRecipeCategory.UID);
    }

    private void registerCompostRecipes(IModRegistry registry) {
        ArrayList<JEICompostRecipe> compostRecipes = new ArrayList<JEICompostRecipe>();

        for (CompostRegistryEntry entry : CompostRegistry.INSTANCE.getEntries().values()) {
            Block block = Block.getBlockFromItem(entry.getInput().getItem());

            if (block != null && block.getMaterial(null) == Material.LEAVES)
                compostRecipes.add(new JEICompostRecipe(entry, entry.getInput(), new ItemStack(Blocks.DIRT, 1, 2)));
            else if (entry.getInput().getItem() == Items.GOLDEN_APPLE)
                compostRecipes.add(new JEICompostRecipe(entry, entry.getInput(), new ItemStack(Blocks.GRASS)));
            else if (entry.getInput().getItem() == Items.ROTTEN_FLESH)
                compostRecipes.add(new JEICompostRecipe(entry, entry.getInput(), new ItemStack(Blocks.DIRT, 1, 1)));
            else
                compostRecipes.add(new JEICompostRecipe(entry, entry.getInput(), dirt));
        }
        registry.addRecipes(compostRecipes);
    }

    private void registerSieveRecipes(IModRegistry registry) {
        ArrayList<JEISieveRecipe> sieveRecipes = new ArrayList<JEISieveRecipe>();
        for (SieveRegistryEntry entry : SieveRegistry.getEntryMap().values()) {
            sieveRecipes.add(new JEISieveRecipe(entry));
        }
        registry.addRecipes(sieveRecipes);
    }

    private void registerCrucibleRecipes(IModRegistry registry) {
        ArrayList<JEICrucibleRecipe> crucibleRecipes = new ArrayList<JEICrucibleRecipe>();
        for (CrucibleRegistryEntry entry : CrucibleRegistry.INSTANCE.getEntries().values()) {
            crucibleRecipes.add(new JEICrucibleRecipe(entry));
        }
        registry.addRecipes(crucibleRecipes);
    }

    private void registerHammerRecipes(IModRegistry registry) {
        ArrayList<JEIHammerRecipe> hammerRecipes = new ArrayList<JEIHammerRecipe>();

        for (HammerRegistryEntry entry : HammerRegistry.INSTANCE.getEntries().values()) {
            hammerRecipes.add(new JEIHammerRecipe(entry));
        }
        registry.addRecipes(hammerRecipes);
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {
    }

    @Override
    public void onRuntimeAvailable(@Nonnull IJeiRuntime jeiRuntime) {

    }
}
