package com.jozufozu.exnihiloomnia.common.items.tools

import com.jozufozu.exnihiloomnia.ExNihilo
import com.jozufozu.exnihiloomnia.common.items.ItemBaseTool
import com.jozufozu.exnihiloomnia.common.registries.RegistryManager
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.event.world.BlockEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.*

class ItemHammer(registryName: ResourceLocation, toolMaterial: ToolMaterial) : ItemBaseTool(registryName, toolMaterial) {

    override fun hitEntity(stack: ItemStack, target: EntityLivingBase, attacker: EntityLivingBase): Boolean {
        //Hammers have a built-in knockback effect
        val lookVec = attacker.lookVec
        target.knockBack(attacker, 1f, -lookVec.x, -lookVec.z)
        return super.hitEntity(stack, target, attacker)
    }

    override fun canHarvestBlock(state: IBlockState): Boolean {
        return toolMaterial.harvestLevel >= state.block.getHarvestLevel(state) && RegistryManager.hammerable(state)
    }

    override fun getHarvestLevel(stack: ItemStack, toolClass: String, player: EntityPlayer?, blockState: IBlockState?): Int {
        return if (RegistryManager.hammerable(blockState!!)) this.toolMaterial.harvestLevel else -1
    }

    override fun getStrVsBlock(stack: ItemStack, state: IBlockState): Float {
        return if (RegistryManager.hammerable(state)) this.efficiencyOnProperMaterial else super.getStrVsBlock(stack, state)
    }

    override fun canDisableShield(stack: ItemStack, shield: ItemStack, entity: EntityLivingBase, attacker: EntityLivingBase): Boolean {
        return true
    }

    @Mod.EventBusSubscriber(modid = ExNihilo.MODID)
    companion object {

        @SubscribeEvent
        @JvmStatic fun onBreak(event: BlockEvent.HarvestDropsEvent) {
            val player = event.harvester

            if (player == null || player.heldItemMainhand.item !is ItemHammer) {
                return
            }

            val world = player.entityWorld

            if (world.isRemote || player.isCreative)
                return

            val blockState = event.state

            if (!RegistryManager.hammerable(blockState)) {
                return
            }

            event.drops.clear()
            val pos = event.pos
            val rand = Random()

            val blockBox = blockState.getBoundingBox(world, pos).offset(pos).shrink(0.125).offset(0.0, -0.125, 0.0)

            for (drop in RegistryManager.getHammerRewards(world, player.activeItemStack, player, blockState)) {

                val xOff = rand.nextDouble() * (blockBox.maxX - blockBox.minX)
                val yOff = rand.nextDouble() * (blockBox.maxY - blockBox.minY)
                val zOff = rand.nextDouble() * (blockBox.maxZ - blockBox.minZ)

                val entityitem = EntityItem(player.world, blockBox.minX + xOff, blockBox.minY + yOff, blockBox.minZ + zOff, drop)

                entityitem.setDefaultPickupDelay()
                player.world.spawnEntity(entityitem)
            }
        }
    }
}
