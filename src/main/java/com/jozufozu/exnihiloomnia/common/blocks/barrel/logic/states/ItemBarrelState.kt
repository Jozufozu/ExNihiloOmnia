package com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.states

import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.BarrelState
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.BarrelStates
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.TileEntityBarrel

class ItemBarrelState : BarrelState(BarrelStates.ID_ITEMS) {

    override fun canInteractWithFluids(barrel: TileEntityBarrel) = false

    override fun canExtractItems(barrel: TileEntityBarrel) = true

    override fun draw(barrel: TileEntityBarrel, x: Double, y: Double, z: Double, partialTicks: Float) {
        super.draw(barrel, x, y, z, partialTicks)

        renderContents(barrel, x, y, z, partialTicks)
    }
}
