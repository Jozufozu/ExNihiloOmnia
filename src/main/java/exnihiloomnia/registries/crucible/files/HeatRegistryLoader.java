package exnihiloomnia.registries.crucible.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import exnihiloomnia.ENO;
import exnihiloomnia.registries.crucible.HeatRegistryEntry;
import exnihiloomnia.registries.crucible.pojos.HeatValue;
import exnihiloomnia.registries.crucible.pojos.HeatValueList;

public class HeatRegistryLoader {
	public static Gson gson = new GsonBuilder().setPrettyPrinting().create();
	public static ArrayList<HeatRegistryEntry> entries;
	
	public static List<HeatRegistryEntry> load(String path) {	
		generateExampleJsonFile(path);
		entries = new ArrayList<HeatRegistryEntry>();
		
		File[] files = new File(path).listFiles();
		
		for (File file : files) {
			if (!file.getName().equals("example.json")) {//Ignore the example file
				HeatValueList list = loadRecipes(file);
				
				if (list != null && !list.getRecipes().isEmpty()) {
					for (HeatValue recipe : list.getRecipes()) {
						if (recipe.getMeta() == -1) {
                            for (int x = 0; x <= 15; x++) {
                                HeatRegistryEntry entry = HeatRegistryEntry.fromRecipe(recipe.setMeta(x));
                                if (entry != null) {
                                    entries.add(entry);
                                }
                            }
                        }
                        else {
                            HeatRegistryEntry entry = HeatRegistryEntry.fromRecipe(recipe);
                            if (entry != null) {
                                entries.add(entry);
                            }
                        }
					}
				}
			}
		}
		
		return entries;
	}
	
	private static void generateExampleJsonFile(String path) {
		File file = new File(path + "example.json");
		HeatValueList recipes;
		
		if (!file.exists()) {
			ENO.log.info("Attempting to generate example heat value file: '" + file + "'.");
			
			recipes = HeatRegistryExample.getExampleRecipeList();
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
	
	private static HeatValueList loadRecipes(File file) {
		HeatValueList recipes = null;
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file)); 
			
			if (reader.ready()) {
				recipes = gson.fromJson(reader, HeatValueList.class);
			}
			
			reader.close();
		} 
		catch (Exception e) {
			ENO.log.error("Failed to read heat value file: '" + file + "'.");
			ENO.log.error(e);
		}  
		
		return recipes;
	}
}
