package exnihiloomnia.registries.composting.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import exnihiloomnia.ENO;
import exnihiloomnia.registries.composting.CompostRegistryEntry;
import exnihiloomnia.registries.composting.pojos.CompostRecipe;
import exnihiloomnia.registries.composting.pojos.CompostRecipeList;

public class CompostRecipeLoader {
	public static Gson gson = new GsonBuilder().setPrettyPrinting().create();
	public static ArrayList<CompostRegistryEntry> entries; 
	
	public static List<CompostRegistryEntry> load(String path)
	{	
		generateExampleJsonFile(path);
		entries = new ArrayList<CompostRegistryEntry>();
		
		File[] files = new File(path).listFiles();
		
		for (File file : files)
		{
			if (!file.getName().equals("example.json"))//Ignore the example file
			{
				CompostRecipeList list = loadRecipes(file);
				
				if (list != null && !list.getRecipes().isEmpty())
				{
					for (CompostRecipe recipe : list.getRecipes())
					{
						CompostRegistryEntry entry = CompostRegistryEntry.fromRecipe(recipe);
						
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
		CompostRecipeList recipes = null;
		
		if (!file.exists())
		{
			ENO.log.info("Attempting to generate example compost recipe file: '" + file + "'.");
			
			recipes = CompostRecipeExample.getExampleRecipeList();
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
	
	private static CompostRecipeList loadRecipes(File file)
	{
		CompostRecipeList recipes = null;
		
		try 
		{
			BufferedReader reader = new BufferedReader(new FileReader(file)); 
			
			if (reader.ready())
			{
				recipes = gson.fromJson(reader, CompostRecipeList.class);
			}
			
			reader.close();
		} 
		catch (Exception e) 
		{
			ENO.log.error("Failed to read COMPOST recipe file: '" + file + "'.");
			ENO.log.error(e);
		}  
		
		return recipes;
	}
}
