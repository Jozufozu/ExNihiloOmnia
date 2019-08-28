package com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.states

import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.BarrelState
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.BarrelStates
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.TileEntityBarrel

class BarrelStateItem : BarrelState(BarrelStates.ID_ITEMS) {

    override fun canInteractWithFluids(barrel: TileEntityBarrel): Boolean {
        return false
    }

    override fun canExtractItems(barrel: TileEntityBarrel): Boolean {
        return true
    }

    override fun draw(barrel: TileEntityBarrel, x: Double, y: Double, z: Double, partialTicks: Float) {
        super.draw(barrel, x, y, z, partialTicks)

        drawContents(barrel, x, y, z, partialTicks)
    }
}
