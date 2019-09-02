package com.jozufozu.exnihiloomnia.common.blocks.sieve

import com.jozufozu.exnihiloomnia.advancements.ExNihiloTriggers
import com.jozufozu.exnihiloomnia.client.ParticleSieve
import com.jozufozu.exnihiloomnia.common.items.tools.IMesh
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
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.ItemHandlerHelper
import net.minecraftforge.items.ItemStackHandler

class TileEntitySieve : TileEntity(), ITickable {
    private val itemHandler = SieveItemHandler(2)

    var requiredTime: Int = 0

    var countdown: Int = 0
    var countdownLastTick: Int = 0

    private var user: EntityPlayer? = null
    private var workTimer: Int = 0

    val contents: ItemStack
        get() = itemHandler.getStackInSlot(0)

    val mesh: ItemStack
        get() = itemHandler.getStackInSlot(1)

    fun queueWork(player: EntityPlayer, held: ItemStack) {
        this.user = player

        workTimer += 4
    }

    override fun update() {
        countdownLastTick = countdown

        if (requiredTime == 0)
            return

        if (workTimer > 0) {
            workTimer--
            countdown--

            if (world.isRemote) {
                val mimic = itemHandler.getStackInSlot(0)
                for (i in 0 until world.rand.nextInt(10) + 25) {
                    Minecraft.getMinecraft().effectRenderer.addEffect(ParticleSieve(world, mimic, pos))
                }
            }

            markDirty()
        }

        if (countdown <= 0) {
            if (!world.isRemote) {
                rollRewards()

                if (itemHandler.getStackInSlot(1).attemptDamageItem(1, world.rand, null)) {
                    this.world.playSound(null, this.pos, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.BLOCKS, 0.8f, 0.8f + this.world.rand.nextFloat() * 0.4f)

                    setStackInSlot(1, ItemStack.EMPTY)
                }

                val soundType = blockSound(user!!)
                this.world.playSound(null, this.pos, soundType.breakSound, SoundCategory.BLOCKS, 0.8f, soundType.getPitch() * 0.8f + this.world.rand.nextFloat() * 0.4f)
            }

            reset()
        }
    }

    /**
     * Takes the given item and checks to see if it can be sifted. If it can, it will decrease the stack by one and fill the sieve
     */
    fun insertContents(user: EntityPlayer?, contents: ItemStack) {
        if (itemHandler.getStackInSlot(0) != ItemStack.EMPTY)
            return

        if (RegistryManager.siftable(contents)) {
            val insert = ItemStack(contents.item, 1, contents.metadata)

            if (user == null || !user.isCreative)
                contents.shrink(1)

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
            val block = Block.getBlockFromItem(sifting.item)

            if (block !== Blocks.AIR)
                soundEvent = block.getSoundType(block.getStateFromMeta(sifting.metadata), world, pos, user).placeSound

            world.playSound(null, pos, soundEvent, SoundCategory.BLOCKS, 0.4f, 0.75f + world.rand.nextFloat() * 0.1f)
        }
    }

    fun hasContents(): Boolean {
        return itemHandler.getStackInSlot(0) != ItemStack.EMPTY
    }

    fun trySetMesh(user: EntityPlayer?, mesh: ItemStack) {
        if (mesh.item !is IMesh)
            return

        val toInsert = mesh.copy()

        user?.inventory?.deleteStack(mesh)

        setStackInSlot(1, toInsert)
    }

    fun removeMesh(user: EntityPlayer) {
        ItemHandlerHelper.giveItemToPlayer(user, itemHandler.getStackInSlot(1))

        setStackInSlot(1, ItemStack.EMPTY)
    }

    fun hasMesh(): Boolean {
        return itemHandler.getStackInSlot(1) != ItemStack.EMPTY
    }

    fun blockSound(user: EntityPlayer): SoundType {
        var soundEvent = SoundType.GROUND

        val sifting = itemHandler.getStackInSlot(0)
        val block = Block.getBlockFromItem(sifting.item)

        if (block !== Blocks.AIR)
            soundEvent = block.getSoundType(block.getStateFromMeta(sifting.metadata), world, pos, user)

        return soundEvent
    }

    fun rollRewards() {
        markDirty()

        val drops = NonNullList.create<ItemStack>()

        val contents = contents
        val mesh = mesh

        for (recipe in RegistryManager.SIFTING) {
            if (recipe.matches(contents)) {
                val roll = recipe.rewards.roll(this.user, mesh, world.rand)

                if (user is EntityPlayerMP)
                    ExNihiloTriggers.USE_SIEVE_TRIGGER.trigger((user as EntityPlayerMP?)!!, recipe.registryName!!, roll)

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

        setStackInSlot(0, ItemStack.EMPTY)
    }

    fun setStackInSlot(slot: Int, stack: ItemStack) {
        itemHandler.setStackInSlot(slot, stack)


        val blockState = this.world.getBlockState(this.getPos())
        this.world.notifyBlockUpdate(this.getPos(), blockState, blockState, 3)
        markDirty()
    }

    override fun getUpdateTag(): NBTTagCompound {
        return writeToNBT(NBTTagCompound())
    }

    override fun getUpdatePacket(): SPacketUpdateTileEntity {
        return SPacketUpdateTileEntity(getPos(), 1, this.updateTag)
    }

    override fun onDataPacket(net: NetworkManager, packet: SPacketUpdateTileEntity) {
        this.readFromNBT(packet.nbtCompound)
    }

    override fun writeToNBT(compound: NBTTagCompound): NBTTagCompound {
        compound.setTag("inventory", itemHandler.serializeNBT())
        compound.setInteger("countdown", countdown)
        compound.setInteger("requiredTime", requiredTime)
        return super.writeToNBT(compound)
    }

    override fun readFromNBT(compound: NBTTagCompound) {
        super.readFromNBT(compound)
        itemHandler.deserializeNBT(compound.getCompoundTag("inventory"))
        countdown = compound.getInteger("countdown")
        countdownLastTick = countdown
        requiredTime = compound.getInteger("requiredTime")
    }

    override fun hasCapability(capability: Capability<*>, facing: EnumFacing?): Boolean {
        return capability === CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing)
    }

    override fun <T> getCapability(capability: Capability<T>, facing: EnumFacing?): T? {
        return if (capability === CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) itemHandler as T else super.getCapability(capability, facing)
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
