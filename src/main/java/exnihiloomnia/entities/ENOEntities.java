package exnihiloomnia.entities;

import exnihiloomnia.ENO;
import exnihiloomnia.entities.thrown.stone.EntityStone;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ENOEntities {
	public static final int STONE_ID = 0;

	public static void configure() {
		EntityRegistry.registerModEntity(EntityStone.class, "stone", STONE_ID, ENO.INSTANCE, 64, 10, true);
	}
}
