package com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.states

import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.BarrelState
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.BarrelStates
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.BarrelTileEntity

class ItemBarrelState : BarrelState(BarrelStates.ID_ITEMS) {

    override fun canInteractWithFluids(barrel: BarrelTileEntity) = false

    override fun canExtractItems(barrel: BarrelTileEntity) = true

    override fun draw(barrel: BarrelTileEntity, x: Double, y: Double, z: Double, partialTicks: Float) {
        super.draw(barrel, x, y, z, partialTicks)

        renderContents(barrel, x, y, z, partialTicks)
    }
}
