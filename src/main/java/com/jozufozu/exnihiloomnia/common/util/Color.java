package com.jozufozu.exnihiloomnia.common.util;

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
}
