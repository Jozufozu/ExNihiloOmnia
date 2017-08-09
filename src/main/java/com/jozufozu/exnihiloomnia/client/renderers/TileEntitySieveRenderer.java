package com.jozufozu.exnihiloomnia.client.renderers;

import com.jozufozu.exnihiloomnia.common.blocks.sieve.TileEntitySieve;
import com.jozufozu.exnihiloomnia.common.util.MathStuff;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class TileEntitySieveRenderer extends TileEntitySpecialRenderer<TileEntitySieve>
{
    private static EntityLivingBase renderSlave;
    
    private static final double scale = 14.0 / 16.0;
    private static final double height = 9.0 / 16.0;
    
    @Override
    public void render(TileEntitySieve sieve, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        IItemHandler itemHandler = sieve.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
    
        if (itemHandler == null)
            return;
    
        ItemStack contents = itemHandler.getStackInSlot(0);
        ItemStack mesh = itemHandler.getStackInSlot(1);
    
        //if (mesh == ItemStack.EMPTY)
        //    return;
    
        if (renderSlave == null)
            renderSlave = new EntityGuardian(getWorld());
    
        GlStateManager.pushMatrix();
        
        GlStateManager.translate(x + 0.5, y + height, z + 0.5);
        GlStateManager.scale(scale, 0.2, scale);
        GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
        
        Minecraft.getMinecraft().getItemRenderer().renderItem(renderSlave, mesh, ItemCameraTransforms.TransformType.NONE);
    
        if (contents != ItemStack.EMPTY)
        {
            double progress = MathStuff.lerp(((double) sieve.countdownLastTick) / sieve.requiredTime, ((double) sieve.countdown) / sieve.requiredTime, partialTicks);
    
            double contentsSize = 7.0 / 16.0 * progress;
    
            GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f);
    
            GlStateManager.scale(1, 5, 1);
            GlStateManager.translate(0, contentsSize / 2.0, 0);
            GlStateManager.scale(1, contentsSize, 1);
    
            Minecraft.getMinecraft().getItemRenderer().renderItem(renderSlave, contents, ItemCameraTransforms.TransformType.NONE);
        }
        
        GlStateManager.popMatrix();
    }
}
