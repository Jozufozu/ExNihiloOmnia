package exnihiloomnia.blocks.leaves;

import exnihiloomnia.util.Color;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InfestedLeavesRenderer extends TileEntitySpecialRenderer<TileEntityInfestedLeaves> {

    public static HashMap<IBlockState, List<BakedQuad>> models = new HashMap<>();

    @Override
    public void renderTileEntityAt(TileEntityInfestedLeaves te, double x, double y, double z, float partialTicks, int destroyStage) {
        Minecraft mc = Minecraft.getMinecraft();
        World world = getWorld();
        BlockPos pos = te.getPos();

        if (te.getState() != null) {
            mc.mcProfiler.startSection("renderLeaves");
            IBlockState state = te.state;

            if (!models.containsKey(state)) {
                IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state);
                List<BakedQuad> temp = new ArrayList<BakedQuad>();

                for (EnumFacing face : EnumFacing.VALUES)
                    temp.addAll(model.getQuads(state, face, 0));

                models.put(state, temp);
            }

            GlStateManager.pushMatrix();
            RenderHelper.disableStandardItemLighting();

            this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

            GlStateManager.translate(x, y, z);
            GlStateManager.scale(1.0D, 1.0D, 1.0D);

            VertexBuffer vertexbuffer = Tessellator.getInstance().getBuffer();
            vertexbuffer.begin(7, DefaultVertexFormats.ITEM);

            for (BakedQuad quad : models.get(state)) {
                if (state.shouldSideBeRendered(world, pos, quad.getFace())) {
                    vertexbuffer.addVertexData(quad.getVertexData());

                    mc.mcProfiler.startSection("colorMath");

                    Color color = te.getColorForTint(quad.getTintIndex());

                    vertexbuffer.putColorMultiplier(color.r, color.g, color.b, 1);

                    mc.mcProfiler.endSection();
                }
            }

            Tessellator.getInstance().draw();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.popMatrix();
            mc.mcProfiler.endSection();
        }
    }
}
