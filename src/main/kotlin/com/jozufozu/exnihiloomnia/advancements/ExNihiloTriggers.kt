package com.jozufozu.exnihiloomnia.advancements

import com.jozufozu.exnihiloomnia.advancements.triggers.UseSieveTrigger
import net.minecraft.advancements.CriteriaTriggers

object ExNihiloTriggers {
    val USE_SIEVE_TRIGGER: UseSieveTrigger = CriteriaTriggers.register(UseSieveTrigger())

    fun preInit() { }
}
