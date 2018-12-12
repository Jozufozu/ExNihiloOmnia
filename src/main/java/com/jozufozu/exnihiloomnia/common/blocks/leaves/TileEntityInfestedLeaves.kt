package com.jozufozu.exnihiloomnia.common.blocks.leaves

class TileEntityInfestedLeaves : TileEntity(), ITickable {
    companion object {
        private const val infestMax: Int = 20 * 30
    }

    var mimic: IBlockState = Blocks.LEAVES.defaultState
    var infestTimer: Int = 0
        private set

    override fun update() {
        if (infestTimer < infestMax) {
            infestTimer++
        }
    }
}
