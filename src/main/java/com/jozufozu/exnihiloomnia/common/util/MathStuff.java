package com.jozufozu.exnihiloomnia.common.util;

public class MathStuff
{
    public static float lerp(float f1, float f2, float fraction)
    {
        return (1.0f - fraction) * f1 + f2 * fraction;
    }
    
    public static double lerp(double f1, double f2, double fraction)
    {
        return (1.0 - fraction) * f1 + f2 * fraction;
    }
}
