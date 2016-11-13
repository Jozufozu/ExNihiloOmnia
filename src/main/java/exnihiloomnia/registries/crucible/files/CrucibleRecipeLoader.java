package exnihiloomnia.registries.crucible.files;

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
import exnihiloomnia.registries.crucible.CrucibleRegistryEntry;
import exnihiloomnia.registries.crucible.pojos.CrucibleRecipe;
import exnihiloomnia.registries.crucible.pojos.CrucibleRegistryList;

public class CrucibleRecipeLoader {
	public static Gson gson = new GsonBuilder().setPrettyPrinting().create();
	public static ArrayList<CrucibleRegistryEntry> entries;
	
	public static List<CrucibleRegistryEntry> load(String path) {	
		generateExampleJsonFile(path);
		entries = new ArrayList<CrucibleRegistryEntry>();
		
		File[] files = new File(path).listFiles();
		
		for (File file : files) {
			if (!file.getName().equals("example.json") && !file.getName().equals("defaults.json")) {//Ignore the example file
				CrucibleRegistryList list = loadRecipes(file);
				
				if (list != null && !list.getRecipes().isEmpty()) {
					for (CrucibleRecipe recipe : list.getRecipes()) {
						CrucibleRegistryEntry entry = CrucibleRegistryEntry.fromRecipe(recipe);
						
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
		CrucibleRegistryList recipes;
		
		if (!file.exists()) {
			ENO.log.info("Attempting to generate example crucible recipe file: '" + file + "'.");
			
			recipes = CrucibleRecipeExample.getExampleRecipeList();
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
	
	private static CrucibleRegistryList loadRecipes(File file) {
		CrucibleRegistryList recipes = null;
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file)); 
			
			if (reader.ready()) {
				recipes = gson.fromJson(reader, CrucibleRegistryList.class);
			}
			
			reader.close();
		} 
		catch (Exception e) {
			ENO.log.error("Failed to read crucible recipe file: '" + file + "'.");
			ENO.log.error(e);
		}  
		
		return recipes;
	}

	public static void dumpRecipes(HashMap<String, CrucibleRegistryEntry> recipes, String path) {
		if (!recipes.isEmpty()) {
			CrucibleRegistryList list = new CrucibleRegistryList();

			for (CrucibleRegistryEntry entry : recipes.values()) {
				list.addRecipe(entry.toRecipe());
			}

			File file = new File(path + "defaults.json");

			ENO.log.info("Attempting to dump crucible melting list: '" + file + "'.");

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
