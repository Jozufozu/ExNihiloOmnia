package com.jozufozu.exnihiloomnia.common

import com.jozufozu.exnihiloomnia.common.entity.ThrownStoneEntity
import net.minecraft.entity.EntityType

object ModEntities {
    val THROWN_STONE: EntityType<ThrownStoneEntity> = EntityType.Builder.create<ThrownStoneEntity>(::ThrownStoneEntity).build("exnihiloomnia:thrown_stone")
}