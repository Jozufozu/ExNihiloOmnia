package com.jozufozu.exnihiloomnia.common;

import com.jozufozu.exnihiloomnia.ExNihilo;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootParameter;

public class ExNihiloLootParameters
{
    public static final LootParameter<ItemStack> MESH = register("mesh");

    private static <T> LootParameter<T> register(String name) {
        return new LootParameter<>(new ResourceLocation(ExNihilo.MODID, name));
    }
}
