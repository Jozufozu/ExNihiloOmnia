package exnihiloomnia.registries.hammering.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import exnihiloomnia.ENO;
import exnihiloomnia.registries.hammering.HammerRegistryEntry;
import exnihiloomnia.registries.hammering.pojos.HammerRecipe;
import exnihiloomnia.registries.hammering.pojos.HammerRecipeList;

public class HammerRecipeLoader {
	public static Gson gson = new GsonBuilder().setPrettyPrinting().create();
	public static ArrayList<HammerRegistryEntry> entries; 
	
	public static List<HammerRegistryEntry> load(String path) {	
		generateExampleJsonFile(path);
		entries = new ArrayList<HammerRegistryEntry>();
		
		File[] files = new File(path).listFiles();
		
		for (File file : files) {
			if (!file.getName().equals("example.json") && !file.getName().equals("defaults.json")) {//Ignore the example file
				HammerRecipeList list = loadRecipes(file);
				
				if (list != null && !list.getRecipes().isEmpty()) {
					for (HammerRecipe recipe : list.getRecipes()) {
						HammerRegistryEntry entry = HammerRegistryEntry.fromRecipe(recipe);
						
						if (entry != null) {
							entries.add(entry);
						}
					}
				}
			}
		}
		
		return entries;
	}
	
	private static void generateExampleJsonFile(String path) {
		File file = new File(path + "example.json");
		HammerRecipeList recipes = null;
		
		if (!file.exists()) {
			ENO.log.info("Attempting to generate example hammer recipe file: '" + file + "'.");
			
			recipes = HammerRecipeExample.getExampleRecipeList();
			FileWriter writer;
			
			try {
				file.getParentFile().mkdirs();
				
				writer = new FileWriter(file);
				writer.write(gson.toJson(recipes)); 
				writer.close();
			} 
			catch (Exception e) {
				ENO.log.error("Failed to write file: '" + file + "'.");
				ENO.log.error(e);
			}  
		}
	}
	
	private static HammerRecipeList loadRecipes(File file) {
		HammerRecipeList recipes = null;
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file)); 
			
			if (reader.ready()) {
				recipes = gson.fromJson(reader, HammerRecipeList.class);
			}
			
			reader.close();
		} 
		catch (Exception e) {
			ENO.log.error("Failed to read hammer recipe file: '" + file + "'.");
			ENO.log.error(e);
		}  
		
		return recipes;
	}

	public static void dumpRecipes(HashMap<String, HammerRegistryEntry> recipes, String path) {
		if (!recipes.isEmpty()) {
			HammerRecipeList list = new HammerRecipeList();

			for (HammerRegistryEntry entry : recipes.values()) {
				list.addRecipe(entry.toRecipe());
			}

			File file = new File(path + "defaults.json");

			ENO.log.info("Attempting to dump hammer recipe list: '" + file + "'.");

			FileWriter writer;

			try {
				file.getParentFile().mkdirs();

				writer = new FileWriter(file);
				writer.write(gson.toJson(list));
				writer.close();
			} catch (Exception e) {
				ENO.log.error("Failed to write file: '" + file + "'.");
				ENO.log.error(e);
			}
		}
	}
}
