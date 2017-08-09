package com.jozufozu.exnihiloomnia.client.renderers;

import com.jozufozu.exnihiloomnia.common.blocks.crucible.TileEntityCrucible;
import com.jozufozu.exnihiloomnia.common.util.Color;
import com.jozufozu.exnihiloomnia.common.util.MathStuff;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

public class TileEntityCrucibleRenderer extends TileEntitySpecialRenderer<TileEntityCrucible>
{
    private static EntityLivingBase renderSlave;
    
    private static final double width = 14.0 / 16.0;
    private static final double height = 3.0 / 16.0;
    
    @Override
    public boolean isGlobalRenderer(TileEntityCrucible te)
    {
        return true;
    }
    
    @Override
    public void render(TileEntityCrucible crucible, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        if (renderSlave == null)
            renderSlave = new EntityGuardian(getWorld());
        
        if (crucible.fluidAmount == 0 && crucible.solidAmount == 0)
            return;
        
        renderSolid(crucible, x, y, z, partialTicks);
        
        if (crucible.solidAmount != crucible.fluidAmount || crucible.solidAmount < TileEntityCrucible.crucibleCapacity)
            renderFluid(crucible, x, y, z, partialTicks);
    }
    
    public static void renderSolid(TileEntityCrucible crucible, double x, double y, double z, float partialTicks)
    {
        ItemStack contents = crucible.getSolidContents();
        
        if (contents.isEmpty())
            return;
        
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
    
        GlStateManager.translate(x + 0.5, y + height, z + 0.5);
        GlStateManager.scale(width, 1, width);
        
        float solid = (float) crucible.solidAmount / (float) TileEntityCrucible.crucibleCapacity;
        float solidLastTick = (float) crucible.solidAmountLastTick / (float) TileEntityCrucible.crucibleCapacity;
    
        float fullness = MathStuff.lerp(solidLastTick, solid, partialTicks);
        double contentsSize = 12.5 / 16.0 * fullness;
    
        GlStateManager.translate(0, contentsSize / 2.0, 0);
        GlStateManager.scale(1, contentsSize, 1);
    
        Minecraft.getMinecraft().getItemRenderer().renderItem(renderSlave, contents, ItemCameraTransforms.TransformType.NONE);
        
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
    
    public static void renderFluid(TileEntityCrucible crucible, double x, double y, double z, float partialTicks)
    {
        FluidStack fluidStack = crucible.getFluidContents();
        
        if (fluidStack == null)
            return;
        
        GlStateManager.pushMatrix();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableBlend();
        GlStateManager.disableCull();
    
        if (Minecraft.isAmbientOcclusionEnabled())
        {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
        }
        else
        {
            GlStateManager.shadeModel(GL11.GL_FLAT);
        }
    
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        TextureAtlasSprite fluidTexture = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(fluidStack.getFluid().getStill().toString());
    
        float fluid = (float) crucible.fluidAmount / (float) TileEntityCrucible.crucibleCapacity;
        float fluidLastTick = (float) crucible.fluidAmountLastTick / (float) TileEntityCrucible.crucibleCapacity;
        float fullness = MathStuff.lerp(fluid, fluidLastTick, partialTicks);
        double contentsSize = 12.5 / 16.0 * fullness;
    
        GlStateManager.translate(x + 1.0 / 16.0, y + height, z + 1.0 / 16.0);
        GlStateManager.scale(width, 1, width);
    
        GlStateManager.translate(0, contentsSize, 0);
        
        renderContents(fluidTexture, 0, Color.WHITE);
    
        GlStateManager.cullFace(GlStateManager.CullFace.BACK);
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        
        GlStateManager.popMatrix();
    }
    
    public static void renderContents(TextureAtlasSprite texture, double height, Color color)
    {
        GlStateManager.pushMatrix();
        RenderHelper.disableStandardItemLighting();
    
        double minU = (double) texture.getMinU();
        double maxU = (double) texture.getMaxU();
        double minV = (double) texture.getMinV();
        double maxV = (double) texture.getMaxV();
        
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder renderer = tessellator.getBuffer();
        
        renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        renderer.pos(1.0d, height, 1.0d).tex(maxU, maxV).color(color.r, color.g, color.b, color.a).endVertex();
        renderer.pos(1.0d, height, 0).tex(maxU, minV).color(color.r, color.g, color.b, color.a).endVertex();
        renderer.pos(0, height, 0).tex(minU, minV).color(color.r, color.g, color.b, color.a).endVertex();
        renderer.pos(0, height, 1.0d).tex(minU, maxV).color(color.r, color.g, color.b, color.a).endVertex();
        tessellator.draw();
    
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }
}
