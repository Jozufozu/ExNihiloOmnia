package com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.states

import com.jozufozu.exnihiloomnia.client.RenderUtil
import com.jozufozu.exnihiloomnia.common.ExNihiloFluids
import com.jozufozu.exnihiloomnia.common.ModConfig
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.BarrelState
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.BarrelStates
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.CountdownLogic
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.TileEntityBarrel
import com.jozufozu.exnihiloomnia.common.util.Color
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fluids.FluidStack
import org.lwjgl.opengl.GL11

class BarrelStateFermenting : BarrelState(BarrelStates.ID_FERMENTING) {
    init {
        this.logic.add(CountdownLogic({ ModConfig.blocks.barrel.fermentTime }, {
            it.fluid = FluidStack(ExNihiloFluids.WITCHWATER, ModConfig.blocks.barrel.fluidCapacity)
            it.state = BarrelStates.FLUID
        }))
    }

    override fun draw(barrel: TileEntityBarrel, x: Double, y: Double, z: Double, partialTicks: Float) {
        super.draw(barrel, x, y, z, partialTicks)

        GlStateManager.pushMatrix()
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        GlStateManager.enableBlend()
        GlStateManager.disableCull()

        if (Minecraft.isAmbientOcclusionEnabled()) {
            GlStateManager.shadeModel(GL11.GL_SMOOTH)
        } else {
            GlStateManager.shadeModel(GL11.GL_FLAT)
        }

        Minecraft.getMinecraft().textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE)
        val textureMapBlocks = Minecraft.getMinecraft().textureMapBlocks

        val waterTexture = textureMapBlocks.getAtlasSprite(FluidRegistry.WATER.still.toString())
        val witchWaterTexture = textureMapBlocks.getAtlasSprite(ExNihiloFluids.WITCHWATER.still.toString())

        val progress: Float = (ModConfig.blocks.barrel.fermentTime - barrel.timer).toFloat() / ModConfig.blocks.barrel.fermentTime
        val waterTint = Color(1.0f, 1.0f, 1.0f, 1.0f - progress)
        val witchWaterTint = Color(1.0f, 1.0f, 1.0f, progress)

        GlStateManager.translate(x + 0.125, y + 0.0625, z + 0.125)
        GlStateManager.scale(0.75, 1.0, 0.75)

        RenderHelper.disableStandardItemLighting()

        if (barrel.world.getBlockState(barrel.pos).material.isOpaque) {
            RenderUtil.renderContents(waterTexture, 0.875, waterTint)
            RenderUtil.renderContents(witchWaterTexture, 0.875, witchWaterTint)
        } else {
            RenderUtil.renderContents3D(waterTexture, waterTexture, 0.875, waterTint)
            RenderUtil.renderContents3D(witchWaterTexture, witchWaterTexture, 0.875, witchWaterTint)
        }

        RenderHelper.enableStandardItemLighting()

        GlStateManager.disableRescaleNormal()
        GlStateManager.disableBlend()

        GlStateManager.popMatrix()
    }
}