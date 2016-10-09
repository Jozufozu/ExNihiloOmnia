package exnihiloomnia.util;

import exnihiloomnia.ENO;

public class Color {
	public float r;
	public float g;
	public float b;
	public float a;
	
	public static Color WHITE = new Color(1.0f, 1.0f, 1.0f, 1.0f);

	public Color(int red, int green, int blue) {
		this.r = red / 255f;
		this.g = green / 255f;
		this.b = blue / 255f;
	}

	public Color(float red, float green, float blue, float alpha) {
		this.r = red;
		this.g = green;
		this.b = blue;
		this.a = alpha;
	}
	
	public Color (int color) {
		this(color, true);
	}
	
	public Color (String hex) {
		this(parseString(hex), false);
	}
	
	public Color (int color, boolean hasAlpha) {
		if (!hasAlpha) {
			this.a = 1.0f;
			this.r = (float) (color >> 16 & 255) / 255.0F;
			this.g = (float) (color >> 8 & 255) / 255.0F;
			this.b = (float) (color & 255) / 255.0F;
		}
		else {
			this.a = (float) (color >> 24 & 255) / 255.0F;
			this.r = (float) (color >> 16 & 255) / 255.0F;
			this.g = (float) (color >> 8 & 255) / 255.0F;
			this.b = (float) (color & 255) / 255.0F;
		}
	}

	private static int parseString(String hex) {
		int val = 0;

		try {
			val = Integer.parseInt(hex, 16);
		}
		catch (Exception e) {
			ENO.log.error("Unable to parse color: " + hex);
		}

		return val;
	}

	public int toInt() {
		int color = 0;
		color |= (int)(this.a * 255) << 24;
		color |= (int)(this.r * 255) << 16;
		color |= (int)(this.g * 255) << 8;
		color |= (int)(this.b * 255);
		return color;
	}
	
	public static Color average(Color colorA, Color colorB, float percentage) {
		float avgR = colorA.r + (colorB.r - colorA.r) * percentage;
		float avgG = colorA.g + (colorB.g - colorA.g) * percentage;
		float avgB = colorA.b + (colorB.b - colorA.b) * percentage;
		float avgA = colorA.a + (colorB.a - colorA.a) * percentage;
		
		return new Color(avgR, avgG, avgB, avgA);
	}
}
