package com.jozufozu.exnihiloomnia.common.util

import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

/**
 * Created by Jozsef on 8/1/2017.
 */
interface IModelRegister {
    @SideOnly(Side.CLIENT)
    fun registerModels()
}
