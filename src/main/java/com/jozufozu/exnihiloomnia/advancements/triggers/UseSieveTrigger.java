package com.jozufozu.exnihiloomnia.advancements.triggers;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.jozufozu.exnihiloomnia.ExNihilo;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UseSieveTrigger implements ICriterionTrigger<UseSieveTrigger.Instance>
{
    public static final ResourceLocation ID = new ResourceLocation(ExNihilo.MODID,"sieve_item");
    private final Map<PlayerAdvancements, Listeners> listeners = Maps.newHashMap();
    
    
    @Override
    public ResourceLocation getId()
    {
        return ID;
    }
    
    @Override
    public void addListener(PlayerAdvancements playerAdvancementsIn, Listener<Instance> listener)
    {
        Listeners sieveListener = this.listeners.computeIfAbsent(playerAdvancementsIn, Listeners::new);
    
        sieveListener.add(listener);
    }
    
    @Override
    public void removeListener(PlayerAdvancements playerAdvancementsIn, Listener<Instance> listener)
    {
        Listeners sieveListener = this.listeners.computeIfAbsent(playerAdvancementsIn, Listeners::new);
    
        sieveListener.remove(listener);
    }
    
    @Override
    public void removeAllListeners(PlayerAdvancements playerAdvancementsIn)
    {
        this.listeners.remove(playerAdvancementsIn);
    }
    
    @Override
    public Instance deserializeInstance(JsonObject json, JsonDeserializationContext context)
    {
        ResourceLocation recipe = null;
        
        if (json.has("recipe"))
        {
            recipe = new ResourceLocation(JsonUtils.getString(json, "recipe"));
        }
        
        ItemPredicate[] drops = ItemPredicate.deserializeArray(json);
        
        return new Instance(recipe, drops);
    }
    
    public void trigger(EntityPlayerMP player, ResourceLocation sifted, NonNullList<ItemStack> drops)
    {
        Listeners listeners = this.listeners.get(player.getAdvancements());
        
        if (listeners != null)
        {
            listeners.trigger(sifted, drops);
        }
    }
    
    public static class Instance extends AbstractCriterionInstance
    {
        private final ResourceLocation recipe;
        private final ItemPredicate[] drops;
        
        public Instance(@Nullable ResourceLocation recipeName, @Nullable ItemPredicate[] drops)
        {
            super(ID);
            this.recipe = recipeName;
            this.drops = drops;
        }
        
        public boolean test(ResourceLocation recipeName, NonNullList<ItemStack> drops)
        {
            List<ItemPredicate> list = Lists.newArrayList(this.drops != null ? this.drops : new ItemPredicate[0]);
    
            for (ItemStack drop : drops)
            {
                list.removeIf(itemPredicate -> itemPredicate.test(drop));
            }
            
            return list.isEmpty() && this.recipe == null || recipeName.equals(this.recipe);
        }
    }
    
    public static class Listeners
    {
        private final PlayerAdvancements playerAdvancements;
        private final Set<Listener<UseSieveTrigger.Instance>> listeners = Sets.newHashSet();
    
        public Listeners(PlayerAdvancements playerAdvancementsIn)
        {
            this.playerAdvancements = playerAdvancementsIn;
        }
    
        public boolean isEmpty()
        {
            return this.listeners.isEmpty();
        }
    
        public void add(ICriterionTrigger.Listener<UseSieveTrigger.Instance> listener)
        {
            this.listeners.add(listener);
        }
    
        public void remove(ICriterionTrigger.Listener<UseSieveTrigger.Instance> listener)
        {
            this.listeners.remove(listener);
        }
    
        public void trigger(ResourceLocation recipeName, NonNullList<ItemStack> drops)
        {
            List<Listener<UseSieveTrigger.Instance>> list = null;
        
            for (ICriterionTrigger.Listener<UseSieveTrigger.Instance> listener : this.listeners)
            {
                if (listener.getCriterionInstance().test(recipeName, drops))
                {
                    if (list == null)
                    {
                        list = Lists.newArrayList();
                    }
                
                    list.add(listener);
                }
            }
        
            if (list != null)
            {
                for (ICriterionTrigger.Listener<UseSieveTrigger.Instance> listener : list)
                {
                    listener.grantCriterion(this.playerAdvancements);
                }
            }
        }
    }
}
