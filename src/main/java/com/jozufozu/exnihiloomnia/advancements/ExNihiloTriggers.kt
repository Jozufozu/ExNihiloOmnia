package com.jozufozu.exnihiloomnia.advancements

import com.google.common.collect.Maps
import com.jozufozu.exnihiloomnia.ExNihilo
import com.jozufozu.exnihiloomnia.advancements.triggers.UseSieveTrigger
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.advancements.ICriterionTrigger
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.relauncher.ReflectionHelper

import java.lang.reflect.Field

object ExNihiloTriggers {
    val USE_SIEVE_TRIGGER: UseSieveTrigger = CriteriaTriggers.register(UseSieveTrigger())

    fun preInit() { }
}
