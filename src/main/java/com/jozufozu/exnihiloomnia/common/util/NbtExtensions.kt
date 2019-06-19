package com.jozufozu.exnihiloomnia.common.util

import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.INBT
import java.util.*

operator fun CompoundNBT.set(key: String, value: ItemStack) = this.put(key, value.serializeNBT())

operator fun CompoundNBT.set(key: String, value: INBT) = this.put(key, value)

operator fun CompoundNBT.set(key: String, value: Byte) = this.putByte(key, value)
operator fun CompoundNBT.set(key: String, value: Short) = this.putShort(key, value)
operator fun CompoundNBT.set(key: String, value: Int) = this.putInt(key, value)
operator fun CompoundNBT.set(key: String, value: Long) = this.putLong(key, value)
operator fun CompoundNBT.set(key: String, value: Float) = this.putFloat(key, value)
operator fun CompoundNBT.set(key: String, value: Double) = this.putDouble(key, value)
operator fun CompoundNBT.set(key: String, value: Boolean) = this.putBoolean(key, value)

operator fun CompoundNBT.set(key: String, value: String) = this.putString(key, value)

operator fun CompoundNBT.set(key: String, value: UUID) = this.putUniqueId(key, value)

operator fun CompoundNBT.set(key: String, value: IntArray) = this.putIntArray(key, value)
operator fun CompoundNBT.set(key: String, value: ByteArray) = this.putByteArray(key, value)