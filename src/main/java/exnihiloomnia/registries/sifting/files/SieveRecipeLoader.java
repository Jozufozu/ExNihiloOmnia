package exnihiloomnia.registries.sifting.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import exnihiloomnia.ENO;
import exnihiloomnia.registries.sifting.SieveRegistryEntry;
import exnihiloomnia.registries.sifting.pojos.SieveRecipe;
import exnihiloomnia.registries.sifting.pojos.SieveRecipeList;

public class SieveRecipeLoader {
	public static Gson gson = new GsonBuilder().setPrettyPrinting().create();
	public static ArrayList<SieveRegistryEntry> entries; 
	
	public static List<SieveRegistryEntry> load(String path)
	{	
		generateExampleJsonFile(path);
		entries = new ArrayList<SieveRegistryEntry>();
		
		File[] files = new File(path).listFiles();
		
		for (File file : files)
		{
			if (!file.getName().equals("example.json"))//Ignore the example file
			{
				SieveRecipeList list = loadRecipes(file);
				
				if (list != null && !list.getRecipes().isEmpty())
				{
					for (SieveRecipe recipe : list.getRecipes())
					{
						SieveRegistryEntry entry = SieveRegistryEntry.fromRecipe(recipe);
						
						if (entry != null)
						{
							entries.add(entry);
						}
					}
				}
			}
		}
		
		return entries;
	}
	
	private static void generateExampleJsonFile(String path)
	{
		File file = new File(path + "example.json");
		SieveRecipeList recipes = null;
		
		if (!file.exists())
		{
			ENO.log.info("Attempting to generate example sieve recipe file: '" + file + "'.");
			
			recipes = SieveRecipeExample.getExampleRecipeList();
			FileWriter writer;
			
			try 
			{
				file.getParentFile().mkdirs();
				
				writer = new FileWriter(file);
				writer.write(gson.toJson(recipes)); 
				writer.close();
			} 
			catch (Exception e) 
			{
				ENO.log.error("Failed to write file: '" + file + "'.");
				ENO.log.error(e);
			}  
		}
	}
	
	private static SieveRecipeList loadRecipes(File file)
	{
		SieveRecipeList recipes = null;
		
		try 
		{
			BufferedReader reader = new BufferedReader(new FileReader(file)); 
			
			if (reader.ready())
			{
				recipes = gson.fromJson(reader, SieveRecipeList.class);
			}
			
			reader.close();
		} 
		catch (Exception e) 
		{
			ENO.log.error("Failed to read sieve recipe file: '" + file + "'.");
			ENO.log.error(e);
		}  
		
		return recipes;
	}
}
