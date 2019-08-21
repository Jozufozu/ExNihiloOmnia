package com.jozufozu.exnihiloomnia.client.renderers

import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.BarrelTileEntity
import net.minecraft.client.renderer.tileentity.TileEntityRenderer

class BarrelTileEntityRenderer : TileEntityRenderer<BarrelTileEntity>() {
    override fun render(te: BarrelTileEntity, x: Double, y: Double, z: Double, partialTicks: Float, destroyStage: Int) {
        te.state.draw(te, x, y, z, partialTicks)
        super.render(te, x, y, z, partialTicks, destroyStage)
    }
}
