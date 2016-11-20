package exnihiloomnia.client.models;

import exnihiloomnia.blocks.ENOBlocks;
import exnihiloomnia.items.ENOItems;
import exnihiloomnia.items.buckets.ModelPorcelainBucket;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;

public class ENOModels {
	public static final ModelResourceLocation WITCHWATER_MODEL_LOCATION = new ModelResourceLocation("exnihiloomnia:witchwater", "fluid");

	public static void register() {
		ModelLoaderRegistry.registerLoader(ModelPorcelainBucket.LoaderDynBucket.INSTANCE);

		ModelBakery.registerItemVariants(new ItemBlock(ENOBlocks.WITCHWATER));
		
		ModelLoader.setCustomStateMapper(ENOBlocks.WITCHWATER, new StateMapperBase() {
			protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
				return WITCHWATER_MODEL_LOCATION;
			}
		});
		
		ModelLoader.setCustomMeshDefinition(new ItemBlock(ENOBlocks.WITCHWATER), new ItemMeshDefinition() {
			public ModelResourceLocation getModelLocation(ItemStack stack) {
				return WITCHWATER_MODEL_LOCATION;
			}
		});


		ModelBakery.registerItemVariants(ENOItems.BUCKET_PORCELAIN);
		ModelLoader.setCustomMeshDefinition(ENOItems.BUCKET_PORCELAIN, new ItemMeshDefinition()
		{
			@Override
			public ModelResourceLocation getModelLocation(ItemStack stack)
			{
				return ModelPorcelainBucket.LOCATION;
			}
		});
		ModelBakery.registerItemVariants(ENOItems.BUCKET_PORCELAIN, ModelPorcelainBucket.LOCATION);
	}
}
