package com.jozufozu.exnihiloomnia.common.items;

import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ExNihiloMaterials
{
    public static ToolMaterial SILK;
    public static ToolMaterial SKELETAL;
    
    @GameRegistry.ItemStackHolder("minecraft:string")
    public static ItemStack STRING;
    
    @GameRegistry.ItemStackHolder("minecraft:bone")
    public static ItemStack BONE;
    
    public static void preInit()
    {
        SILK = EnumHelper.addToolMaterial("SILK", 0, 128, 10.0f, 0, 18);
        SKELETAL = EnumHelper.addToolMaterial("BONE", 1, 200, 8.0f, 2, 25);
    }
    
    public static void init()
    {
        SILK.setRepairItem(STRING);
        SKELETAL.setRepairItem(BONE);
    }
}
