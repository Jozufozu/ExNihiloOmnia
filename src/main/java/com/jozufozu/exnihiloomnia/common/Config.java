package com.jozufozu.exnihiloomnia.common;

import net.minecraftforge.common.config.ConfigCategory;

import static com.jozufozu.exnihiloomnia.ExNihilo.CONFIG;

public class Config
{
    public static final String CATEGORY_SIEVE = "Sieve";
    
    public static void load()
    {
        CONFIG.load();
    }
    
    public static void loadSieve()
    {
        ConfigCategory sieve = CONFIG.getCategory(CATEGORY_SIEVE);
    }
}
