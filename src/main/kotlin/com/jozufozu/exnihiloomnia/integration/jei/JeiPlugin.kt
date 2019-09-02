package com.jozufozu.exnihiloomnia.integration.jei

import com.jozufozu.exnihiloomnia.common.blocks.ExNihiloBlocks
import com.jozufozu.exnihiloomnia.common.items.ExNihiloItems
import com.jozufozu.exnihiloomnia.common.registries.RegistryManager
import com.jozufozu.exnihiloomnia.common.registries.recipes.CompostRecipe
import com.jozufozu.exnihiloomnia.common.registries.recipes.HammerRecipe
import com.jozufozu.exnihiloomnia.common.registries.recipes.MeltingRecipe
import com.jozufozu.exnihiloomnia.common.registries.recipes.SieveRecipe
import com.jozufozu.exnihiloomnia.integration.jei.composting.CompostRecipeCategory
import com.jozufozu.exnihiloomnia.integration.jei.composting.CompostRecipeWrapper
import com.jozufozu.exnihiloomnia.integration.jei.hammer.HammerRecipeCategory
import com.jozufozu.exnihiloomnia.integration.jei.hammer.HammerRecipeWrapper
import com.jozufozu.exnihiloomnia.integration.jei.melting.MeltingRecipeCategory
import com.jozufozu.exnihiloomnia.integration.jei.melting.MeltingRecipeWrapper
import com.jozufozu.exnihiloomnia.integration.jei.sieve.SieveRecipeCategory
import com.jozufozu.exnihiloomnia.integration.jei.sieve.SieveRecipeWrapper
import mezz.jei.api.IModPlugin
import mezz.jei.api.IModRegistry
import mezz.jei.api.JEIPlugin
import mezz.jei.api.recipe.IRecipeCategoryRegistration
import net.minecraft.block.BlockPlanks
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.ItemStack
import java.util.*

@JEIPlugin
class JeiPlugin : IModPlugin {
    override fun registerCategories(registry: IRecipeCategoryRegistration) {
        val jeiHelpers = registry.jeiHelpers
        val guiHelper = jeiHelpers.guiHelper

        registry.addRecipeCategories(HammerRecipeCategory(guiHelper))
        registry.addRecipeCategories(SieveRecipeCategory(guiHelper))
        registry.addRecipeCategories(CompostRecipeCategory(guiHelper))
        registry.addRecipeCategories(MeltingRecipeCategory(guiHelper))
    }

    override fun register(registry: IModRegistry) {
        registry.handleRecipes(HammerRecipe::class.java, ::HammerRecipeWrapper, HammerRecipeCategory.UID)

        registry.handleRecipes(SieveRecipe::class.java, ::SieveRecipeWrapper, SieveRecipeCategory.UID)
        registry.handleRecipes(CompostRecipe::class.java, ::CompostRecipeWrapper, CompostRecipeCategory.UID)
        registry.handleRecipes(MeltingRecipe::class.java, ::MeltingRecipeWrapper, MeltingRecipeCategory.UID)

        registry.addRecipes(RegistryManager.SIFTING.getValues(), SieveRecipeCategory.UID)
        registry.addRecipes(RegistryManager.COMPOST.getValues(), CompostRecipeCategory.UID)
        registry.addRecipes(RegistryManager.MELTING.getValues(), MeltingRecipeCategory.UID)
        registry.addRecipes(RegistryManager.HAMMERING.getValues(), HammerRecipeCategory.UID)

        registry.addRecipeCatalyst(ItemStack(ExNihiloItems.DIAMOND_HAMMER), HammerRecipeCategory.UID)
        registry.addRecipeCatalyst(ItemStack(ExNihiloItems.GOLD_HAMMER), HammerRecipeCategory.UID)
        registry.addRecipeCatalyst(ItemStack(ExNihiloItems.IRON_HAMMER), HammerRecipeCategory.UID)
        registry.addRecipeCatalyst(ItemStack(ExNihiloItems.STONE_HAMMER), HammerRecipeCategory.UID)
        registry.addRecipeCatalyst(ItemStack(ExNihiloItems.WOOD_HAMMER), HammerRecipeCategory.UID)

        for (i in BlockPlanks.EnumType.values().indices) {
            registry.addRecipeCatalyst(ItemStack(ExNihiloBlocks.SIEVE, 1, i), SieveRecipeCategory.UID)
        }

        registry.addRecipeCatalyst(ItemStack(ExNihiloBlocks.BARREL_GLASS), CompostRecipeCategory.UID)
        registry.addRecipeCatalyst(ItemStack(ExNihiloBlocks.BARREL_STONE), CompostRecipeCategory.UID)
        for (i in BlockPlanks.EnumType.values().indices) {
            registry.addRecipeCatalyst(ItemStack(ExNihiloBlocks.BARREL_WOOD, 1, i), CompostRecipeCategory.UID)
        }

        for (i in EnumDyeColor.values().indices) {
            registry.addRecipeCatalyst(ItemStack(ExNihiloBlocks.BARREL_CONCRETE, 1, i), CompostRecipeCategory.UID)
            registry.addRecipeCatalyst(ItemStack(ExNihiloBlocks.BARREL_TERRACOTTA, 1, i), CompostRecipeCategory.UID)
            registry.addRecipeCatalyst(ItemStack(ExNihiloBlocks.BARREL_STAINED_GLASS, 1, i), CompostRecipeCategory.UID)
        }

        registry.addRecipeCatalyst(ItemStack(ExNihiloBlocks.CRUCIBLE), MeltingRecipeCategory.UID)
    }

    companion object {

        fun getRewardsOutput(rewards: Collection<ItemStack>, slots: Int): List<List<ItemStack>> {
            val drops = ArrayList(rewards)
            val out = ArrayList<ArrayList<ItemStack>>(slots)

            for (i in 0 until slots) {
                out.add(ArrayList())
            }

            for (i in drops.indices) {
                out[i % slots].add(drops[i])
            }

            return out
        }
    }
}
