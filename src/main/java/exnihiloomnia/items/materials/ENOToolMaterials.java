package exnihiloomnia.items.materials;

import net.minecraft.init.Items;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;

public class ENOToolMaterials {
	public static ToolMaterial STICK;
	public static ToolMaterial BONE;
	
	public static void configure() {
		STICK = EnumHelper.addToolMaterial("STICK", 0, 59, 2.0F, 0.0F, 15);
		STICK.setRepairItem(new ItemStack(Items.STICK, 0));
		
		BONE = EnumHelper.addToolMaterial("BONE", 1, 131, 4.0F, 1.0F, 5);
		BONE.setRepairItem(new ItemStack(Items.DYE, 1, 15)); //bonemeal
	}
}
