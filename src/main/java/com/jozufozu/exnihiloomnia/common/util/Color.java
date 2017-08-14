package com.jozufozu.exnihiloomnia.common.util;

import net.minecraft.nbt.NBTTagInt;

public class Color
{
    public static final Color WHITE = new Color(0xFFFFFF);
    
    public final float r;
    public final float g;
    public final float b;
    public final float a;
    
    public Color(int packed, boolean hasAlpha)
    {
        this.r = (packed >> 16 & 255) / 255.0f;
        this.g = (packed >> 8 & 255) / 255.0f;
        this.b = (packed & 255) / 255.0f;
        this.a = hasAlpha ? (packed >> 24 & 255) / 255.0f : 1.0f;
    }
    
    public Color(int packed)
    {
        this.r = (packed >> 16 & 255) / 255.0f;
        this.g = (packed >> 8 & 255) / 255.0f;
        this.b = (packed & 255) / 255.0f;
        this.a = 1.0f;
    }
    
    public Color(float r, float g, float b)
    {
        this(r, g, b, 1.0f);
    }
    
    public Color(float r, float g, float b, float a)
    {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }
    
    public int toInt()
    {
        int i = 0;
        
        i |= ((int) (this.r * 255) << 16);
        i |= ((int) (this.g * 255) << 8);
        i |= ((int) (this.b * 255));
        i |= ((int) (this.a * 255) << 24);
        
        return i;
    }
    
    public Color withAlpha(float alpha)
    {
        return new Color(r, g, b, a * alpha);
    }
    
    public Color average(Color other)
    {
        return average(this, other);
    }
    
    public NBTTagInt serializeNBT()
    {
        return new NBTTagInt(toInt());
    }
    
    public static Color deserialize(NBTTagInt nbt)
    {
        return new Color(nbt.getInt(), true);
    }
    
    public static Color average(Color c1, Color c2)
    {
        return weightedAverage(c1, c2, 0.5f);
    }
    
    /**
     * Finds the weighted average of two colors
     * @param percentage between 0 and 1. 0 mean it will return c1, 0.5 indicates a normal average, 1 mean it will return c2
     */
    public static Color weightedAverage(Color from, Color to, float percentage)
    {
        float r = MathStuff.lerp(from.r, to.r, percentage);
        float g = MathStuff.lerp(from.g, to.g, percentage);
        float b = MathStuff.lerp(from.b, to.b, percentage);
        float a = MathStuff.lerp(from.a, to.a, percentage);
    
        return new Color(r, g, b, a);
    }
}
