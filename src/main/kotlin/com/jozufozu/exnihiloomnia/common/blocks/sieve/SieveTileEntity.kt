package com.jozufozu.exnihiloomnia.common.blocks.sieve

import com.jozufozu.exnihiloomnia.advancements.ExNihiloTriggers
import com.jozufozu.exnihiloomnia.client.SieveParticle
import com.jozufozu.exnihiloomnia.common.blocks.ExNihiloTileEntities
import com.jozufozu.exnihiloomnia.common.network.CSievePacket
import com.jozufozu.exnihiloomnia.common.network.ExNihiloNetwork
import com.jozufozu.exnihiloomnia.common.registries.RegistryManager
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.SoundType
import net.minecraft.client.Minecraft
import net.minecraft.entity.item.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.SUpdateTileEntityPacket
import net.minecraft.tileentity.ITickableTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.NonNullList
import net.minecraft.util.SoundCategory
import net.minecraft.util.SoundEvents
import net.minecraft.util.math.Vec3d
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.Constants
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.fml.network.PacketDistributor
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.IItemHandlerModifiable

class SieveTileEntity : TileEntity(ExNihiloTileEntities.SIEVE), ITickableTileEntity {
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

    private var user: PlayerEntity? = null

    /**
     * Accessing packet on the server will populate the backing field if it is null,
     * letting the update loop know for sure that a packet has to be sent.
     * If packet is accessed on the client it will always return null,
     * this makes the side checking by the user unnecessary and can be simplified to just be the ? operator.
     */
    private val packet: CSievePacket?
        get() {
            if (world == null || world!!.isRemote) return null
            if (_packet == null) _packet = CSievePacket(pos)
            return _packet
        }
    // Separate backing field so the update loop can know whether or not to send a packet
    private var _packet: CSievePacket? = null

    override fun tick() {
        countdownLastTick = countdown

        if (requiredTime > 0) {
            if (workTimer > 0) {
                workTimer--
                countdown--

                if (world!!.isRemote) {
                    val mimic = contents
                    for (i in 0 until world!!.rand.nextInt(10) + 25) {
                        Minecraft.getInstance().particles.addEffect(SieveParticle(world!!, mimic, pos))
                    }
                }
            }

            if (countdown <= 0) {
                if (!world!!.isRemote) {
                    rollRewards()

                    if (user?.isCreative?.not() == true) {
                        if (mesh.attemptDamageItem(1, world!!.rand, user as? ServerPlayerEntity)) {
                            world!!.playSound(null, pos, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.BLOCKS, 0.8f, 0.8f + world!!.rand.nextFloat() * 0.4f)

                            mesh = ItemStack.EMPTY
                        }
                    }

                    val soundType = blockSound(user)
                    world!!.playSound(null, pos, soundType.breakSound, SoundCategory.BLOCKS, 0.8f, soundType.getPitch() * 0.8f + world!!.rand.nextFloat() * 0.4f)
                }

                reset()
            }
        }

        if (!world!!.isRemote) {
            _packet?.let {
                markDirty()
                ExNihiloNetwork.channel.send(PacketDistributor.TRACKING_CHUNK.with { world!!.getChunkAt(pos) }, it)
            }
            _packet = null
        }
    }

    fun queueWork(player: PlayerEntity, held: ItemStack) {
        user = player

        workTimer += 4
    }

    fun trySetMesh(player: PlayerEntity, stack: ItemStack) {
        user = player
        if (!stack.isEmpty && RegistryManager.isMesh(stack)) {
            mesh = stack.split(1)
        }
    }

    fun tryAddContents(player: PlayerEntity, stack: ItemStack) {
        user = player
        if (contents.isEmpty && RegistryManager.siftable(stack)) {
            val insert = if (player.isCreative) stack.copy().also { it.count = 1 } else stack.split(1)

            itemHandler.setStackInSlot(1, insert)

            val block = Block.getBlockFromItem(insert.item)
            val soundEvent = if (block !== Blocks.AIR)
                block.getSoundType(block.defaultState, world, pos, player).placeSound
            else
                SoundEvents.BLOCK_GRAVEL_HIT

            world!!.playSound(null, pos, soundEvent, SoundCategory.BLOCKS, 0.4f, 0.75f + world!!.rand.nextFloat() * 0.1f)
        }
    }

    fun removeMesh(): ItemStack {
        val out = mesh
        mesh = ItemStack.EMPTY
        return out
    }


    fun blockSound(user: PlayerEntity?): SoundType {
        var soundEvent = SoundType.GROUND

        val block = Block.getBlockFromItem(contents.item)

        if (block !== Blocks.AIR)
            soundEvent = block.getSoundType(block.defaultState, world, pos, user)

        return soundEvent
    }

    private fun rollRewards() {
        val drops = NonNullList.create<ItemStack>()

        val mesh = mesh

        for (recipe in RegistryManager.SIFTING) {
            if (recipe.matches(contents)) {
                val roll = recipe.rewards.roll(user, RegistryManager.getMultipliers(mesh), world!!.rand)

                (user as? ServerPlayerEntity)?.let { ExNihiloTriggers.USE_SIEVE_TRIGGER.trigger(it, recipe.registryName!!, roll) }

                drops.addAll(roll)
            }
        }

        for (drop in drops) {
            val posX = world!!.rand.nextDouble() * 0.75 + 0.125
            val posZ = world!!.rand.nextDouble() * 0.75 + 0.125

            val entityitem = ItemEntity(world!!, pos.x + posX, pos.y + 1.0, pos.z + posZ, drop)

            val motionMag = 0.08

            entityitem.motion = Vec3d(
                    0.5 * (world!!.rand.nextFloat().toDouble() * motionMag * 2.0 - motionMag),
                    world!!.rand.nextFloat().toDouble() * motionMag * 1.6,
                    0.5 * (world!!.rand.nextFloat().toDouble() * motionMag * 2.0 - motionMag)
            )

            entityitem.setDefaultPickupDelay()
            world!!.addEntity(entityitem)
        }
    }

    fun reset() {
        workTimer = 0
        countdown = 0
        countdownLastTick = 0
        requiredTime = 0

        contents = ItemStack.EMPTY
    }

    override fun getUpdateTag(): CompoundNBT {
        return write(CompoundNBT())
    }

    override fun getUpdatePacket(): SUpdateTileEntityPacket {
        return SUpdateTileEntityPacket(getPos(), 1, updateTag)
    }

    override fun onDataPacket(net: NetworkManager, packet: SUpdateTileEntityPacket) {
        read(packet.nbtCompound)
    }

    override fun write(compound: CompoundNBT): CompoundNBT {
        if (!mesh.isEmpty) compound.put("mesh", mesh.write(CompoundNBT()))
        if (!contents.isEmpty) compound.put("contents", contents.write(CompoundNBT()))
        if (countdown != 0) compound.putInt("countdown", countdown)
        if (requiredTime != 0) compound.putInt("requiredTime", requiredTime)
        return super.write(compound)
    }

    override fun read(compound: CompoundNBT) {
        super.read(compound)
        if (compound.contains("mesh", Constants.NBT.TAG_COMPOUND))
        countdown = compound.getInt("countdown")
        countdownLastTick = countdown
        requiredTime = compound.getInt("requiredTime")
    }


    override fun <T : Any?> getCapability(cap: Capability<T>): LazyOptional<T> {
        return if (cap === CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) LazyOptional.of { itemHandler }.cast() else super.getCapability(cap)
    }

    private inner class SieveItemHandler : IItemHandlerModifiable {
        override fun isItemValid(slot: Int, stack: ItemStack) = when (slot) {
            0 -> mesh.isEmpty && RegistryManager.isMesh(stack)
            1 -> contents.isEmpty && RegistryManager.siftable(stack)
            else -> false
        }

        override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
            if (stack.isEmpty) return stack
            when (slot) {
                0 -> {
                    if (mesh.isEmpty && RegistryManager.isMesh(stack)) {
                        val copy = stack.copy()

                        val insert = copy.split(1)

                        if (!simulate) mesh = insert

                        return copy
                    }

                    return ItemStack.EMPTY
                }
                1 -> {
                    if (contents.isEmpty && RegistryManager.siftable(stack)) {
                        val copy = stack.copy()

                        val insert = copy.split(1)

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
