package exnihiloomnia.util;

import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraftforge.client.model.Attributes;

public class Vertex {
	private float x;
	private float y;
	private float z;
	private Color color;
	private float u;
	private float v;
	
	public Vertex(float x, float y, float z, Color color, float u, float v)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.color = color;
		this.u = u;
		this.v = v;
	}
	
	public int[] toIntArray()
	{
		return new int[] {
				Float.floatToRawIntBits(x),
				Float.floatToRawIntBits(y),
				Float.floatToRawIntBits(z),
				color.toInt(),
				Float.floatToRawIntBits(u),
				Float.floatToRawIntBits(v),
				0
		};
	}
	
	public static VertexFormat getFormat() {
		return Attributes.DEFAULT_BAKED_FORMAT;
	}
}
