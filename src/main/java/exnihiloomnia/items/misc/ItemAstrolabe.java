package exnihiloomnia.items.misc;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import exnihiloomnia.items.ENOItems;
import exnihiloomnia.world.ENOWorld;
import exnihiloomnia.world.generation.templates.io.TemplateWorldExporter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemAstrolabe extends Item{
	private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
	
	public ItemAstrolabe()
	{
		this.setMaxStackSize(1);
		this.setCreativeTab(ENOItems.ENO_TAB);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack item, World world, EntityPlayer player, EnumHand hand) {
		if (!world.isRemote)
		{
			if (ENOWorld.isWorldGenerationOverridden(world.provider.getDimension()))
			{
				if (item.hasDisplayName())
				{
					TemplateWorldExporter.generate(item.getDisplayName(), world, player);
				}
				else
				{
					TemplateWorldExporter.generate("export_" + format.format(new Date()), world, player);
				}
			}
			
			player.setHeldItem(hand, null);
		}
		
		return new ActionResult<ItemStack>(EnumActionResult.PASS, item);
	}
}
