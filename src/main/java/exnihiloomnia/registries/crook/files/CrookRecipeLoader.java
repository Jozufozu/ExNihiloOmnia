package exnihiloomnia.registries.crook.files;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exnihiloomnia.ENO;
import exnihiloomnia.registries.crook.CrookRegistryEntry;
import exnihiloomnia.registries.crook.pojos.CrookRecipe;
import exnihiloomnia.registries.crook.pojos.CrookRecipeList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class CrookRecipeLoader {
	public static Gson gson = new GsonBuilder().setPrettyPrinting().create();
	public static ArrayList<CrookRegistryEntry> entries;
	
	public static List<CrookRegistryEntry> load(String path) {
		generateExampleJsonFile(path);
		entries = new ArrayList<CrookRegistryEntry>();
		
		File[] files = new File(path).listFiles();
		
		for (File file : files) {
			if (!file.getName().equals("example.json")) {//Ignore the example file 
				CrookRecipeList list = loadRecipes(file);
				
				if (list != null && !list.getRecipes().isEmpty()) {
					for (CrookRecipe recipe : list.getRecipes()) {
						CrookRegistryEntry entry = CrookRegistryEntry.fromRecipe(recipe);
						
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
		CrookRecipeList recipes = null;
		
		if (!file.exists()) {
			ENO.log.info("Attempting to generate example crook recipe file: '" + file + "'.");
			
			recipes = CrookRecipeExample.getExampleRecipeList();
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
	
	private static CrookRecipeList loadRecipes(File file) {
		CrookRecipeList recipes = null;
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file)); 
			
			if (reader.ready()) {
				recipes = gson.fromJson(reader, CrookRecipeList.class);
			}
			
			reader.close();
		} 
		catch (Exception e) {
			ENO.log.error("Failed to read crook recipe file: '" + file + "'.");
			ENO.log.error(e);
		}  
		
		return recipes;
	}
}
