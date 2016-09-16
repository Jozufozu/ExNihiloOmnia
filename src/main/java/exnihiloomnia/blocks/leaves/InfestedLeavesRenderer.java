package exnihiloomnia.blocks.leaves;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

import net.minecraft.util.EnumFacing;

import exnihiloomnia.util.Color;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InfestedLeavesRenderer extends TileEntitySpecialRenderer<TileEntityInfestedLeaves> {
    //private static HashMap<IBlockState, List<BakedQuad>> models = new HashMap<IBlockState, List<BakedQuad>>();

    @Override
    public void renderTileEntityAt(TileEntityInfestedLeaves te, double x, double y, double z, float partialTicks, int destroyStage) {
        if (te.getBlock() != null) {
            IBlockState blockState = te.block.getStateFromMeta(te.meta);

            //if (!models.containsKey(blockState)) {
                IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(blockState);
                List<BakedQuad> temp = new ArrayList<BakedQuad>();

                for (EnumFacing face : EnumFacing.VALUES) {
                    if (te.getBlock().shouldSideBeRendered(blockState, te.getWorld(), te.getPos(), face))
                        temp.addAll(model.getQuads(blockState, face, 0));
                }
                //models.put(blockState, temp);
            //}
            Color color = te.getRenderColor();

            GlStateManager.pushMatrix();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.bindTexture(Minecraft.getMinecraft().getTextureMapBlocks().getGlTextureId());

            GlStateManager.translate(x, y, z);
            GlStateManager.scale(1.0D, 1.0D, 1.0D);

            VertexBuffer vertexbuffer = Tessellator.getInstance().getBuffer();
            vertexbuffer.begin(7, DefaultVertexFormats.ITEM);
            for (BakedQuad quad : temp) {
                vertexbuffer.addVertexData(quad.getVertexData());
                vertexbuffer.putColorRGB_F4(color.r, color.g, color.b);
            }

            Tessellator.getInstance().draw();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.popMatrix();
        }
    }
}

