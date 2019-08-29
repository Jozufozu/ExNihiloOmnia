package com.jozufozu.exnihiloomnia.common.util

/**
 * Items that extend this and are used to process rewards will alter the drop rate of rewards based on their type
 */
interface IRewardProcessor {
    fun getEffectivenessForType(type: String): Float
}
