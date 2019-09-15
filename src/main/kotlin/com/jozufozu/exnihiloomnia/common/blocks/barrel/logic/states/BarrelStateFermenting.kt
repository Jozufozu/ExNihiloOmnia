package com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.states

import com.jozufozu.exnihiloomnia.client.RenderUtil
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.BarrelState
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.BarrelStates
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.CountdownLogic
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.TileEntityBarrel
import com.jozufozu.exnihiloomnia.common.registries.recipes.FermentingRecipe
import com.jozufozu.exnihiloomnia.common.util.Color
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fluids.FluidRegistry
import org.lwjgl.opengl.GL11

class BarrelStateFermenting(val recipe: FermentingRecipe, name: ResourceLocation) : BarrelState(name) {
    val output = recipe.output
    init {
        this.logic.add(CountdownLogic({ recipe.time }, {
            it.fluid = recipe.output
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

        val waterTexture = textureMapBlocks.getAtlasSprite((barrel.fluid?.fluid ?: FluidRegistry.WATER).still.toString())
        val witchWaterTexture = textureMapBlocks.getAtlasSprite(output.fluid.still.toString())

        val progress: Float = (recipe.time - barrel.timer).toFloat() / recipe.time
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