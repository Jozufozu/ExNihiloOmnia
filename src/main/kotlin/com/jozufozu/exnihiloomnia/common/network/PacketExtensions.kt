package com.jozufozu.exnihiloomnia.common.network

import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.PacketBuffer

fun PacketBuffer.writeStackWithoutSize(stack: ItemStack) {
    if (stack.isEmpty) {
        this.writeVarInt(-1)
    } else {
        this.writeVarInt(Item.getIdFromItem(stack.item))
        this.writeShort(stack.metadata)
        var nbttagcompound: NBTTagCompound? = null

        if (stack.item.isDamageable || stack.item.shareTag) {
            nbttagcompound = stack.item.getNBTShareTag(stack)
        }

        this.writeCompoundTag(nbttagcompound)
    }
}

fun PacketBuffer.readStackWithoutSize(): ItemStack {
    val i = this.readVarInt()

    return if (i < 0) {
        ItemStack.EMPTY
    } else {
        val k = this.readShort().toInt()
        val itemstack = ItemStack(Item.getItemById(i), 1, k)
        itemstack.item.readNBTShareTag(itemstack, this.readCompoundTag())
        itemstack
    }
}