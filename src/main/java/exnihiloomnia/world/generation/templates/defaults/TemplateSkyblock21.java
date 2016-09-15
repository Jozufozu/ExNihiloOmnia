package exnihiloomnia.world.generation.templates.defaults;

import java.io.File;
import java.util.ArrayList;

import exnihiloomnia.world.generation.templates.io.TemplateGenerator;
import exnihiloomnia.world.generation.templates.pojos.Template;
import exnihiloomnia.world.generation.templates.pojos.TemplateBlock;
import exnihiloomnia.world.generation.templates.pojos.TemplateItem;

public class TemplateSkyblock21 extends TemplateGenerator {
	
	public static void generate(String path) {
		generateTemplateFile(path + File.separator + "skyblock_2-1_overworld.json", getOverworldTemplate());
		generateTemplateFile(path + File.separator + "skyblock_2-1_nether.json", getNetherTemplate());
	}
	
	private static Template getOverworldTemplate() {
		Template map = new Template();

		ArrayList<TemplateBlock> blocks = map.getBlocks();
		//layer 0
		blocks.add(new TemplateBlock("minecraft:grass", 0, -1, 0, -1));
		blocks.add(new TemplateBlock("minecraft:grass", 0, -1, 0, 0));
		blocks.add(new TemplateBlock("minecraft:grass", 0, -1, 0, 1));

		blocks.add(new TemplateBlock("minecraft:grass", 0, 0, 0, -1));
		blocks.add(new TemplateBlock("minecraft:grass", 0, 0, 0, 0));
		blocks.add(new TemplateBlock("minecraft:grass", 0, 0, 0, 1));

		blocks.add(new TemplateBlock("minecraft:grass", 0, 1, 0, -1));
		blocks.add(new TemplateBlock("minecraft:grass", 0, 1, 0, 0));
		blocks.add(new TemplateBlock("minecraft:grass", 0, 1, 0, 1));

		//layer 1
		blocks.add(new TemplateBlock("minecraft:dirt", 0, -1, -1, -1));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, -1, -1, 0));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, -1, -1, 1));

		blocks.add(new TemplateBlock("minecraft:dirt", 0, 0, -1, -1));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, 0, -1, 0));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, 0, -1, 1));

		blocks.add(new TemplateBlock("minecraft:dirt", 0, 1, -1, -1));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, 1, -1, 0));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, 1, -1, 1));

		//layer 2
		blocks.add(new TemplateBlock("minecraft:dirt", 0, -1, -2, -1));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, -1, -2, 0));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, -1, -2, 1));

		blocks.add(new TemplateBlock("minecraft:dirt", 0, 0, -2, -1));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, 0, -2, 0));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, 0, -2, 1));

		blocks.add(new TemplateBlock("minecraft:dirt", 0, 1, -2, -1));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, 1, -2, 0));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, 1, -2, 1));

		//layer 0
		blocks.add(new TemplateBlock("minecraft:grass", 0, -4, 0, -1));
		blocks.add(new TemplateBlock("minecraft:grass", 0, -4, 0, 0));
		blocks.add(new TemplateBlock("minecraft:grass", 0, -4, 0, 1));

		blocks.add(new TemplateBlock("minecraft:grass", 0, -3, 0, -1));
		blocks.add(new TemplateBlock("minecraft:grass", 0, -3, 0, 0));
		blocks.add(new TemplateBlock("minecraft:grass", 0, -3, 0, 1));

		blocks.add(new TemplateBlock("minecraft:grass", 0, -2, 0, -1));
		blocks.add(new TemplateBlock("minecraft:grass", 0, -2, 0, 0));
		blocks.add(new TemplateBlock("minecraft:grass", 0, -2, 0, 1));

		//layer 1
		blocks.add(new TemplateBlock("minecraft:dirt", 0, -4, -1, -1));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, -4, -1, 0));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, -4, -1, 1));

		blocks.add(new TemplateBlock("minecraft:dirt", 0, -3, -1, -1));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, -3, -1, 0));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, -3, -1, 1));

		blocks.add(new TemplateBlock("minecraft:dirt", 0, -2, -1, -1));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, -2, -1, 0));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, -2, -1, 1));

		//layer 2
		blocks.add(new TemplateBlock("minecraft:dirt", 0, -4, -2, -1));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, -4, -2, 0));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, -4, -2, 1));

		blocks.add(new TemplateBlock("minecraft:dirt", 0, -3, -2, -1));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, -3, -2, 0));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, -3, -2, 1));

		blocks.add(new TemplateBlock("minecraft:dirt", 0, -2, -2, -1));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, -2, -2, 0));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, -2, -2, 1));

		//layer 0
		blocks.add(new TemplateBlock("minecraft:grass", 0, -1, 0, -4));
		blocks.add(new TemplateBlock("minecraft:grass", 0, -1, 0, -3));
		blocks.add(new TemplateBlock("minecraft:grass", 0, -1, 0, -2));

		blocks.add(new TemplateBlock("minecraft:grass", 0, 0, 0, -4));
		blocks.add(new TemplateBlock("minecraft:grass", 0, 0, 0, -3));
		blocks.add(new TemplateBlock("minecraft:grass", 0, 0, 0, -2));

		blocks.add(new TemplateBlock("minecraft:grass", 0, 1, 0, -4));
		blocks.add(new TemplateBlock("minecraft:grass", 0, 1, 0, -3));
		blocks.add(new TemplateBlock("minecraft:grass", 0, 1, 0, -2));

		//layer 1
		blocks.add(new TemplateBlock("minecraft:dirt", 0, -1, -1, -4));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, -1, -1, -3));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, -1, -1, -2));

		blocks.add(new TemplateBlock("minecraft:dirt", 0, 0, -1, -4));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, 0, -1, -3));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, 0, -1, -2));

		blocks.add(new TemplateBlock("minecraft:dirt", 0, 1, -1, -4));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, 1, -1, -3));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, 1, -1, -2));

		//layer 2
		blocks.add(new TemplateBlock("minecraft:dirt", 0, -1, -2, -4));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, -1, -2, -3));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, -1, -2, -2));

		blocks.add(new TemplateBlock("minecraft:dirt", 0, 0, -2, -4));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, 0, -2, -3));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, 0, -2, -2));

		blocks.add(new TemplateBlock("minecraft:dirt", 0, 1, -2, -4));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, 1, -2, -3));
		blocks.add(new TemplateBlock("minecraft:dirt", 0, 1, -2, -2));

		//chest
		TemplateBlock chest = new TemplateBlock("minecraft:chest", 5, -4, 1, 0);
		chest.setContents(new ArrayList<TemplateItem>());
		chest.getContents().add(new TemplateItem("minecraft:lava_bucket", 1, 0));
		chest.getContents().add(new TemplateItem("minecraft:ice", 1, 0));
		blocks.add(chest);
		
		//tree logs
		blocks.add(new TemplateBlock("minecraft:log", 0, 1, 1, -4));
		blocks.add(new TemplateBlock("minecraft:log", 0, 1, 2, -4));
		blocks.add(new TemplateBlock("minecraft:log", 0, 1, 3, -4));
		blocks.add(new TemplateBlock("minecraft:log", 0, 1, 4, -4));
		blocks.add(new TemplateBlock("minecraft:log", 0, 1, 5, -4));
		blocks.add(new TemplateBlock("minecraft:log", 0, 1, 6, -4));
		
		//tree leaves layer 0
		blocks.add(new TemplateBlock("minecraft:leaves", 0, -1, 4, -3));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, -1, 4, -4));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, -1, 4, -5));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, -1, 4, -6));
		
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 0, 4, -2));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 0, 4, -3));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 0, 4, -4));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 0, 4, -5));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 0, 4, -6));
		
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 1, 4, -2));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 1, 4, -3));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 1, 4, -5));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 1, 4, -6));

		blocks.add(new TemplateBlock("minecraft:leaves", 0, 2, 4, -2));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 2, 4, -3));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 2, 4, -4));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 2, 4, -5));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 2, 4, -6));

		blocks.add(new TemplateBlock("minecraft:leaves", 0, 3, 4, -2));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 3, 4, -3));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 3, 4, -4));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 3, 4, -5));

		//tree leaves layer 1
		blocks.add(new TemplateBlock("minecraft:leaves", 0, -1, 5, -3));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, -1, 5, -4));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, -1, 5, -5));

		blocks.add(new TemplateBlock("minecraft:leaves", 0, 0, 5, -2));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 0, 5, -3));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 0, 5, -4));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 0, 5, -5));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 0, 5, -6));

		blocks.add(new TemplateBlock("minecraft:leaves", 0, 1, 5, -2));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 1, 5, -3));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 1, 5, -5));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 1, 5, -6));

		blocks.add(new TemplateBlock("minecraft:leaves", 0, 2, 5, -2));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 2, 5, -3));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 2, 5, -4));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 2, 5, -5));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 2, 5, -6));

		blocks.add(new TemplateBlock("minecraft:leaves", 0, 3, 5, -3));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 3, 5, -4));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 3, 5, -5));
		
		//tree leaves layer 2
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 0, 6, -3));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 0, 6, -4));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 1, 6, -3));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 1, 6, -5));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 2, 6, -4));
		
		//tree leaves layer 3
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 0, 7, -4));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 1, 7, -3));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 1, 7, -4));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 1, 7, -5));
		blocks.add(new TemplateBlock("minecraft:leaves", 0, 2, 7, -4));
		
		//sand island
		//layer 0
		blocks.add(new TemplateBlock("minecraft:sand", 0, 66, 0, -1));
		blocks.add(new TemplateBlock("minecraft:sand", 0, 66, 0, 0));
		blocks.add(new TemplateBlock("minecraft:sand", 0, 66, 0, 1));

		blocks.add(new TemplateBlock("minecraft:sand", 0, 65, 0, -1));
		blocks.add(new TemplateBlock("minecraft:sand", 0, 65, 0, 0));
		blocks.add(new TemplateBlock("minecraft:sand", 0, 65, 0, 1));

		blocks.add(new TemplateBlock("minecraft:sand", 0, 64, 0, -1));
		blocks.add(new TemplateBlock("minecraft:sand", 0, 64, 0, 0));
		blocks.add(new TemplateBlock("minecraft:sand", 0, 64, 0, 1));

		//layer 1
		blocks.add(new TemplateBlock("minecraft:sand", 0, 66, -1, -1));
		blocks.add(new TemplateBlock("minecraft:sand", 0, 66, -1, 0));
		blocks.add(new TemplateBlock("minecraft:sand", 0, 66, -1, 1));

		blocks.add(new TemplateBlock("minecraft:sand", 0, 65, -1, -1));
		blocks.add(new TemplateBlock("minecraft:sand", 0, 65, -1, 0));
		blocks.add(new TemplateBlock("minecraft:sand", 0, 65, -1, 1));

		blocks.add(new TemplateBlock("minecraft:sand", 0, 64, -1, -1));
		blocks.add(new TemplateBlock("minecraft:sand", 0, 64, -1, 0));
		blocks.add(new TemplateBlock("minecraft:sand", 0, 64, -1, 1));

		//layer 2
		blocks.add(new TemplateBlock("minecraft:sand", 0, 66, -2, -1));
		blocks.add(new TemplateBlock("minecraft:sand", 0, 66, -2, 0));
		blocks.add(new TemplateBlock("minecraft:sand", 0, 66, -2, 1));

		blocks.add(new TemplateBlock("minecraft:sand", 0, 65, -2, -1));
		blocks.add(new TemplateBlock("minecraft:sand", 0, 65, -2, 0));
		blocks.add(new TemplateBlock("minecraft:sand", 0, 65, -2, 1));

		blocks.add(new TemplateBlock("minecraft:sand", 0, 64, -2, -1));
		blocks.add(new TemplateBlock("minecraft:sand", 0, 64, -2, 0));
		blocks.add(new TemplateBlock("minecraft:sand", 0, 64, -2, 1));
		
		//sand island chest
		TemplateBlock chest2 = new TemplateBlock("minecraft:chest", 4, 65, 1, 0);
		chest2.setContents(new ArrayList<TemplateItem>());
		chest2.getContents().add(new TemplateItem("minecraft:obsidian", 10, 0));
		chest2.getContents().add(new TemplateItem("minecraft:melon", 1, 0));
		chest2.getContents().add(new TemplateItem("minecraft:pumpkin_seeds", 1, 0));
		blocks.add(chest2);
		
		//sand island cactus
		blocks.add(new TemplateBlock("minecraft:cactus", 0, 66, 1, -1));

		return map;
	}
	
	private static Template getNetherTemplate() {
		Template map = new Template();
		map.setSpawnYLevel(50);
		
		ArrayList<TemplateBlock> blocks = map.getBlocks();
		//glowstone island
		//layer 0
		blocks.add(new TemplateBlock("minecraft:glowstone", 0, -1, 0, -1));
		blocks.add(new TemplateBlock("minecraft:glowstone", 0, -1, 0, 0));
		blocks.add(new TemplateBlock("minecraft:glowstone", 0, -1, 0, 1));

		blocks.add(new TemplateBlock("minecraft:glowstone", 0, 0, 0, -1));
		blocks.add(new TemplateBlock("minecraft:glowstone", 0, 0, 0, 0));
		blocks.add(new TemplateBlock("minecraft:glowstone", 0, 0, 0, 1));

		blocks.add(new TemplateBlock("minecraft:glowstone", 0, 1, 0, -1));
		blocks.add(new TemplateBlock("minecraft:glowstone", 0, 1, 0, 0));
		blocks.add(new TemplateBlock("minecraft:glowstone", 0, 1, 0, 1));

		//layer 1
		blocks.add(new TemplateBlock("minecraft:glowstone", 0, -1, -1, -1));
		blocks.add(new TemplateBlock("minecraft:glowstone", 0, -1, -1, 0));
		blocks.add(new TemplateBlock("minecraft:glowstone", 0, -1, -1, 1));

		blocks.add(new TemplateBlock("minecraft:glowstone", 0, 0, -1, -1));
		blocks.add(new TemplateBlock("minecraft:glowstone", 0, 0, -1, 0));
		blocks.add(new TemplateBlock("minecraft:glowstone", 0, 0, -1, 1));

		blocks.add(new TemplateBlock("minecraft:glowstone", 0, 1, -1, -1));
		blocks.add(new TemplateBlock("minecraft:glowstone", 0, 1, -1, 0));
		blocks.add(new TemplateBlock("minecraft:glowstone", 0, 1, -1, 1));

		//layer 2
		blocks.add(new TemplateBlock("minecraft:glowstone", 0, -1, -2, -1));
		blocks.add(new TemplateBlock("minecraft:glowstone", 0, -1, -2, 0));
		blocks.add(new TemplateBlock("minecraft:glowstone", 0, -1, -2, 1));

		blocks.add(new TemplateBlock("minecraft:glowstone", 0, 0, -2, -1));
		blocks.add(new TemplateBlock("minecraft:glowstone", 0, 0, -2, 0));
		blocks.add(new TemplateBlock("minecraft:glowstone", 0, 0, -2, 1));

		blocks.add(new TemplateBlock("minecraft:glowstone", 0, 1, -2, -1));
		blocks.add(new TemplateBlock("minecraft:glowstone", 0, 1, -2, 0));
		blocks.add(new TemplateBlock("minecraft:glowstone", 0, 1, -2, 1));

		//chest
		TemplateBlock chest = new TemplateBlock("minecraft:chest", 0, 0, 1, 0);
		chest.setContents(new ArrayList<TemplateItem>());
		chest.getContents().add(new TemplateItem("minecraft:sapling", 1, 2));
		chest.getContents().add(new TemplateItem("minecraft:reeds", 1, 0));
		chest.getContents().add(new TemplateItem("minecraft:ice", 1, 0));
		blocks.add(chest);

		return map;
	}
}
