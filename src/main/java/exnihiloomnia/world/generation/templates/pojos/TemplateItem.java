package exnihiloomnia.world.generation.templates.pojos;

public class TemplateItem {
	private String id;
	private int meta;
	private int count;
	private int slot = -1;
	
	public TemplateItem(){}
	public TemplateItem(String idIn, int countIn, int metaIn)
	{
		this(idIn, countIn, metaIn, -1);
	}
	public TemplateItem(String idIn, int countIn, int metaIn, int slotIn)
	{
		this.id = idIn;
		this.count = countIn;
		this.meta = metaIn;
		this.slot = slotIn;
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
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getSlot() {
		return slot;
	}
	public void setSlot(int slot) {
		this.slot = slot;
	}
}
