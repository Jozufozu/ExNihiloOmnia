package exnihiloomnia.world.generation.templates.defaults;

import java.io.File;
import java.util.ArrayList;

import exnihiloomnia.world.generation.templates.io.TemplateGenerator;
import exnihiloomnia.world.generation.templates.pojos.Template;
import exnihiloomnia.world.generation.templates.pojos.TemplateBlock;
import exnihiloomnia.world.generation.templates.pojos.TemplateItem;

public class TemplateExNihiloEasy extends TemplateGenerator{

	public static void generate(String path)
	{
		generateTemplateFile(path + File.separator + "ex_nihilo_easy_overworld.json", getOverworldTemplate());
	}

	private static Template getOverworldTemplate()
	{
		Template map = new Template();

		ArrayList<TemplateBlock> blocks = map.getBlocks();

		//DIRT ISLAND
		//layer 0
		blocks.add(new TemplateBlock("minecraft:dirt", 0, 0, 0, 0));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, 0, 0, 1));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, 0, 0, 2));

		blocks.add(new TemplateBlock("minecraft:dirt", 0, 1, 0, 0));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, 1, 0, 1));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, 1, 0, 2));

		blocks.add(new TemplateBlock("minecraft:dirt", 0, 2, 0, 0));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, 2, 0, 1));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, 2, 0, 2));

		//layer 1
		blocks.add(new TemplateBlock("minecraft:dirt", 0, 0, -1, 0));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, 0, -1, 1));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, 0, -1, 2));

		blocks.add(new TemplateBlock("minecraft:dirt", 0, 1, -1, 0));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, 1, -1, 1));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, 1, -1, 2));

		blocks.add(new TemplateBlock("minecraft:dirt", 0, 2, -1, 0));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, 2, -1, 1));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, 2, -1, 2));

		//layer 2
		blocks.add(new TemplateBlock("minecraft:dirt", 0, 0, -2, 0));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, 0, -2, 1));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, 0, -2, 2));

		blocks.add(new TemplateBlock("minecraft:dirt", 0, 1, -2, 0));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, 1, -2, 1));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, 1, -2, 2));

		blocks.add(new TemplateBlock("minecraft:dirt", 0, 2, -2, 0));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, 2, -2, 1));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, 2, -2, 2));

		//CHEST
		TemplateBlock chest = new TemplateBlock("minecraft:chest", 2, 1, 1, 0);
		chest.setContents(new ArrayList<TemplateItem>());
		chest.getContents().add(new TemplateItem("minecraft:dye", 64, 15)); //BONE Meal
		chest.getContents().add(new TemplateItem("minecraft:dye", 64, 15));
		chest.getContents().add(new TemplateItem("minecraft:dye", 64, 15));
		blocks.add(chest);

		//TREE
		//logs
		blocks.add(new TemplateBlock("minecraft:log", 0, 1, 1, 1));
		blocks.add(new TemplateBlock("minecraft:log", 0, 1, 2, 1));
		blocks.add(new TemplateBlock("minecraft:log", 0, 1, 3, 1));
		blocks.add(new TemplateBlock("minecraft:log", 0, 1, 4, 1));
		blocks.add(new TemplateBlock("minecraft:log", 0, 1, 5, 1));
		blocks.add(new TemplateBlock("minecraft:log", 0, 1, 6, 1));

		//leaves layer 0
		blocks.add(new TemplateBlock("minecraft:leaves", 0, -1, 4, 2));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, -1, 4, 1));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, -1, 4, 0));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, -1, 4, -1));

		blocks.add(new TemplateBlock("minecraft:leaves", 0, 0, 4, 3));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 0, 4, 2));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 0, 4, 1));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 0, 4, 0));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 0, 4, -1));

		blocks.add(new TemplateBlock("minecraft:leaves", 0, 1, 4, 3));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 1, 4, 2));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 1, 4, 0));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 1, 4, -1));

		blocks.add(new TemplateBlock("minecraft:leaves", 0, 2, 4, 3));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 2, 4, 2));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 2, 4, 1));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 2, 4, 0));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 2, 4, -1));

		blocks.add(new TemplateBlock("minecraft:leaves", 0, 3, 4, 3));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 3, 4, 2));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 3, 4, 1));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 3, 4, 0));

		//leaves layer 1
		blocks.add(new TemplateBlock("minecraft:leaves", 0, -1, 5, 2));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, -1, 5, 1));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, -1, 5, 0));

		blocks.add(new TemplateBlock("minecraft:leaves", 0, 0, 5, 3));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 0, 5, 2));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 0, 5, 1));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 0, 5, 0));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 0, 5, -1));

		blocks.add(new TemplateBlock("minecraft:leaves", 0, 1, 5, 3));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 1, 5, 2));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 1, 5, 0));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 1, 5, -1));

		blocks.add(new TemplateBlock("minecraft:leaves", 0, 2, 5, 3));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 2, 5, 2));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 2, 5, 1));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 2, 5, 0));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 2, 5, -1));

		blocks.add(new TemplateBlock("minecraft:leaves", 0, 3, 5, 2));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 3, 5, 1));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 3, 5, 0));
		
		//leaves layer 2
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 0, 6, 2));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 0, 6, 1));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 1, 6, 2));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 1, 6, 0));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 2, 6, 1));

		//leaves layer 3
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 0, 7, 1));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 1, 7, 2));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 1, 7, 1));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 1, 7, 0));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 2, 7, 1));

		return map;
	}
}
