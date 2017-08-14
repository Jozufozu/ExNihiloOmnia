package com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.states;

import com.jozufozu.exnihiloomnia.client.RenderUtil;
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.*;
import com.jozufozu.exnihiloomnia.common.registries.RegistryManager;
import com.jozufozu.exnihiloomnia.common.registries.recipes.CompostRecipe;
import com.jozufozu.exnihiloomnia.common.util.Color;
import com.jozufozu.exnihiloomnia.common.util.MathStuff;
import com.jozufozu.exnihiloomnia.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;

public class BarrelStateCompostCollect extends BarrelState
{
    public BarrelStateCompostCollect()
    {
        super(BarrelStates.ID_COMPOST_COLLECT);
        
        this.logic.add(new CompostCollectionLogic());
    }
    
    @Override
    public boolean canInteractWithItems(TileEntityBarrel barrel)
    {
        return true;
    }
    
    @Override
    public boolean canInteractWithFluids(TileEntityBarrel barrel)
    {
        return false;
    }
    
    @Override
    public void draw(TileEntityBarrel barrel, double x, double y, double z, float partialTicks)
    {
        super.draw(barrel, x, y, z, partialTicks);
        
        double height = 0.875 * MathStuff.lerp(((double) barrel.compostAmountLastTick) / ((double) barrel.compostCapacity), ((double) barrel.compostAmount) / ((double) barrel.compostCapacity), partialTicks);
    
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        
        GlStateManager.pushMatrix();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableBlend();
        GlStateManager.enableCull();
    
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
            RenderUtil.renderContents(ClientProxy.COMPOST, height, barrel.color);
        }
        else
        {
            RenderUtil.renderContents3D(ClientProxy.COMPOST, ClientProxy.COMPOST, height, barrel.color);
        }
        RenderHelper.enableStandardItemLighting();
    
    
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        
        GlStateManager.popMatrix();
    }
    
    public static class CompostCollectionLogic extends BarrelLogic
    {
        @Override
        public boolean canUseItem(TileEntityBarrel barrel, @Nullable EntityPlayer player, @Nullable EnumHand hand, ItemStack itemStack)
        {
            return RegistryManager.getCompost(itemStack) != null;
        }
    
        @Override
        public EnumInteractResult onUseItem(TileEntityBarrel barrel, @Nullable EntityPlayer player, @Nullable EnumHand hand, ItemStack itemStack)
        {
            CompostRecipe compostRecipe = RegistryManager.getCompost(itemStack);
            if (compostRecipe != null)
            {
                if (!barrel.getWorld().isRemote)
                {
                    if (barrel.getItem().isEmpty())
                    {
                        barrel.setItem(compostRecipe.getOutput());
                    }
                    else if (!ItemStack.areItemStacksEqual(barrel.getItem(), compostRecipe.getOutput()))
                    {
                        return EnumInteractResult.PASS;
                    }
                    
                    if (barrel.color == null)
                    {
                        barrel.color = compostRecipe.getColor();
                    }
                    else
                    {
                        float weight = ((float) barrel.compostAmount) / ((float) (barrel.compostAmount + compostRecipe.getAmount()));
                        barrel.color = Color.weightedAverage(compostRecipe.getColor(), barrel.color, weight);
                    }
    
                    barrel.compostAmount += compostRecipe.getAmount();
                    barrel.compostAmount = Math.min(barrel.compostAmount, barrel.compostCapacity);
                    
                    if (barrel.compostAmount == barrel.compostCapacity)
                    {
                        barrel.setState(BarrelStates.COMPOSTING);
                    }
                }
                return EnumInteractResult.CONSUME;
            }
            
            return EnumInteractResult.PASS;
        }
    }
}
