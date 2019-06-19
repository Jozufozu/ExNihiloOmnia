package com.jozufozu.exnihiloomnia.common.blocks.sieve

import com.jozufozu.exnihiloomnia.client.SieveParticle
import com.jozufozu.exnihiloomnia.common.registries.RegistryManager
import com.jozufozu.exnihiloomnia.common.util.IMesh
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.SoundType
import net.minecraft.client.Minecraft
import net.minecraft.entity.EntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.SUpdateTileEntityPacket
import net.minecraft.tileentity.ITickableTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.Direction
import net.minecraft.util.SoundCategory
import net.minecraft.util.SoundEvents
import net.minecraft.world.ServerWorld
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.ItemHandlerHelper
import net.minecraftforge.items.ItemStackHandler

class SieveTileEntity() : TileEntity(tileEntityType), ITickableTileEntity {
    private val itemHandler = SieveItemHandler(2)
    private val holder = LazyOptional.of<IItemHandler> { itemHandler }

    var requiredTime: Int = 0

    var countdown: Int = 0
    var countdownLastTick: Int = 0

    private var user: PlayerEntity? = null
    private var workTimer: Int = 0

    val contents: ItemStack
        get() = itemHandler.getStackInSlot(0)

    val mesh: ItemStack
        get() = itemHandler.getStackInSlot(1)

    fun queueWork(player: PlayerEntity, held: ItemStack) {
        this.user = player

        workTimer += 4
    }

    override fun tick() {
        countdownLastTick = countdown

        if (requiredTime == 0)
            return

        if (workTimer > 0) {
            workTimer--
            countdown--

            if (world!!.isRemote) {
                val mimic = itemHandler.getStackInSlot(0)
                for (i in 0 until world!!.rand.nextInt(10) + 25) {
                    Minecraft.getInstance().particles.addEffect(SieveParticle(world!!, mimic, pos))
                }
            }

            markDirty()
        }

        if (countdown <= 0) {
            if (!world!!.isRemote) {
                rollRewards()

                if (itemHandler.getStackInSlot(1).attemptDamageItem(1, world!!.rand, null)) {
                    this.world!!.playSound(null, this.pos, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.BLOCKS, 0.8f, 0.8f + this.world!!.rand.nextFloat() * 0.4f)

                    setStackInSlot(1, ItemStack.EMPTY)
                }

                val soundType = blockSound(user)
                this.world!!.playSound(null, this.pos, soundType.breakSound, SoundCategory.BLOCKS, 0.8f, soundType.getPitch() * 0.8f + this.world!!.rand.nextFloat() * 0.4f)
            }

            reset()
        }
    }

    /**
     * Takes the given item and checks to see if it can be sifted. If it can, it will decrease the stack by one and fill the sieve
     */
    fun insertContents(user: PlayerEntity?, contents: ItemStack) {
        if (itemHandler.getStackInSlot(0) != ItemStack.EMPTY)
            return

        if (RegistryManager.siftable(contents)) {
            val insert = contents.split(1)

            if (user != null && user.isCreative)
                contents.grow(1)

            workTimer = 0

            requiredTime = 0

            for (recipe in RegistryManager.SIFTING) {
                if (recipe.matches(insert))
                    requiredTime = Math.max(requiredTime, recipe.siftTime)
            }

            countdownLastTick = 0
            countdown = requiredTime

            setStackInSlot(0, insert)

            var soundEvent = SoundEvents.BLOCK_GRAVEL_HIT

            val sifting = itemHandler.getStackInSlot(0)
            val block = Block.getBlockFromItem(sifting.item).defaultState

            if (block !== Blocks.AIR.defaultState)
                soundEvent = block.getSoundType(world, pos, user).placeSound

            world!!.playSound(null, pos, soundEvent, SoundCategory.BLOCKS, 0.4f, 0.75f + world!!.rand.nextFloat() * 0.1f)
        }
    }

    fun hasContents(): Boolean {
        return itemHandler.getStackInSlot(0) != ItemStack.EMPTY
    }

    fun trySetMesh(user: PlayerEntity?, mesh: ItemStack) {
        if (mesh.item !is IMesh)
            return

        val toInsert = mesh.copy()

        user?.inventory?.deleteStack(mesh)

        setStackInSlot(1, toInsert)
    }

    fun removeMesh(user: PlayerEntity) {
        ItemHandlerHelper.giveItemToPlayer(user, itemHandler.getStackInSlot(1))

        setStackInSlot(1, ItemStack.EMPTY)
    }

    fun hasMesh(): Boolean {
        return itemHandler.getStackInSlot(1) != ItemStack.EMPTY
    }

    fun blockSound(user: PlayerEntity?): SoundType {
        var soundEvent = SoundType.GROUND

        val sifting = itemHandler.getStackInSlot(0)
        val block = Block.getBlockFromItem(sifting.item).defaultState

        if (block !== Blocks.AIR)
            soundEvent = block.getSoundType(world, pos, user)

        return soundEvent
    }

    fun rollRewards() {
        markDirty()

        val serverWorld = world as? ServerWorld ?: return
        val user = user ?: return

        RegistryManager.getSieveRewards(serverWorld, mesh, user, contents).forEach {
            val posX = serverWorld.rand.nextDouble() * 0.75 + 0.125
            val posZ = serverWorld.rand.nextDouble() * 0.75 + 0.125

            with(EntityType.ITEM.create(serverWorld) ?: return) {
                item = it
                setPosition(pos.x + posX, pos.y + 1.0, pos.z + posZ)

                val motionMag = 0.08

                setMotion(0.5 * (serverWorld.rand.nextFloat().toDouble() * motionMag * 2.0 - motionMag),
                        serverWorld.rand.nextFloat().toDouble() * motionMag * 1.6,
                        0.5 * (serverWorld.rand.nextFloat().toDouble() * motionMag * 2.0 - motionMag))

                setDefaultPickupDelay()
            }
        }
    }

    fun reset() {
        workTimer = 0
        countdown = 0
        countdownLastTick = 0
        requiredTime = 0

        setStackInSlot(0, ItemStack.EMPTY)
    }

    fun setStackInSlot(slot: Int, stack: ItemStack) {
        itemHandler.setStackInSlot(slot, stack)

        val world = world ?: return

        val blockState = world.getBlockState(pos)
        world.notifyBlockUpdate(pos, blockState, blockState, 3)
        markDirty()
    }

    override fun getUpdateTag(): CompoundNBT {
        return write(CompoundNBT())
    }

    override fun getUpdatePacket(): SUpdateTileEntityPacket? {
        return SUpdateTileEntityPacket(getPos(), 1, this.updateTag)
    }

    override fun onDataPacket(net: NetworkManager?, packet: SUpdateTileEntityPacket) {
        this.read(packet.nbtCompound)
    }

    override fun write(compound: CompoundNBT): CompoundNBT {
        compound.put("inventory", itemHandler.serializeNBT())
        compound.putInt("countdown", countdown)
        compound.putInt("requiredTime", requiredTime)
        return super.write(compound)
    }

    override fun read(compound: CompoundNBT) {
        super.read(compound)
        itemHandler.deserializeNBT(compound.getCompound("inventory"))
        countdown = compound.getInt("countdown")
        countdownLastTick = countdown
        requiredTime = compound.getInt("requiredTime")
    }

    override fun <T> getCapability(capability: Capability<T>, side: Direction?): LazyOptional<T> {
        return if (capability === CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) holder.cast() else super.getCapability(capability, side)
    }

    private inner class SieveItemHandler(size: Int) : ItemStackHandler(size) {

        override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
            if (slot == 1 && stack.item !is IMesh)
                return stack

            return if (slot == 0 && !RegistryManager.siftable(stack)) stack else super.insertItem(slot, stack, simulate)

        }

        override fun getStackLimit(slot: Int, stack: ItemStack): Int {
            return if (slot == 0) 1 else super.getStackLimit(slot, stack)
        }
    }
}
