package com.jozufozu.exnihiloomnia.common.blocks.barrel.logic

import com.jozufozu.exnihiloomnia.common.blocks.barrel.BarrelTileEntity

class CountdownLogic(val initialTime: () -> Int, val onComplete: (BarrelTileEntity) -> Unit) : BarrelLogic() {
    override fun onActivate(barrel: BarrelTileEntity) {
        barrel.timer = initialTime()
    }

    override fun onUpdate(barrel: BarrelTileEntity): Boolean {
        barrel.timer--
        if (!barrel.world!!.isRemote) {
            if (barrel.timer <= 0) {
                barrel.timer = 0
                onComplete(barrel)
                return true
            }
        }
        return false
    }
}