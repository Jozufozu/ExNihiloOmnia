package com.jozufozu.exnihiloomnia.common.lib;

import com.jozufozu.exnihiloomnia.ExNihilo;
import net.minecraft.util.ResourceLocation;

public class LibItems
{
    /**
     * Material prefixes
     */
    public static final String WOOD = "_wood";
    public static final String STONE = "_stone";
    public static final String IRON = "_iron";
    public static final String GOLD = "_gold";
    public static final String DIAMOND = "_diamond";
    
    /**
     * Ex Nihilo materials
     */
    public static final String SILK = "_silk";
    public static final String BONE = "_bone";
    
    /**
     * Tools
     */
    public static final String MESH = "mesh";
    public static final String CROOK = "crook";
    public static final String HAMMER = "hammer";
    
    /**
     * Mesh
     */
    public static final ResourceLocation WOODEN_MESH = get(MESH, WOOD);
    public static final ResourceLocation SILK_MESH = get(MESH, SILK);
    public static final ResourceLocation GOLD_MESH = get(MESH, GOLD);
    public static final ResourceLocation DIAMOND_MESH = get(MESH, DIAMOND);
    
    public static final ResourceLocation WOODEN_HAMMER = get(HAMMER, WOOD);
    public static final ResourceLocation STONE_HAMMER = get(HAMMER, STONE);
    public static final ResourceLocation IRON_HAMMER = get(HAMMER, IRON);
    public static final ResourceLocation GOLD_HAMMER = get(HAMMER, GOLD);
    public static final ResourceLocation DIAMOND_HAMMER = get(HAMMER, DIAMOND);
    
    public static final ResourceLocation TREE_SEED = get("tree_seed");
    public static final ResourceLocation SMALL_STONE = get("small_stone");
    
    private static ResourceLocation get(String... name)
    {
        StringBuilder builder = new StringBuilder();
        for (String s : name)
        {
            builder.append(s);
        }
        
        return new ResourceLocation(ExNihilo.MODID, builder.toString());
    }
}
