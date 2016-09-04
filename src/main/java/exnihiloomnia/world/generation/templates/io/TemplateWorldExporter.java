package exnihiloomnia.world.generation.templates.io;

import java.io.File;
import java.util.ArrayList;

import exnihiloomnia.world.ENOWorld;
import exnihiloomnia.world.generation.templates.pojos.Template;
import exnihiloomnia.world.generation.templates.pojos.TemplateBlock;
import exnihiloomnia.world.generation.templates.pojos.TemplateItem;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class TemplateWorldExporter extends TemplateGenerator{
	private static final int X_SEARCH_RADIUS = 100;
	private static final int Z_SEARCH_RADIUS = 100;
	
	public static void generate(String name, World world, EntityPlayer player)
	{
		generateTemplateFile(ENOWorld.getTemplatePath() + File.separator + name + ".json", generateWorldTemplate(world, player), true);
	}
	
	private static Template generateWorldTemplate(World world, EntityPlayer player)
	{
		Template map = new Template();
		map.setSpawnYLevel(player.getPosition().getY());
		
		for (int x = 0 - X_SEARCH_RADIUS; x < X_SEARCH_RADIUS; x++)
		{
			for (int z = 0 - Z_SEARCH_RADIUS; z < Z_SEARCH_RADIUS; z++)
			{
				BlockPos pos = new BlockPos(player.getPosition().getX() + x, 1, player.getPosition().getZ() + z);
				Chunk chunk = world.getChunkFromBlockCoords(pos);
				
				for (int y = 1; y < world.getHeight(); y++)
				{
					BlockPos current = new BlockPos(player.getPosition().getX() + x, y, player.getPosition().getZ() + z);
					
					if(!world.isAirBlock(current))
					{
						IBlockState state = world.getBlockState(current);
						TemplateBlock block = new TemplateBlock(
								Block.REGISTRY.getNameForObject(state.getBlock()).toString(),
								state.getBlock().getMetaFromState(state),
								x,
								0 - (player.getPosition().getY() - y), 
								z);
						
						TileEntity te = world.getTileEntity(current);
						if (te != null && te instanceof IInventory)
						{
							IInventory inv = (IInventory) te;
							
							ArrayList<TemplateItem> contents = new ArrayList<TemplateItem>();

							for (int i = 0; i < inv.getSizeInventory(); i++)
							{
								ItemStack stack = inv.getStackInSlot(i);

								if (stack != null)
								{
									TemplateItem item = new TemplateItem(
											Item.REGISTRY.getNameForObject(stack.getItem()).toString(),
											stack.stackSize,
											stack.getMetadata(),
											i);
									
									contents.add(item);
								}
							}
							
							if (contents.size() > 0)
							{
								block.setContents(contents);
							}
						}
						
						map.getBlocks().add(block);
					}
				}
			}
		}
		
		return map;
	}
}
