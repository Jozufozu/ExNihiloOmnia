package exnihiloomnia.world.generation.templates.pojos;

import java.util.ArrayList;

public class TemplateBlock {
	private String id;
	private int meta;
	private int x;
	private int y;
	private int z;
	private ArrayList<TemplateItem>contents;
	
	public TemplateBlock() {}
	
	public TemplateBlock(String idIn, int metaIn, int xIn, int yIn, int zIn) {
		this.id = idIn;
		this.meta = metaIn;
		this.x = xIn;
		this.y = yIn;
		this.z = zIn;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public int getMeta() {
		return meta;
	}
	
	public void setMeta(int meta) {
		this.meta = meta;
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getZ() {
		return z;
	}
	
	public void setZ(int z) {
		this.z = z;
	}
	
	public ArrayList<TemplateItem> getContents() {
		return contents;
	}
	
	public void setContents(ArrayList<TemplateItem> contents) {
		this.contents = contents;
	}
}
