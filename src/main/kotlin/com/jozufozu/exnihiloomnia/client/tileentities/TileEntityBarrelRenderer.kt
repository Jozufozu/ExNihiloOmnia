package com.jozufozu.exnihiloomnia.client.tileentities

import com.jozufozu.exnihiloomnia.common.blocks.barrel.BarrelTileEntity
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer

class TileEntityBarrelRenderer : TileEntitySpecialRenderer<BarrelTileEntity>() {
    override fun render(te: BarrelTileEntity, x: Double, y: Double, z: Double, partialTicks: Float, destroyStage: Int, alpha: Float) {
        te.state.draw(te, x, y, z, partialTicks)

        super.render(te, x, y, z, partialTicks, destroyStage, alpha)
    }
}
