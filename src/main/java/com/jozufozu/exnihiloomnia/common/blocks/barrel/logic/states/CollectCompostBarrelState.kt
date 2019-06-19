package com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.states

import com.jozufozu.exnihiloomnia.client.RenderUtil
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.*
import com.jozufozu.exnihiloomnia.common.registries.RegistryManager
import com.jozufozu.exnihiloomnia.common.util.Color
import com.jozufozu.exnihiloomnia.common.util.MathStuff
import com.jozufozu.exnihiloomnia.proxy.ClientProxy
import com.mojang.blaze3d.platform.GlStateManager
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.texture.AtlasTexture
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.world.World
import org.lwjgl.opengl.GL11

class CollectCompostBarrelState : BarrelState(BarrelStates.ID_COLLECT_COMPOST) {
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

        Minecraft.getInstance().getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE)

        GlStateManager.pushMatrix()
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        GlStateManager.enableBlend()
        GlStateManager.enableCull()

        if (Minecraft.isAmbientOcclusionEnabled()) {
            GlStateManager.shadeModel(GL11.GL_SMOOTH)
        } else {
            GlStateManager.shadeModel(GL11.GL_FLAT)
        }

        GlStateManager.translated(x + 0.125, y + 0.0625, z + 0.125)
        GlStateManager.scaled(0.75, 1.0, 0.75)

        RenderHelper.disableStandardItemLighting()
        if (barrel.world!!.getBlockState(barrel.pos).material.isOpaque) {
            RenderUtil.renderContents(ClientProxy.COMPOST, height, barrel.color!!)
        } else {
            RenderUtil.renderContents3D(ClientProxy.COMPOST, ClientProxy.COMPOST, height, barrel.color!!)
        }
        RenderHelper.enableStandardItemLighting()


        GlStateManager.disableRescaleNormal()
        GlStateManager.disableBlend()

        GlStateManager.popMatrix()
    }

    class CompostCollectionLogic : BarrelLogic() {
        override fun canUseItem(barrel: TileEntityBarrel, world: World, itemStack: ItemStack, player: PlayerEntity?, hand: Hand?): Boolean {
            return RegistryManager.getCompost(itemStack) != null
        }

        override fun onUseItem(barrel: TileEntityBarrel, world: World, itemStack: ItemStack, player: PlayerEntity?, hand: Hand?): InteractResult {
            RegistryManager.getCompost(itemStack)?.let {
                if (barrel.world?.isRemote == false) {
                    if (barrel.item.isEmpty) {
                        barrel.item = it.getOutput()
                    } else if (!ItemStack.areItemStacksEqual(barrel.item, it.getOutput())) {
                        return InteractResult.PASS
                    }

                    if (barrel.color == null) {
                        barrel.color = it.color
                    } else {
                        val weight = barrel.compostAmount.toFloat() / (barrel.compostAmount + it.amount).toFloat()
                        barrel.color = Color.weightedAverage(it.color!!, barrel.color!!, weight)
                    }

                    barrel.compostAmount += it.amount
                    barrel.compostAmount = Math.min(barrel.compostAmount, barrel.compostCapacity)

                    if (barrel.compostAmount == barrel.compostCapacity) {
                        barrel.state = BarrelStates.COMPOSTING
                    }
                }
                return InteractResult.CONSUME
            }

            return InteractResult.PASS
        }
    }
}
