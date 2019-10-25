package com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.states

import com.jozufozu.exnihiloomnia.client.RenderUtil
import com.jozufozu.exnihiloomnia.common.blocks.barrel.BarrelTileEntity
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.BarrelLogic
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.BarrelState
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.BarrelStates
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.EnumInteractResult
import com.jozufozu.exnihiloomnia.common.registries.RegistryManager
import com.jozufozu.exnihiloomnia.common.util.Color
import com.jozufozu.exnihiloomnia.common.util.MathStuff
import com.jozufozu.exnihiloomnia.proxy.ClientProxy
import net.minecraft.block.state.BlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumHand
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import org.lwjgl.opengl.GL11
import kotlin.math.min

object BarrelStateCompostCollect : BarrelState(BarrelStates.ID_COMPOST_COLLECT) {
    init {
        logic.add(CompostCollectionLogic)
    }

    override fun addCollisionBoxToList(barrel: BarrelTileEntity, state: BlockState, worldIn: World, pos: BlockPos, entityBox: AxisAlignedBB, collidingBoxes: MutableList<AxisAlignedBB>, entityIn: Entity?) {
        addCollisionBoxToList(pos, entityBox, collidingBoxes, barrel.compostBB)
    }

    override fun canInteractWithItems(barrel: BarrelTileEntity): Boolean {
        return true
    }

    override fun canInteractWithFluids(barrel: BarrelTileEntity): Boolean {
        return false
    }

    override fun draw(barrel: BarrelTileEntity, x: Double, y: Double, z: Double, partialTicks: Float) {
        super.draw(barrel, x, y, z, partialTicks)

        val height = 0.875 * MathStuff.lerp(barrel.compostAmountLastTick.toDouble() / BarrelTileEntity.compostCapacity.toDouble(), barrel.compostAmount.toDouble() / BarrelTileEntity.compostCapacity.toDouble(), partialTicks.toDouble())

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
            RenderUtil.renderContents(ClientProxy.COMPOST, height, barrel.color)
        } else {
            RenderUtil.renderContents3D(ClientProxy.COMPOST, ClientProxy.COMPOST, height, barrel.color)
        }
        RenderHelper.enableStandardItemLighting()


        GlStateManager.disableRescaleNormal()
        GlStateManager.disableBlend()

        GlStateManager.popMatrix()
    }

    object CompostCollectionLogic : BarrelLogic() {
        override fun canUseItem(barrel: BarrelTileEntity, player: PlayerEntity?, hand: EnumHand?, itemStack: ItemStack): Boolean {
            return RegistryManager.getCompost(itemStack) != null
        }

        override fun onUseItem(barrel: BarrelTileEntity, player: PlayerEntity?, hand: EnumHand?, itemStack: ItemStack): EnumInteractResult {
            val compostRecipe = RegistryManager.getCompost(itemStack) ?: return EnumInteractResult.PASS

            if (!barrel.world.isRemote) {
                if (barrel.item.isEmpty) {
                    barrel.item = compostRecipe.output
                } else if (!ItemStack.areItemStacksEqual(barrel.item, compostRecipe.output)) {
                    return EnumInteractResult.PASS
                }

                barrel.color.let {
                    val weight = barrel.compostAmount.toFloat() / (barrel.compostAmount + compostRecipe.volume).toFloat()
                    barrel.color = Color.weightedAverage(compostRecipe.color, it, weight)
                }

                barrel.compostAmount += compostRecipe.volume
                barrel.compostAmount = min(barrel.compostAmount, BarrelTileEntity.compostCapacity)

                if (barrel.compostAmount == BarrelTileEntity.compostCapacity) {
                    barrel.state = BarrelStates.COMPOSTING
                }
            }
            return EnumInteractResult.CONSUME
        }
    }
}
