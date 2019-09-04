package com.jozufozu.exnihiloomnia.common.blocks.barrel.logic

class CountdownLogic(val initialTime: () -> Int, val onComplete: (TileEntityBarrel) -> Unit) : BarrelLogic() {
    override fun onActivate(barrel: TileEntityBarrel) {
        barrel.timer = initialTime()
    }

    override fun onUpdate(barrel: TileEntityBarrel): Boolean {
        barrel.timer--
        if (!barrel.world.isRemote) {
            if (barrel.timer <= 0) {
                barrel.timer = 0
                onComplete(barrel)
                return true
            }
        }
        return false
    }
}