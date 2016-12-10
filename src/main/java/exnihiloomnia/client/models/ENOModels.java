package exnihiloomnia.client.models;

import exnihiloomnia.ENO;
import exnihiloomnia.ENOConfig;
import exnihiloomnia.blocks.ENOBlocks;
import exnihiloomnia.items.ENOItems;
import exnihiloomnia.registries.ore.BlockOre;
import exnihiloomnia.registries.ore.OreRegistry;
import exnihiloomnia.util.enums.EnumOreBlockType;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.*;

public class ENOModels {
	public static final ModelResourceLocation WITCHWATER_MODEL_LOCATION = new ModelResourceLocation("exnihiloomnia:witchwater", "fluid");
	public static final ModelResourceLocation PORCELAIN_BUCKET_LOCATION = new ModelResourceLocation(new ResourceLocation(ENO.MODID, "porcelain_bucket"), "inventory");

	public enum LoaderPorcelainBucket implements ICustomModelLoader {
		INSTANCE;

		@Override
		public boolean accepts(ResourceLocation modelLocation) {
			return modelLocation.getResourceDomain().equals(ENO.MODID) && modelLocation.getResourcePath().contains("porcelainbucket");
		}

		@Override
		public IModel loadModel(ResourceLocation modelLocation)
		{
			return ModelDynBucket.MODEL;
		}

		@Override
		public void onResourceManagerReload(IResourceManager resourceManager) {
		}
	}

	public static void register() {
		ModelLoaderRegistry.registerLoader(LoaderPorcelainBucket.INSTANCE);

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

		if (ENOConfig.universal_bucket)
		{
			ModelBakery.registerItemVariants(ENOItems.BUCKET_PORCELAIN);
			ModelLoader.setCustomMeshDefinition(ENOItems.BUCKET_PORCELAIN, new ItemMeshDefinition()
			{
				@Override
				public ModelResourceLocation getModelLocation(ItemStack stack)
				{
					return PORCELAIN_BUCKET_LOCATION;
				}
			});
			ModelBakery.registerItemVariants(ENOItems.BUCKET_PORCELAIN, PORCELAIN_BUCKET_LOCATION);
		}

		for (Block ore : OreRegistry.blocks.values()) {
			ModelLoader.setCustomStateMapper(ore, new StateMapperBase() {
				protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
					EnumOreBlockType type = (EnumOreBlockType) state.getValue(BlockOre.TYPE);
					return type.getLocation();
				}
			});
		}

	}
}
