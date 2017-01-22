package exnihiloomnia.world.generation.templates.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exnihiloomnia.ENO;
import exnihiloomnia.world.generation.templates.pojos.Template;

import java.io.File;
import java.io.FileWriter;

public class TemplateGenerator {
	public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	protected static void generateTemplateFile(String path, Template template) {
		generateTemplateFile(path, template, false);
	}
	
	protected static void generateTemplateFile(String path, Template template, boolean overwrite) {
		File file = new File(path);
		FileWriter writer;
		
		if (!file.exists() && template != null) {
			ENO.log.info("Map file not found '" + file + "'. Attempting to generate template at this path.");
			
			try {
				file.getParentFile().mkdirs();
				
				writer = new FileWriter(file);
				writer.write(gson.toJson(template)); 
				writer.close();
			} 
			catch (Exception e) {
				ENO.log.error("Failed to write file: '" + file + "'.");
				ENO.log.error(e);
			}  
		}
		else if (overwrite) {
			ENO.log.info("Attempting to overwrite template at '" + file + "'");
			
			try {
				writer = new FileWriter(file);
				writer.write(gson.toJson(template)); 
				writer.close();
			} 
			catch (Exception e) {
				ENO.log.error("Failed to write file: '" + file + "'.");
				ENO.log.error(e);
			}  
		}
	}
}
