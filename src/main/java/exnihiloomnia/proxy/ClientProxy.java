package exnihiloomnia.proxy;

import java.util.ArrayList;

import exnihiloomnia.ENO;
import exnihiloomnia.blocks.ENOBlocks;
import exnihiloomnia.blocks.barrels.renderer.BarrelRenderer;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import exnihiloomnia.blocks.crucibles.renderer.CrucibleRenderer;
import exnihiloomnia.blocks.crucibles.tileentity.TileEntityCrucible;
import exnihiloomnia.blocks.leaves.InfestedLeavesRenderer;
import exnihiloomnia.blocks.leaves.TileEntityInfestedLeaves;
import exnihiloomnia.blocks.sieves.renderer.SieveRenderer;
import exnihiloomnia.blocks.sieves.tileentity.TileEntitySieve;
import exnihiloomnia.client.models.ENOModels;
import exnihiloomnia.compatibility.ENOOres;
import exnihiloomnia.entities.thrown.stone.EntityStone;
import exnihiloomnia.entities.thrown.stone.EntityStoneRenderer;
import exnihiloomnia.items.ENOItems;
import exnihiloomnia.items.ores.ItemOre;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

//Commands that only execute on the client.
public class ClientProxy extends Proxy {
	
    @Override
    public void registerModels() {
        ENOModels.register();
    }

    @Override
    public void registerRenderers() {
        registerItemModels();
        registerBlockModels();
        registerBlockRenderers();
        registerEntityRenderers();
    }

    @Override
    public void registerColors() {
        ENOOres.regColors();
    }

    public static void registerItemModels() {
        for (Item item : ENOItems.getItems()) {
            registerRenderer(item);
        }
    }

    public static void registerBlockModels() {
        for (Block block : ENOBlocks.getBlocks()) {
            registerRenderer(block);
        }
    }

    private void registerBlockRenderers() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBarrel.class, new BarrelRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySieve.class, new SieveRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCrucible.class, new CrucibleRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityInfestedLeaves.class, new InfestedLeavesRenderer());
    }

    private void registerEntityRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(EntityStone.class, EntityStoneRenderer.INSTANCE);

    }

    private static void registerRenderer(Block block) {
        Item item = Item.getItemFromBlock(block);

        if (item != null) {
            if (item.getHasSubtypes()) {
                ArrayList<ItemStack> list = new ArrayList<ItemStack>();

                block.getSubBlocks(item, null, list);

                for (ItemStack i : list) {
                    ModelBakery.registerItemVariants(i.getItem(), i.getItem().getRegistryName());
                    //if (Block.getBlockFromItem(i.getItem()) instanceof BlockOre)
                    //    ModelLoader.setCustomModelResourceLocation(item, i.getItemDamage(), new ModelResourceLocation(ENO.MODID + ":ore_" + ((BlockOre) Block.getBlockFromItem(i.getItem())).getType().getName()));
                    //else
                        ModelLoader.setCustomModelResourceLocation(item, i.getItemDamage(), new ModelResourceLocation(ENO.MODID + ":" + i.getUnlocalizedName().substring(5)));
                }
            }
            else {
                ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(ENO.MODID + ":" + item.getUnlocalizedName().substring(5)));
            }
        }
    }

    private static void registerRenderer(Item item) {
        if (item.getHasSubtypes()) {
            ArrayList<ItemStack> list = new ArrayList<ItemStack>();

            item.getSubItems(item, null, list);

            for (ItemStack i : list) {
                ModelBakery.registerItemVariants(i.getItem(), i.getItem().getRegistryName());
                if (i.getItem() instanceof ItemOre)
                    ModelLoader.setCustomModelResourceLocation(item, i.getItemDamage(), new ModelResourceLocation(ENO.MODID + ":ore_" + ((ItemOre) i.getItem()).getType().getName()));
                else
                    ModelLoader.setCustomModelResourceLocation(item, i.getItemDamage(), new ModelResourceLocation(ENO.MODID + ":" + i.getUnlocalizedName().substring(5)));
            }
        }
        else {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(ENO.MODID + ":" + item.getUnlocalizedName().substring(5)));
        }
    }
}
