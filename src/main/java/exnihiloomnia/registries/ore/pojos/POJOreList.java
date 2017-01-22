package exnihiloomnia.registries.ore.pojos;

import java.util.ArrayList;

public class POJOreList {
	private ArrayList<POJOre> entries = new ArrayList<>();

	public ArrayList<POJOre> getEntries() {
		return entries;
	}

	public void setEntries(ArrayList<POJOre> recipes) {
		this.entries = recipes;
	}

	public void addEntry(POJOre recipe) {
		this.entries.add(recipe);
	}
}
