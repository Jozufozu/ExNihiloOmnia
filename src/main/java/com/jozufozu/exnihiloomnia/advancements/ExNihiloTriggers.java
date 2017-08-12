package com.jozufozu.exnihiloomnia.advancements;

import com.google.common.collect.Maps;
import com.jozufozu.exnihiloomnia.ExNihilo;
import com.jozufozu.exnihiloomnia.advancements.triggers.UseSieveTrigger;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;
import java.util.Map;

public class ExNihiloTriggers
{
    public static UseSieveTrigger USE_SIEVE_TRIGGER;
    
    public static void preInit()
    {
        USE_SIEVE_TRIGGER = register(new UseSieveTrigger());
    }
    
    public static <T extends ICriterionTrigger> T register(T criterion)
    {
        Field registry = ReflectionHelper.findField(CriteriaTriggers.class, "REGISTRY", "field_192139_s");
        
        try
        {
            if (registry.getGenericType() instanceof Map)
            {
                Map hopefully = (Map) registry.get(Maps.newHashMap());
                
                
                
                hopefully.put(criterion.getId(), criterion);
            }
        }
        catch (IllegalAccessException e)
        {
            ExNihilo.log.error("Could not register advancement '%s'", criterion.getId());
        }
        
        return criterion;
    }
}
