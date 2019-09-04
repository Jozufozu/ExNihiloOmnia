package com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.states

import com.jozufozu.exnihiloomnia.client.RenderUtil
import com.jozufozu.exnihiloomnia.common.ModConfig
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.BarrelState
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.BarrelStates
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.CountdownLogic
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.TileEntityBarrel
import com.jozufozu.exnihiloomnia.common.util.MathStuff
import com.jozufozu.exnihiloomnia.proxy.ClientProxy
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.block.model.ItemCameraTransforms
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.entity.Entity
import net.minecraft.init.Blocks
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import org.lwjgl.opengl.GL11

class BarrelStateComposting : BarrelState(BarrelStates.ID_COMPOSTING) {
    init {
        this.logic.add(CountdownLogic({ ModConfig.blocks.barrel.compostTime }, { it.state = BarrelStates.ITEMS }))
    }

    override fun addCollisionBoxToList(barrel: TileEntityBarrel, state: IBlockState, worldIn: World, pos: BlockPos, entityBox: AxisAlignedBB, collidingBoxes: MutableList<AxisAlignedBB>, entityIn: Entity?) {
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AxisAlignedBB(2.0 / 16.0, 1.0 / 16.0, 2.0 / 16.0, 14.0 / 16.0, 1.0 / 16.0 + (barrel.compostAmount / TileEntityBarrel.compostCapacity) * 14.0 / 16.0, 14.0 / 16.0))
    }

    override fun draw(barrel: TileEntityBarrel, x: Double, y: Double, z: Double, partialTicks: Float) {
        super.draw(barrel, x, y, z, partialTicks)

        val timerNow = (ModConfig.blocks.barrel.compostTime - barrel.timer).toFloat() / ModConfig.blocks.barrel.compostTime.toFloat()
        val timerLast = (ModConfig.blocks.barrel.compostTime - barrel.timerLastTick).toFloat() / ModConfig.blocks.barrel.compostTime.toFloat()

        val progress = MathStuff.lerp(timerLast, timerNow, partialTicks)

        Minecraft.getMinecraft().textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE)

        val contents = barrel.item

        if (contents.isEmpty)
            return

        GlStateManager.pushMatrix()
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        GlStateManager.disableCull()
        GlStateManager.enableAlpha()
        GlStateManager.enableBlend()

        if (Minecraft.isAmbientOcclusionEnabled()) {
            GlStateManager.shadeModel(GL11.GL_SMOOTH)
        } else {
            GlStateManager.shadeModel(GL11.GL_FLAT)
        }

        GlStateManager.translate(x + 0.5, y + 0.0625, z + 0.5)

        if (Block.getBlockFromItem(contents.item) !== Blocks.AIR) {
            GlStateManager.translate(0.0, 0.4375, 0.0)
            GlStateManager.scale(0.75, 0.875, 0.75)
        } else {
            GlStateManager.translate(0.0, 0.03125, 0.0)
            GlStateManager.scale(0.75, 0.75, 0.75)
            GlStateManager.rotate(90f, 1f, 0f, 0f)
        }

        GlStateManager.scale(0.9999, 0.9999, 0.9999)
        Minecraft.getMinecraft().itemRenderer.renderItem(BarrelState.renderSlave, contents, ItemCameraTransforms.TransformType.NONE)

        RenderHelper.enableStandardItemLighting()

        GlStateManager.disableBlend()
        GlStateManager.popMatrix()

        GlStateManager.pushMatrix()
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        GlStateManager.disableCull()
        GlStateManager.enableAlpha()
        GlStateManager.enableBlend()

        if (Minecraft.isAmbientOcclusionEnabled()) {
            GlStateManager.shadeModel(GL11.GL_SMOOTH)
        } else {
            GlStateManager.shadeModel(GL11.GL_FLAT)
        }

        GlStateManager.translate(x + 0.125, y + 0.0625, z + 0.125)
        GlStateManager.scale(0.75, 1.0, 0.75)

        RenderHelper.disableStandardItemLighting()
        if (barrel.world.getBlockState(barrel.pos).material.isOpaque) {
            RenderUtil.renderContents(ClientProxy.COMPOST, 0.875, barrel.color.withAlpha(1.0f - progress))
        } else {
            RenderUtil.renderContents3D(ClientProxy.COMPOST, ClientProxy.COMPOST, 0.875, barrel.color.withAlpha(1.0f - progress))
        }

        GlStateManager.popMatrix()
    }
}
