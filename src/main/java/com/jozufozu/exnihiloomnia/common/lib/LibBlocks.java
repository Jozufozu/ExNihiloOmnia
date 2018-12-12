package com.jozufozu.exnihiloomnia.common.lib;

import com.jozufozu.exnihiloomnia.ExNihilo;
import net.minecraft.util.ResourceLocation;

public class LibBlocks
{
    //Modifiers
    private static final String SUFFIX_WOOD = "_wood";
    private static final String SUFFIX_STONE = "_stone";
    private static final String SUFFIX_GLASS = "_glass";
    private static final String SUFFIX_CONCRETE = "_concrete";
    private static final String SUFFIX_TERRACOTTA = "_terracotta";
    private static final String STAINED = "_stained";
    
    //Blocks
    private static final String BARREL = "barrel";
    
    public static final ResourceLocation SIEVE = get("sieve");
    public static final ResourceLocation WOODEN_BARREL = get(BARREL, SUFFIX_WOOD);
    public static final ResourceLocation STONE_BARREL = get(BARREL, SUFFIX_STONE);
    public static final ResourceLocation GLASS_BARREL = get(BARREL, SUFFIX_GLASS);
    public static final ResourceLocation STAINED_GLASS_BARREL = get(BARREL, STAINED, SUFFIX_GLASS);
    public static final ResourceLocation TERRACOTTA_BARREL = get(BARREL, SUFFIX_TERRACOTTA);
    public static final ResourceLocation CONCRETE_BARREL = get(BARREL, SUFFIX_CONCRETE);
    
    public static final ResourceLocation CRUCIBLE = get("crucible");
    public static final ResourceLocation RAW_CRUCIBLE = get("raw_crucible");
    
    public static final ResourceLocation DUST = get("dust");
    public static final ResourceLocation NETHER_GRAVEL = get("nether_gravel");
    public static final ResourceLocation END_GRAVEL = get("end_gravel");
    
    public static final ResourceLocation KINDLING = get("kindling");
    
    public static final ResourceLocation INFESTED_LEAVES = get("infested_leaves");
    
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
