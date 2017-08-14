package com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.states;

import com.jozufozu.exnihiloomnia.client.RenderUtil;
import com.jozufozu.exnihiloomnia.common.ModConfig;
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.BarrelLogic;
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.BarrelState;
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.BarrelStates;
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.TileEntityBarrel;
import com.jozufozu.exnihiloomnia.common.util.MathStuff;
import com.jozufozu.exnihiloomnia.proxy.ClientProxy;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;

public class BarrelStateComposting extends BarrelState
{
    public BarrelStateComposting()
    {
        super(BarrelStates.ID_COMPOSTING);
        this.logic.add(new CompostLogic());
    }
    
    @Override
    public void draw(TileEntityBarrel barrel, double x, double y, double z, float partialTicks)
    {
        super.draw(barrel, x, y, z, partialTicks);
    
        float timerNow = ((float) (ModConfig.blocks.barrel.compostTime - barrel.timer)) / ((float) ModConfig.blocks.barrel.compostTime);
        float timerLast = ((float) (ModConfig.blocks.barrel.compostTime - barrel.timerLastTick)) / ((float) ModConfig.blocks.barrel.compostTime);
        
        float progress = MathStuff.lerp(timerLast, timerNow, partialTicks);
    
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        
        ItemStack contents = barrel.getItem();
    
        if (contents.isEmpty())
            return;
    
        GlStateManager.pushMatrix();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableCull();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
    
        if (Minecraft.isAmbientOcclusionEnabled())
        {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
        }
        else
        {
            GlStateManager.shadeModel(GL11.GL_FLAT);
        }
    
        GlStateManager.translate(x + 0.5, y + 0.0625, z + 0.5);
    
        if (Block.getBlockFromItem(contents.getItem()) != Blocks.AIR)
        {
            GlStateManager.translate(0, 0.4375, 0);
            GlStateManager.scale(0.75, 0.875, 0.75);
        }
        else
        {
            GlStateManager.translate(0, 0.03125, 0);
            GlStateManager.scale(0.75, 0.75, 0.75);
            GlStateManager.rotate(90f, 1, 0, 0);
        }
    
        GlStateManager.scale(0.9999, 0.9999, 0.9999);
        Minecraft.getMinecraft().getItemRenderer().renderItem(renderSlave, contents, ItemCameraTransforms.TransformType.NONE);
    
        RenderHelper.enableStandardItemLighting();
    
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    
        GlStateManager.pushMatrix();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableCull();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
    
        if (Minecraft.isAmbientOcclusionEnabled())
        {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
        }
        else
        {
            GlStateManager.shadeModel(GL11.GL_FLAT);
        }
    
        GlStateManager.translate(x + 0.125, y + 0.0625, z + 0.125);
        GlStateManager.scale(0.75, 1, 0.75);
    
        RenderHelper.disableStandardItemLighting();
        if (barrel.getWorld().getBlockState(barrel.getPos()).getMaterial().isOpaque())
        {
            RenderUtil.renderContents(ClientProxy.COMPOST, 0.875, barrel.color.withAlpha(1.0f - progress));
        }
        else
        {
            RenderUtil.renderContents3D(ClientProxy.COMPOST, ClientProxy.COMPOST, 0.875, barrel.color.withAlpha(1.0f - progress));
        }
        
        GlStateManager.popMatrix();
    }
    
    public static class CompostLogic extends BarrelLogic
    {
        @Override
        public void onActivate(TileEntityBarrel barrel, @Nullable BarrelState previousState)
        {
            barrel.timer = ModConfig.blocks.barrel.compostTime;
        }
    
        @Override
        public boolean onUpdate(TileEntityBarrel barrel)
        {
            barrel.timer--;
            if (!barrel.getWorld().isRemote)
            {
                if (barrel.timer <= 0)
                {
                    barrel.setState(BarrelStates.ITEMS);
                }
            }
            return false;
        }
    }
}
