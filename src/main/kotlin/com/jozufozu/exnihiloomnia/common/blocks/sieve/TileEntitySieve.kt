package com.jozufozu.exnihiloomnia.common.blocks.sieve

import com.jozufozu.exnihiloomnia.advancements.ExNihiloTriggers
import com.jozufozu.exnihiloomnia.client.ParticleSieve
import com.jozufozu.exnihiloomnia.common.items.tools.IMesh
import com.jozufozu.exnihiloomnia.common.network.ExNihiloNetwork
import com.jozufozu.exnihiloomnia.common.network.MessageUpdateSieve
import com.jozufozu.exnihiloomnia.common.registries.RegistryManager
import net.minecraft.block.Block
import net.minecraft.block.SoundType
import net.minecraft.client.Minecraft
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.init.Blocks
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.SPacketUpdateTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.ITickable
import net.minecraft.util.NonNullList
import net.minecraft.util.SoundCategory
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.Constants
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.IItemHandlerModifiable

class TileEntitySieve : TileEntity(), ITickable {
    private val itemHandler = SieveItemHandler()

    var mesh: ItemStack = ItemStack.EMPTY
        set(value) {
            packet?.mesh = value
            field = value
        }

    var contents: ItemStack = ItemStack.EMPTY
    var requiredTime: Int = 0

    var countdown: Int = 0
        set(value) {
            if (value != field) {
                packet?.countdown = value
                field = value
            }
        }
    var countdownLastTick: Int = 0

    var workTimer: Int = 0
        set(value) {
            if (value != field) {
                packet?.workTimer = value
                field = value
            }
        }

    val hasContents get() = !contents.isEmpty
    val hasMesh get() = !mesh.isEmpty

    private var user: EntityPlayer? = null

    /**
     * Accessing packet on the server will populate the backing field if it is null,
     * letting the update loop know for sure that a packet has to be sent.
     * If packet is accessed on the client it will always return null,
     * this makes the side checking by the user unnecessary and can be simplified to just be the ? operator.
     */
    private val packet: MessageUpdateSieve?
        get() {
            if (world == null || world.isRemote) return null
            if (_packet == null) _packet = MessageUpdateSieve(pos)
            return _packet
        }
    // Separate backing field so the update loop can know whether or not to send a packet
    private var _packet: MessageUpdateSieve? = null

    override fun update() {
        countdownLastTick = countdown

        if (requiredTime > 0) {
            if (workTimer > 0) {
                workTimer--
                countdown--

                if (world.isRemote) {
                    val mimic = contents
                    for (i in 0 until world.rand.nextInt(10) + 25) {
                        Minecraft.getMinecraft().effectRenderer.addEffect(ParticleSieve(world, mimic, pos))
                    }
                }
            }

            if (countdown <= 0) {
                if (!world.isRemote) {
                    rollRewards()

                    if (user?.isCreative?.not() == true) {
                        if (mesh.attemptDamageItem(1, world.rand, user as? EntityPlayerMP)) {
                            world.playSound(null, pos, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.BLOCKS, 0.8f, 0.8f + world.rand.nextFloat() * 0.4f)

                            mesh = ItemStack.EMPTY
                        }
                    }

                    val soundType = blockSound(user)
                    world.playSound(null, pos, soundType.breakSound, SoundCategory.BLOCKS, 0.8f, soundType.getPitch() * 0.8f + world.rand.nextFloat() * 0.4f)
                }

                reset()
            }
        }

        if (!world.isRemote) {
            _packet?.let {
                markDirty()
                ExNihiloNetwork.channel.sendToAllAround(it, NetworkRegistry.TargetPoint(world.provider.dimension, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), 64.0))
            }
            _packet = null
        }
    }

    fun queueWork(player: EntityPlayer, held: ItemStack) {
        user = player

        workTimer += 4
    }

    fun trySetMesh(player: EntityPlayer, stack: ItemStack) {
        user = player
        if (!stack.isEmpty && stack.item is IMesh) {
            mesh = stack.splitStack(1)
        }
    }

    fun tryAddContents(player: EntityPlayer, stack: ItemStack) {
        user = player
        if (contents.isEmpty && RegistryManager.siftable(stack)) {
            val insert = if (player.isCreative) stack.copy().also { it.count = 1 } else stack.splitStack(1)

            itemHandler.setStackInSlot(1, insert)

            val block = Block.getBlockFromItem(insert.item)
            val soundEvent = if (block !== Blocks.AIR)
                block.getSoundType(block.getStateFromMeta(insert.metadata), world, pos, player).placeSound
            else
                SoundEvents.BLOCK_GRAVEL_HIT

            world.playSound(null, pos, soundEvent, SoundCategory.BLOCKS, 0.4f, 0.75f + world.rand.nextFloat() * 0.1f)
        }
    }

    fun removeMesh(): ItemStack {
        val out = mesh
        mesh = ItemStack.EMPTY
        return out
    }


    fun blockSound(user: EntityPlayer?): SoundType {
        var soundEvent = SoundType.GROUND

        val block = Block.getBlockFromItem(contents.item)

        if (block !== Blocks.AIR)
            soundEvent = block.getSoundType(block.getStateFromMeta(contents.metadata), world, pos, user)

        return soundEvent
    }

    private fun rollRewards() {
        val drops = NonNullList.create<ItemStack>()

        val mesh = mesh

        for (recipe in RegistryManager.SIFTING) {
            if (recipe.matches(contents)) {
                val roll = recipe.rewards.roll(user, mesh, world.rand)

                (user as? EntityPlayerMP)?.let { ExNihiloTriggers.USE_SIEVE_TRIGGER.trigger(it, recipe.registryName!!, roll) }

                drops.addAll(roll)
            }
        }

        for (drop in drops) {
            val posX = world.rand.nextDouble() * 0.75 + 0.125
            val posZ = world.rand.nextDouble() * 0.75 + 0.125

            val entityitem = EntityItem(world, pos.x + posX, pos.y + 1.0, pos.z + posZ, drop)

            val motionMag = 0.08

            entityitem.motionX = 0.5 * (world.rand.nextFloat().toDouble() * motionMag * 2.0 - motionMag)
            entityitem.motionY = world.rand.nextFloat().toDouble() * motionMag * 1.6
            entityitem.motionZ = 0.5 * (world.rand.nextFloat().toDouble() * motionMag * 2.0 - motionMag)

            entityitem.setDefaultPickupDelay()
            world.spawnEntity(entityitem)
        }
    }

    fun reset() {
        workTimer = 0
        countdown = 0
        countdownLastTick = 0
        requiredTime = 0

        contents = ItemStack.EMPTY
    }

    override fun getUpdateTag(): NBTTagCompound {
        return writeToNBT(NBTTagCompound())
    }

    override fun getUpdatePacket(): SPacketUpdateTileEntity {
        return SPacketUpdateTileEntity(getPos(), 1, updateTag)
    }

    override fun onDataPacket(net: NetworkManager, packet: SPacketUpdateTileEntity) {
        readFromNBT(packet.nbtCompound)
    }

    override fun writeToNBT(compound: NBTTagCompound): NBTTagCompound {
        if (!mesh.isEmpty) compound.setTag("mesh", mesh.writeToNBT(NBTTagCompound()))
        if (!contents.isEmpty) compound.setTag("contents", contents.writeToNBT(NBTTagCompound()))
        if (countdown != 0) compound.setInteger("countdown", countdown)
        if (requiredTime != 0) compound.setInteger("requiredTime", requiredTime)
        return super.writeToNBT(compound)
    }

    override fun readFromNBT(compound: NBTTagCompound) {
        super.readFromNBT(compound)
        if (compound.hasKey("mesh", Constants.NBT.TAG_COMPOUND))
        countdown = compound.getInteger("countdown")
        countdownLastTick = countdown
        requiredTime = compound.getInteger("requiredTime")
    }

    override fun hasCapability(capability: Capability<*>, facing: EnumFacing?): Boolean {
        return capability === CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing)
    }

    override fun <T> getCapability(capability: Capability<T>, facing: EnumFacing?): T? {
        return if (capability === CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(itemHandler) else super.getCapability(capability, facing)
    }

    private inner class SieveItemHandler : IItemHandlerModifiable {
        override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
            if (stack.isEmpty) return stack
            when (slot) {
                0 -> {
                    if (mesh.isEmpty && stack.item is IMesh) {
                        val copy = stack.copy()

                        val insert = copy.splitStack(1)

                        if (!simulate) mesh = insert

                        return copy
                    }

                    return ItemStack.EMPTY
                }
                1 -> {
                    if (contents.isEmpty && RegistryManager.siftable(stack)) {
                        val copy = stack.copy()

                        val insert = copy.splitStack(1)

                        if (!simulate) setStackInSlot(1, insert)

                        return copy
                    }

                    return ItemStack.EMPTY
                }
                else -> return ItemStack.EMPTY
            }
        }

        override fun getStackInSlot(slot: Int): ItemStack = when (slot) {
            0 -> mesh
            1 -> contents
            else -> ItemStack.EMPTY
        }

        override fun getSlotLimit(slot: Int) = 1

        override fun setStackInSlot(slot: Int, stack: ItemStack) {
            when (slot) {
                0 -> mesh = stack
                1 -> {
                    if (!stack.isEmpty) {
                        workTimer = 0

                        requiredTime = 0

                        for (recipe in RegistryManager.SIFTING) {
                            if (recipe.matches(stack))
                                requiredTime = requiredTime.coerceAtLeast(recipe.siftTime)
                        }

                        countdownLastTick = 0
                        countdown = requiredTime

                        contents = stack
                        packet?.sendContents(stack, requiredTime)
                    } else {
                        reset()
                        packet?.sendContents()
                    }
                }
            }
        }

        override fun getSlots() = 2

        override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack = ItemStack.EMPTY
    }
}
