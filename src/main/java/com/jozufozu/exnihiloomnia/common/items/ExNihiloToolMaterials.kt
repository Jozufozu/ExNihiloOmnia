package com.jozufozu.exnihiloomnia.common.items

import net.minecraft.item.IItemTier
import net.minecraft.item.Items
import net.minecraft.item.crafting.Ingredient

enum class ExNihiloToolMaterials(private val harvestLevel: Int,
                                 private val maxUses: Int,
                                 private val attackDamage: Float,
                                 private val enchantability: Int,
                                 private val efficiency: Float,
                                 lazyRepairMaterialSupplier: () -> Ingredient): IItemTier {
    SILK(0, 128, 0.0f, 18, 10.0f, { Ingredient.fromItems(Items.STRING) }),
    BONE(1, 200, 2.0f, 25, 8.0f, { Ingredient.fromItems(Items.BONE) });

    private val repair: Ingredient by lazy(lazyRepairMaterialSupplier)

    override fun getHarvestLevel(): Int = harvestLevel
    override fun getMaxUses(): Int = maxUses
    override fun getAttackDamage(): Float = attackDamage
    override fun getEnchantability(): Int = enchantability
    override fun getEfficiency(): Float = efficiency
    override fun getRepairMaterial(): Ingredient = repair

}