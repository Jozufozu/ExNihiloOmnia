package com.jozufozu.exnihiloomnia.common.network

import com.jozufozu.exnihiloomnia.ExNihilo
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.network.NetworkRegistry

object ExNihiloNetwork {
    val channel = NetworkRegistry.newSimpleChannel(ResourceLocation(ExNihilo.MODID, ExNihilo.MODID), { "1" }, { true }, { true })

    private var ID = 0
    private val nextID: Int get() = ID++

    fun init() {
        channel.registerMessage(nextID, CBarrelPacket::class.java, CBarrelPacket::encode, ::CBarrelPacket, CBarrelPacket::handle)
        channel.registerMessage(nextID, CCruciblePacket::class.java, CCruciblePacket::encode, ::CCruciblePacket, CCruciblePacket::handle)
        channel.registerMessage(nextID, CSievePacket::class.java, CSievePacket::encode, ::CSievePacket, CSievePacket::handle)
    }
}