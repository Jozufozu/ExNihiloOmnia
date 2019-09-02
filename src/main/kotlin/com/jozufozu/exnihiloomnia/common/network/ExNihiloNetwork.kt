package com.jozufozu.exnihiloomnia.common.network

import com.jozufozu.exnihiloomnia.ExNihilo
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.relauncher.Side


object ExNihiloNetwork {
    val channel = NetworkRegistry.INSTANCE.newSimpleChannel(ExNihilo.MODID)
    private var nextId: Int = 0

    fun init() {
        registerMessage(MessageUpdateBarrel::class.java, MessageUpdateBarrel.Handler::class.java, Side.CLIENT)
    }

    private fun <REQ : IMessage, REPLY : IMessage> registerMessage(requestMessageType: Class<REQ>, messageHandler: Class<out IMessageHandler<REQ, REPLY>>, side: Side) {
        channel.registerMessage(messageHandler, requestMessageType, nextId++, side)
    }
}