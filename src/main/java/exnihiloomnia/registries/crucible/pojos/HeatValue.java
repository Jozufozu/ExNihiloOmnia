package exnihiloomnia.registries.crucible.pojos;

public class HeatValue {

	private String block;
	private int meta;
	private int heat;

	public HeatValue(String block, int meta, int heat)
	{
		this.block = block;
		this.meta = meta;
		this.heat = heat;
	}

	public HeatValue(String block, int heat) {
	    this.block = block;
        this.heat = heat;
        this.meta = -1;
    }

	public String getBlock() {
		return block;
	}

	public void setBlock(String block) {
	    this.block = block;
    }

    public int getMeta() {
		return this.meta;
	}

    public HeatValue setMeta(int meta) {
        this.meta = meta;
        return this;
    }

	public int getHeat() {
		return heat;
	}

    public void setHeat(int heat) {
        this.heat = heat;
    }
	
}
