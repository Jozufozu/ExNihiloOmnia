package com.jozufozu.exnihiloomnia.client.tileentities

import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.TileEntityBarrel
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer

class TileEntityBarrelRenderer : TileEntitySpecialRenderer<TileEntityBarrel>() {
    override fun render(te: TileEntityBarrel, x: Double, y: Double, z: Double, partialTicks: Float, destroyStage: Int, alpha: Float) {
        te.state.draw(te, x, y, z, partialTicks)

        super.render(te, x, y, z, partialTicks, destroyStage, alpha)
    }
}
