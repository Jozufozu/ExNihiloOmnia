package exnihiloomnia.registries.ore.files;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exnihiloomnia.ENO;
import exnihiloomnia.registries.ore.Ore;
import exnihiloomnia.registries.ore.pojos.POJOre;
import exnihiloomnia.registries.ore.pojos.POJOreList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OreLoader {
	public static Gson gson = new GsonBuilder().setPrettyPrinting().create();
	public static ArrayList<Ore> entries;
	
	public static List<Ore> load(String path) {
		generateExampleJsonFile(path);
		entries = new ArrayList<Ore>();
		
		File[] files = new File(path).listFiles();
		
		for (File file : files) {
			if (!file.getName().equals("example.json") && !file.getName().equals("defaults.json")) {//Ignore the example file
				POJOreList list = loadRecipes(file);
				
				if (list != null && !list.getEntries().isEmpty()) {
					for (POJOre ore : list.getEntries()) {
						Ore entry = Ore.fromPOJOre(ore);
						
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
		POJOreList recipes;
		
		if (!file.exists()) {
			ENO.log.info("Attempting to generate example ores file: '" + file + "'.");
			
			recipes = OreExample.getExampleRecipeList();
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

	public static void dumpRecipes(HashMap<String, Ore> ores, String path) {
		if (!ores.isEmpty()) {
			POJOreList list = new POJOreList();

			for (Ore entry : ores.values()) {
				list.addEntry(entry.toPOJOre());
			}

			File file = new File(path + "defaults.json");

			ENO.log.info("Attempting to dump ore list: '" + file + "'.");

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
	
	private static POJOreList loadRecipes(File file) {
		POJOreList recipes = null;
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file)); 
			
			if (reader.ready()) {
				recipes = gson.fromJson(reader, POJOreList.class);
			}
			
			reader.close();
		} 
		catch (Exception e) {
			ENO.log.error("Failed to read sieve recipe file: '" + file + "'.");
			ENO.log.error(e);
		}  
		
		return recipes;
	}
}
