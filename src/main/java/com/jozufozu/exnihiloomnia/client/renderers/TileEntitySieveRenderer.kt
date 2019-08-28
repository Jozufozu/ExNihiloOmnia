package com.jozufozu.exnihiloomnia.client.renderers

import com.jozufozu.exnihiloomnia.common.blocks.sieve.TileEntitySieve
import com.jozufozu.exnihiloomnia.common.util.MathStuff
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.block.model.ItemCameraTransforms
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.monster.EntityGuardian
import net.minecraft.item.ItemStack
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.IItemHandler

class TileEntitySieveRenderer : TileEntitySpecialRenderer<TileEntitySieve>() {

    override fun render(sieve: TileEntitySieve, x: Double, y: Double, z: Double, partialTicks: Float, destroyStage: Int, alpha: Float) {
        val itemHandler = sieve.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null) ?: return

        val contents = itemHandler.getStackInSlot(0)
        val mesh = itemHandler.getStackInSlot(1)

        //if (mesh == ItemStack.EMPTY)
        //    return;

        if (!isSlaveReady) renderSlave = EntityGuardian(world)

        GlStateManager.pushMatrix()

        GlStateManager.translate(x + 0.5, y + height, z + 0.5)
        GlStateManager.scale(scale, 0.2, scale)
        GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f)

        Minecraft.getMinecraft().itemRenderer.renderItem(renderSlave, mesh, ItemCameraTransforms.TransformType.NONE)

        if (contents != ItemStack.EMPTY) {
            val progress = MathStuff.lerp(sieve.countdownLastTick.toDouble() / sieve.requiredTime, sieve.countdown.toDouble() / sieve.requiredTime, partialTicks.toDouble())

            val contentsSize = 7.0 / 16.0 * progress

            GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f)

            GlStateManager.scale(1f, 5f, 1f)
            GlStateManager.translate(0.0, contentsSize / 2.0, 0.0)
            GlStateManager.scale(1.0, contentsSize, 1.0)

            Minecraft.getMinecraft().itemRenderer.renderItem(renderSlave, contents, ItemCameraTransforms.TransformType.NONE)
        }

        GlStateManager.popMatrix()
    }

    companion object {
        private lateinit var renderSlave: EntityLivingBase
        private val isSlaveReady: Boolean get() = ::renderSlave.isInitialized

        private const val scale = 14.0 / 16.0
        private const val height = 9.0 / 16.0
    }
}
