package com.jozufozu.exnihiloomnia.common.items;

import com.jozufozu.exnihiloomnia.common.blocks.ExNihiloBlocks;
import com.jozufozu.exnihiloomnia.common.items.tools.ItemCrook;
import com.jozufozu.exnihiloomnia.common.items.tools.ItemHammer;
import com.jozufozu.exnihiloomnia.common.items.tools.ItemMesh;
import com.jozufozu.exnihiloomnia.common.lib.LibItems;
import com.jozufozu.exnihiloomnia.common.util.IItemBlockHolder;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

public class ExNihiloItems
{
    private static boolean registeredItems = false;
    
    public static final ArrayList<Item> modItems = new ArrayList<>();
    
    public static Item WOOD_MESH;
    public static Item SILK_MESH;
    public static Item DIAMOND_MESH;
    public static Item GOLD_MESH;
    
    public static Item CROOK;
    public static Item BONE_CROOK;
    
    public static Item WOOD_HAMMER;
    public static Item STONE_HAMMER;
    public static Item IRON_HAMMER;
    public static Item GOLD_HAMMER;
    public static Item DIAMOND_HAMMER;
    
    public static Item TREE_SEED;
    public static Item STONE;
    public static Item ASTROLABE;
    public static Item ASH;
    
    public static boolean hasRegisteredItems()
    {
        return registeredItems;
    }
    
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        WOOD_MESH = new ItemMesh(LibItems.WOODEN_MESH, Item.ToolMaterial.WOOD);
        SILK_MESH = new ItemMesh(LibItems.SILK_MESH, ExNihiloMaterials.SILK);
        DIAMOND_MESH = new ItemMesh(LibItems.DIAMOND_MESH, Item.ToolMaterial.DIAMOND);
        GOLD_MESH = new ItemMesh(LibItems.GOLD_MESH, Item.ToolMaterial.GOLD);
        
        CROOK = new ItemCrook(LibItems.CROOK, Item.ToolMaterial.WOOD);
        BONE_CROOK = new ItemCrook(LibItems.BONE_CROOK, ExNihiloMaterials.SKELETAL);
        
        WOOD_HAMMER = new ItemHammer(LibItems.WOODEN_HAMMER, Item.ToolMaterial.WOOD);
        STONE_HAMMER = new ItemHammer(LibItems.STONE_HAMMER, Item.ToolMaterial.STONE);
        IRON_HAMMER = new ItemHammer(LibItems.IRON_HAMMER, Item.ToolMaterial.IRON);
        GOLD_HAMMER = new ItemHammer(LibItems.GOLD_HAMMER, Item.ToolMaterial.GOLD);
        DIAMOND_HAMMER = new ItemHammer(LibItems.DIAMOND_HAMMER, Item.ToolMaterial.DIAMOND);
        
        TREE_SEED = new ItemTreeSeed();
        
        STONE = new ItemStone();
        
        ASTROLABE = new ItemJadeAstrolabe();
        ASH = new ItemAsh();
        
        for (Item item : modItems)
            event.getRegistry().register(item);
    
        for (Block block : ExNihiloBlocks.modBlocks)
            if (block instanceof IItemBlockHolder)
                event.getRegistry().register(((IItemBlockHolder) block).getItemBlock());
        
        registeredItems = true;
    }
}