package com.jozufozu.exnihiloomnia.common.util;

/**
 * Items that extend this and are used to process rewards will alter the drop rate of rewards based on their type
 */
public interface IRewardProcessor
{
    float getEffectivenessForType(String type);
}
