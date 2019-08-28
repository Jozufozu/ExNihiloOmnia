package com.jozufozu.exnihiloomnia.common.items.tools

import com.jozufozu.exnihiloomnia.ExNihilo
import com.jozufozu.exnihiloomnia.common.items.ItemBaseTool
import com.jozufozu.exnihiloomnia.common.registries.RegistryManager
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraftforge.event.world.BlockEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.Random

@Mod.EventBusSubscriber(modid = ExNihilo.MODID)
class ItemHammer(registryName: ResourceLocation, toolMaterial: ToolMaterial) : ItemBaseTool(registryName, toolMaterial) {

    override fun hitEntity(stack: ItemStack, target: EntityLivingBase, attacker: EntityLivingBase): Boolean {
        //Hammers have a built-in knockback effect
        val lookVec = attacker.lookVec
        target.knockBack(attacker, 1f, -lookVec.x, -lookVec.z)
        return super.hitEntity(stack, target, attacker)
    }

    override fun canHarvestBlock(blockIn: IBlockState): Boolean {
        val block = blockIn.block

        if (block === Blocks.OBSIDIAN) {
            return this.toolMaterial.harvestLevel == 3
        } else if (block !== Blocks.DIAMOND_BLOCK && block !== Blocks.DIAMOND_ORE) {
            if (block !== Blocks.EMERALD_ORE && block !== Blocks.EMERALD_BLOCK) {
                if (block !== Blocks.GOLD_BLOCK && block !== Blocks.GOLD_ORE) {
                    if (block !== Blocks.IRON_BLOCK && block !== Blocks.IRON_ORE) {
                        return if (block !== Blocks.LAPIS_BLOCK && block !== Blocks.LAPIS_ORE) {
                            if (block !== Blocks.REDSTONE_ORE && block !== Blocks.LIT_REDSTONE_ORE) {
                                val material = blockIn.material

                                when {
                                    material === Material.ROCK -> true
                                    material === Material.IRON -> true
                                    else -> material === Material.ANVIL
                                }
                            } else {
                                this.toolMaterial.harvestLevel >= 2
                            }
                        } else {
                            this.toolMaterial.harvestLevel >= 1
                        }
                    } else {
                        return this.toolMaterial.harvestLevel >= 1
                    }
                } else {
                    return this.toolMaterial.harvestLevel >= 2
                }
            } else {
                return this.toolMaterial.harvestLevel >= 2
            }
        } else {
            return this.toolMaterial.harvestLevel >= 2
        }
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

    companion object {

        @SubscribeEvent
        fun onBreak(event: BlockEvent.HarvestDropsEvent) {
            val player = event.harvester

            if (player == null || player.activeItemStack.item !is ItemHammer) {
                return
            }

            val world = player.entityWorld

            if (world.isRemote || player.isCreative)
                return

            val pos = event.pos
            val blockState = world.getBlockState(pos)

            if (!RegistryManager.hammerable(blockState)) {
                return
            }

            event.drops.clear()
            val rand = Random()

            for (drop in RegistryManager.getHammerRewards(world, player.activeItemStack, player, blockState)) {
                var blockBox = blockState.getBoundingBox(world, pos)

                blockBox = blockBox.offset(pos).shrink(0.125).offset(0.0, -0.125, 0.0)

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
