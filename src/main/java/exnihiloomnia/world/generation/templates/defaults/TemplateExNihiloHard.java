package exnihiloomnia.world.generation.templates.defaults;

import java.io.File;
import java.util.ArrayList;

import exnihiloomnia.world.generation.templates.io.TemplateGenerator;
import exnihiloomnia.world.generation.templates.pojos.Template;
import exnihiloomnia.world.generation.templates.pojos.TemplateBlock;

public class TemplateExNihiloHard extends TemplateGenerator{

	public static void generate(String path)
	{
		generateTemplateFile(path + File.separator + "ex_nihilo_hard_overworld.json", getOverworldTemplate());
	}

	private static Template getOverworldTemplate()
	{
		Template map = new Template();

		ArrayList<TemplateBlock> blocks = map.getBlocks();

		//DIRT ISLAND
		//layer 0
		blocks.add(new TemplateBlock("minecraft:dirt", 0, 0, -7, 0));

		//TREE
		//logs
		blocks.add(new TemplateBlock("minecraft:log", 2, 0, -6, 0));
		blocks.add(new TemplateBlock("minecraft:log", 2, 0, -5, 0));
		blocks.add(new TemplateBlock("minecraft:log", 2, 0, -4, 0));
		blocks.add(new TemplateBlock("minecraft:log", 2, 0, -3, 0));
		
		blocks.add(new TemplateBlock("minecraft:leaves", 2, 0, -2, 0));
		blocks.add(new TemplateBlock("minecraft:leaves", 2, 0, -1, 0));

		//leaves layer 0
		blocks.add(new TemplateBlock("minecraft:leaves", 2, -2, -3, 1));
		blocks.add(new TemplateBlock("minecraft:leaves", 2, -2, -3, 0));
		blocks.add(new TemplateBlock("minecraft:leaves", 2, -2, -3, -1));
		blocks.add(new TemplateBlock("minecraft:leaves", 2, -2, -3, -2));
		
		blocks.add(new TemplateBlock("minecraft:leaves", 2, -1, -3, 2));
		blocks.add(new TemplateBlock("minecraft:leaves", 2, -1, -3, 1));
		blocks.add(new TemplateBlock("minecraft:leaves", 2, -1, -3, 0));
		blocks.add(new TemplateBlock("minecraft:leaves", 2, -1, -3, -1));
		blocks.add(new TemplateBlock("minecraft:leaves", 2, -1, -3, -2));

		blocks.add(new TemplateBlock("minecraft:leaves", 2, 0, -3, 2));
		blocks.add(new TemplateBlock("minecraft:leaves", 2, 0, -3, 1));
		blocks.add(new TemplateBlock("minecraft:leaves", 2, 0, -3, -1));
		blocks.add(new TemplateBlock("minecraft:leaves", 2, 0, -3, -2));
		
		blocks.add(new TemplateBlock("minecraft:leaves", 2, 1, -3, 2));
		blocks.add(new TemplateBlock("minecraft:leaves", 2, 1, -3, 1));
		blocks.add(new TemplateBlock("minecraft:leaves", 2, 1, -3, 0));
		blocks.add(new TemplateBlock("minecraft:leaves", 2, 1, -3, -1));
		blocks.add(new TemplateBlock("minecraft:leaves", 2, 1, -3, -2));

		blocks.add(new TemplateBlock("minecraft:leaves", 2, 2, -3, 2));
		blocks.add(new TemplateBlock("minecraft:leaves", 2, 2, -3, 1));
		blocks.add(new TemplateBlock("minecraft:leaves", 2, 2, -3, 0));
		blocks.add(new TemplateBlock("minecraft:leaves", 2, 2, -3, -1));

		//leaves layer 1
		blocks.add(new TemplateBlock("minecraft:leaves", 2, -2, -2, 1));
		blocks.add(new TemplateBlock("minecraft:leaves", 2, -2, -2, 0));
		blocks.add(new TemplateBlock("minecraft:leaves", 2, -2, -2, -1));

		blocks.add(new TemplateBlock("minecraft:leaves", 2, -1, -2, 2));
		blocks.add(new TemplateBlock("minecraft:leaves", 2, -1, -2, 1));
		blocks.add(new TemplateBlock("minecraft:leaves", 2, -1, -2, 0));
		blocks.add(new TemplateBlock("minecraft:leaves", 2, -1, -2, -1));
		blocks.add(new TemplateBlock("minecraft:leaves", 2, -1, -2, -2));

		blocks.add(new TemplateBlock("minecraft:leaves", 2, 0, -2, 2));
		blocks.add(new TemplateBlock("minecraft:leaves", 2, 0, -2, 1));
		blocks.add(new TemplateBlock("minecraft:leaves", 2, 0, -2, -1));
		blocks.add(new TemplateBlock("minecraft:leaves", 2, 0, -2, -2));

		blocks.add(new TemplateBlock("minecraft:leaves", 2, 1, -2, 2));
		blocks.add(new TemplateBlock("minecraft:leaves", 2, 1, -2, 1));
		blocks.add(new TemplateBlock("minecraft:leaves", 2, 1, -2, 0));
		blocks.add(new TemplateBlock("minecraft:leaves", 2, 1, -2, -1));
		blocks.add(new TemplateBlock("minecraft:leaves", 2, 1, -2, -2));

		blocks.add(new TemplateBlock("minecraft:leaves", 2, 2, -2, 1));
		blocks.add(new TemplateBlock("minecraft:leaves", 2, 2, -2, 0));
		blocks.add(new TemplateBlock("minecraft:leaves", 2, 2, -2, -1));
		
		//leaves layer 2
		blocks.add(new TemplateBlock("minecraft:leaves", 2, -1, -1, 1));
		blocks.add(new TemplateBlock("minecraft:leaves", 2, -1, -1, 0));
		blocks.add(new TemplateBlock("minecraft:leaves", 2, 0, -1, 1));
		blocks.add(new TemplateBlock("minecraft:leaves", 2, 0, -1, -1));
		blocks.add(new TemplateBlock("minecraft:leaves", 2, 1, -1, 0));

		//leaves layer 3
		blocks.add(new TemplateBlock("minecraft:leaves", 2, -1, 0, 0));
		blocks.add(new TemplateBlock("minecraft:leaves", 2, 0, 0, 1));
		blocks.add(new TemplateBlock("minecraft:leaves", 2, 0, 0, 0));
		blocks.add(new TemplateBlock("minecraft:leaves", 2, 0, 0, -1));
		blocks.add(new TemplateBlock("minecraft:leaves", 2, 1, 0, 0));

		return map;
	}
}
