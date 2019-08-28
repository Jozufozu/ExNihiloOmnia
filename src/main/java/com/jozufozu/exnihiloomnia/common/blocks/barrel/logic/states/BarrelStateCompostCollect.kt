package com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.states

import com.jozufozu.exnihiloomnia.client.RenderUtil
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.*
import com.jozufozu.exnihiloomnia.common.registries.RegistryManager
import com.jozufozu.exnihiloomnia.common.registries.recipes.CompostRecipe
import com.jozufozu.exnihiloomnia.common.util.Color
import com.jozufozu.exnihiloomnia.common.util.MathStuff
import com.jozufozu.exnihiloomnia.proxy.ClientProxy
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumHand
import org.lwjgl.opengl.GL11
import kotlin.math.min

class BarrelStateCompostCollect : BarrelState(BarrelStates.ID_COMPOST_COLLECT) {
    init {

        this.logic.add(CompostCollectionLogic())
    }

    override fun canInteractWithItems(barrel: TileEntityBarrel): Boolean {
        return true
    }

    override fun canInteractWithFluids(barrel: TileEntityBarrel): Boolean {
        return false
    }

    override fun draw(barrel: TileEntityBarrel, x: Double, y: Double, z: Double, partialTicks: Float) {
        super.draw(barrel, x, y, z, partialTicks)

        val height = 0.875 * MathStuff.lerp(barrel.compostAmountLastTick.toDouble() / barrel.compostCapacity.toDouble(), barrel.compostAmount.toDouble() / barrel.compostCapacity.toDouble(), partialTicks.toDouble())

        Minecraft.getMinecraft().textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE)

        GlStateManager.pushMatrix()
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        GlStateManager.enableBlend()
        GlStateManager.enableCull()

        if (Minecraft.isAmbientOcclusionEnabled()) {
            GlStateManager.shadeModel(GL11.GL_SMOOTH)
        } else {
            GlStateManager.shadeModel(GL11.GL_FLAT)
        }

        GlStateManager.translate(x + 0.125, y + 0.0625, z + 0.125)
        GlStateManager.scale(0.75, 1.0, 0.75)

        RenderHelper.disableStandardItemLighting()
        if (barrel.world.getBlockState(barrel.pos).material.isOpaque) {
            RenderUtil.renderContents(ClientProxy.COMPOST, height, barrel.color ?: Color.WHITE )
        } else {
            RenderUtil.renderContents3D(ClientProxy.COMPOST, ClientProxy.COMPOST, height, barrel.color ?: Color.WHITE )
        }
        RenderHelper.enableStandardItemLighting()


        GlStateManager.disableRescaleNormal()
        GlStateManager.disableBlend()

        GlStateManager.popMatrix()
    }

    class CompostCollectionLogic : BarrelLogic() {
        override fun canUseItem(barrel: TileEntityBarrel, player: EntityPlayer?, hand: EnumHand?, itemStack: ItemStack): Boolean {
            return RegistryManager.getCompost(itemStack) != null
        }

        override fun onUseItem(barrel: TileEntityBarrel, player: EntityPlayer?, hand: EnumHand?, itemStack: ItemStack): EnumInteractResult {
            val compostRecipe = RegistryManager.getCompost(itemStack)
            if (compostRecipe != null) {
                if (!barrel.world.isRemote) {
                    if (barrel.item.isEmpty) {
                        barrel.item = compostRecipe.output
                    } else if (!ItemStack.areItemStacksEqual(barrel.item, compostRecipe.output)) {
                        return EnumInteractResult.PASS
                    }

                    barrel.color.let {
                        if (it == null) {
                            barrel.color = compostRecipe.color
                        } else {
                            val weight = barrel.compostAmount.toFloat() / (barrel.compostAmount + compostRecipe.amount).toFloat()
                            barrel.color = Color.weightedAverage(compostRecipe.color, it, weight)
                        }
                    }

                    barrel.compostAmount += compostRecipe.amount
                    barrel.compostAmount = min(barrel.compostAmount, barrel.compostCapacity)

                    if (barrel.compostAmount == barrel.compostCapacity) {
                        barrel.state = BarrelStates.COMPOSTING
                    }
                }
                return EnumInteractResult.CONSUME
            }

            return EnumInteractResult.PASS
        }
    }
}
