package exnihiloomnia.blocks.leaves;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

import net.minecraft.util.EnumFacing;

import exnihiloomnia.util.Color;

public class InfestedLeavesRenderer extends TileEntitySpecialRenderer<TileEntityInfestedLeaves>{

    @Override
    public void renderTileEntityAt(TileEntityInfestedLeaves te, double x, double y, double z, float partialTicks, int destroyStage)
    {
        if (te.getBlock() != null) {
            if (te.model.size() == 0) {
                IBlockState blockState = te.block.getStateFromMeta(te.meta);
                IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(blockState);
                for (EnumFacing face : EnumFacing.VALUES)
                    te.model.addAll(model.getQuads(blockState, face, 0));
            }
            Color color = te.getRenderColor();

            GlStateManager.pushMatrix();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.bindTexture(Minecraft.getMinecraft().getTextureMapBlocks().getGlTextureId());

            GlStateManager.translate(x, y, z);
            GlStateManager.scale(1.0D, 1.0D, 1.0D);

            VertexBuffer vertexbuffer = Tessellator.getInstance().getBuffer();
            vertexbuffer.begin(7, DefaultVertexFormats.ITEM);
            for (BakedQuad quad : te.model) {
                vertexbuffer.addVertexData(quad.getVertexData());
                vertexbuffer.putColorRGB_F4(color.r, color.g, color.b);
            }

            Tessellator.getInstance().draw();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.popMatrix();
        }
    }
}

