package exnihiloomnia.client.models;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import exnihiloomnia.ENO;
import exnihiloomnia.registries.ore.Ore;
import exnihiloomnia.registries.ore.OreRegistry;
import exnihiloomnia.util.enums.EnumOreBlockType;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IModelCustomData;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import org.apache.commons.lang3.tuple.Pair;

import javax.vecmath.Matrix4f;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ModelOreBlock implements IModel, IModelCustomData {

    private static final ModelResourceLocation CUBE = new ModelResourceLocation("block/cube");
    private static final ResourceLocation MISSING = new ResourceLocation(ENO.MODID, "blocks/ore_gravel_base");
    public static final IModel MODEL = new ModelOreBlock();

    private ResourceLocation texture = MISSING;
    private EnumOreBlockType type = EnumOreBlockType.GRAVEL;
    private Ore ore = null;

    public ModelOreBlock() {
        this(MISSING);
    }

    public ModelOreBlock(Ore ore, EnumOreBlockType type) {
        this.ore = ore;
        this.type = type;
        this.texture = ore.getOreName(type);
    }

    public ModelOreBlock(ResourceLocation texture) {
        this.texture = texture;
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
        return ImmutableList.<ResourceLocation>of(CUBE);
    }

    @Override
    public Collection<ResourceLocation> getTextures() {
        return ImmutableList.of(texture);
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transformMap = IPerspectiveAwareModel.MapWrapper.getTransforms(state);

        TextureAtlasSprite sprite = bakedTextureGetter.apply(texture);
        ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();

        IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelManager().getModel(CUBE);

        if (sprite != null) {
            for (EnumFacing facing : EnumFacing.values())
                builder.add(new BakedQuadRetextured(model.getQuads(null, facing, 0).get(0), sprite));
        }
        return new ModelOreBlock.BakedModelOreBlock(this, builder.build(), sprite, format, Maps.immutableEnumMap(transformMap), Maps.<String, IBakedModel>newHashMap());
    }

    @Override
    public IModelState getDefaultState() {
        return TRSRTransformation.identity();
    }

    @Override
    public IModel process(ImmutableMap<String, String> customData) {
        String oreName = customData.get("ore");
        Ore ore = OreRegistry.getOre(oreName);

        String typeName = customData.get("type");
        EnumOreBlockType type = EnumOreBlockType.valueOf(typeName);

        if (ore == null) ore = this.ore;
        if (type == null) type = this.type;

        // create new model with correct liquid
        return new ModelOreBlock();
    }

    private static final class BakedModelOreBlock implements IPerspectiveAwareModel
    {

        private final ModelOreBlock parent;
        private final Map<String, IBakedModel> cache; // contains all the baked models since they'll never change
        private final ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms;
        private final ImmutableList<BakedQuad> quads;
        private final TextureAtlasSprite particle;
        private final VertexFormat format;

        public BakedModelOreBlock(ModelOreBlock parent,
                              ImmutableList<BakedQuad> quads, TextureAtlasSprite particle, VertexFormat format, ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms,
                              Map<String, IBakedModel> cache)
        {
            this.quads = quads;
            this.particle = particle;
            this.format = format;
            this.parent = parent;
            this.transforms = transforms;
            this.cache = cache;
        }

        @Override
        public ItemOverrideList getOverrides()
        {
            return BakedOreBlockOverrideHandler.INSTANCE;
        }

        @Override
        public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType)
        {
            return IPerspectiveAwareModel.MapWrapper.handlePerspective(this, transforms, cameraTransformType);
        }

        @Override
        public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
        {
            if(side == null) return quads;
            return ImmutableList.of();
        }

        public boolean isAmbientOcclusion() { return true;  }
        public boolean isGui3d() { return false; }
        public boolean isBuiltInRenderer() { return false; }
        public TextureAtlasSprite getParticleTexture() { return particle; }
        public ItemCameraTransforms getItemCameraTransforms() { return ItemCameraTransforms.DEFAULT; }
    }

    private static final class BakedOreBlockOverrideHandler extends ItemOverrideList
    {
        public static final BakedOreBlockOverrideHandler INSTANCE = new BakedOreBlockOverrideHandler();
        private BakedOreBlockOverrideHandler() {
            super(ImmutableList.<ItemOverride>of());
        }

        @Override
        public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity)
        {
            ModelOreBlock.BakedModelOreBlock model = (ModelOreBlock.BakedModelOreBlock)originalModel;

            Ore ore = OreRegistry.getOre(Block.getBlockFromItem(stack.getItem()));

            String name = ore.getName();

            if (!model.cache.containsKey(name)) {
                IModel parent = model.parent.process(ImmutableMap.of("ore", name));
                Function<ResourceLocation, TextureAtlasSprite> textureGetter;
                textureGetter = new Function<ResourceLocation, TextureAtlasSprite>() {
                    public TextureAtlasSprite apply(ResourceLocation location) {
                        return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
                    }
                };

                IBakedModel bakedModel = parent.bake(new SimpleModelState(model.transforms), model.format, textureGetter);
                model.cache.put(name, bakedModel);
                return bakedModel;
            }

            return model.cache.get(name);
        }
    }
}
