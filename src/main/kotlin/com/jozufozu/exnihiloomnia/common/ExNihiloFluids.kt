package com.jozufozu.exnihiloomnia.common

import com.jozufozu.exnihiloomnia.common.blocks.ExNihiloBlocks
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidRegistry


object ExNihiloFluids {
    val WITCHWATER: Fluid = Fluid("exnihiloomnia:witchwater", ResourceLocation("exnihiloomnia:blocks/witchwater_still"), ResourceLocation("exnihiloomnia:blocks/witchwater_flowing")).setUnlocalizedName("exnihiloomnia.witchwater")

    fun preInit() {
        FluidRegistry.registerFluid(WITCHWATER)
        FluidRegistry.addBucketForFluid(WITCHWATER)
    }

    fun init() {
        WITCHWATER.block = ExNihiloBlocks.WITCHWATER
    }
}