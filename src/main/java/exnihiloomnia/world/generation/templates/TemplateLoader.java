package exnihiloomnia.world.generation.templates;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import exnihiloomnia.ENO;
import exnihiloomnia.world.generation.templates.pojos.Template;

public class TemplateLoader {
	public static Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	public static Template load(String path)
	{
		File file = new File(path);
		Template template = null;
		
		if (file.exists())
		{
			try 
			{
				BufferedReader reader = new BufferedReader(new FileReader(path)); 
				
				if (reader.ready())
				{
					template = gson.fromJson(reader, Template.class);
				}
				
				reader.close();
			} 
			catch (Exception e) 
			{
				ENO.log.error("Failed to read map file: '" + file + "'.");
				ENO.log.error(e);
			}  
		}

		return template;
	}
}
