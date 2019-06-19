package com.jozufozu.exnihiloomnia.common.items;

import com.jozufozu.exnihiloomnia.common.blocks.ExNihiloBlocks;
import com.jozufozu.exnihiloomnia.common.items.tools.ItemHammer;
import com.jozufozu.exnihiloomnia.common.items.tools.ItemMesh;
import com.jozufozu.exnihiloomnia.common.lib.ItemsLib;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;

import java.util.ArrayList;

public class ExNihiloItems
{
    public static final ArrayList<Item> modItems = new ArrayList<>();
    
    public static Item WOOD_MESH = new ItemMesh(ItemsLib.WOOD_MESH, Item.ToolMaterial.WOOD);
    public static Item SILK_MESH = new ItemMesh(ItemsLib.SILK_MESH, ExNihiloItemTier.SILK);
    public static Item DIAMOND_MESH = new ItemMesh(ItemsLib.DIAMOND_MESH, Item.ToolMaterial.DIAMOND);
    public static Item GOLD_MESH = new ItemMesh(ItemsLib.GOLD_MESH, Item.ToolMaterial.GOLD);
    
    public static Item CROOK;
    public static Item BONE_CROOK;
    
    public static Item WOOD_HAMMER = new ItemHammer(ItemsLib.WOOD_HAMMER, Item.ToolMaterial.WOOD);
    public static Item STONE_HAMMER = new ItemHammer(ItemsLib.STONE_HAMMER, Item.ToolMaterial.STONE);
    public static Item IRON_HAMMER = new ItemHammer(ItemsLib.IRON_HAMMER, Item.ToolMaterial.IRON);
    public static Item GOLD_HAMMER = new ItemHammer(ItemsLib.GOLD_HAMMER, Item.ToolMaterial.GOLD);
    public static Item DIAMOND_HAMMER = new ItemHammer(ItemsLib.DIAMOND_HAMMER, Item.ToolMaterial.DIAMOND);
    
    public static Item TREE_SEED;
    public static Item STONE = new ItemStone();
    public static Item ASTROLABE = new ItemJadeAstrolabe();
    public static Item ASH = new ItemAsh();

    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        for (Item item : modItems)
            event.getRegistry().register(item);
    
        for (Block block : ExNihiloBlocks.modBlocks)
            event.getRegistry().register(block.asItem());
    }
}