package com.jozufozu.exnihiloomnia.common.blocks.barrel.logic;

import com.google.common.collect.HashBiMap;
import com.jozufozu.exnihiloomnia.client.RenderUtil;
import com.jozufozu.exnihiloomnia.common.util.Color;
import com.jozufozu.exnihiloomnia.common.util.MathStuff;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BarrelState
{
    public static final HashBiMap<ResourceLocation, BarrelState> STATES = HashBiMap.create();
    
    public static EntityLivingBase renderSlave;
    
    protected final ResourceLocation id;
    protected List<BarrelLogic> logic = new ArrayList<>();
    
    public BarrelState(ResourceLocation id)
    {
        this.id = id;
        STATES.put(id, this);
    }
    
    public ResourceLocation getID()
    {
        return id;
    }
    
    public boolean canInteractWithFluids(TileEntityBarrel barrel)
    {
        return true;
    }
    
    public boolean canInteractWithItems(TileEntityBarrel barrel)
    {
        return true;
    }
    
    public boolean canExtractItems(TileEntityBarrel barrel)
    {
        return false;
    }
    
    public boolean canExtractFluids(TileEntityBarrel barrel)
    {
        return false;
    }
    
    public void activate(TileEntityBarrel barrel, @Nullable BarrelState previousState)
    {
        for (BarrelLogic barrelLogic : logic)
        {
            barrelLogic.onActivate(barrel, previousState);
        }
    }
    
    public void update(TileEntityBarrel barrel)
    {
        for (BarrelLogic barrelLogic : logic)
        {
            if (barrelLogic.onUpdate(barrel))
            {
                return;
            }
        }
    }
    
    public boolean canUseItem(TileEntityBarrel barrel, @Nullable EntityPlayer player, @Nullable EnumHand hand, ItemStack itemStack)
    {
        for (BarrelLogic barrelLogic : logic)
        {
            if (barrelLogic.canUseItem(barrel, player, hand, itemStack))
            {
                return true;
            }
        }
        return false;
    }
    
    public EnumInteractResult onUseItem(TileEntityBarrel barrel, @Nullable EntityPlayer player, @Nullable EnumHand hand, ItemStack itemStack)
    {
        for (BarrelLogic barrelLogic : logic)
        {
            EnumInteractResult interactResult = barrelLogic.onUseItem(barrel, player, hand, itemStack);
            if (interactResult != EnumInteractResult.PASS)
            {
                barrel.sync(true);
                return interactResult;
            }
        }
        
        return EnumInteractResult.PASS;
    }
    
    public boolean canFillFluid(TileEntityBarrel barrel, FluidStack fluidStack)
    {
        for (BarrelLogic barrelLogic : logic)
        {
            if (barrelLogic.canFillFluid(barrel, fluidStack))
            {
                return true;
            }
        }
        return false;
    }
    
    public EnumInteractResult onFillFluid(TileEntityBarrel barrel, FluidStack fluidStack)
    {
        for (BarrelLogic barrelLogic : logic)
        {
            EnumInteractResult interactResult = barrelLogic.onFillFluid(barrel, fluidStack);
            if (interactResult != EnumInteractResult.PASS)
            {
                barrel.sync(true);
                return interactResult;
            }
        }
        
        return EnumInteractResult.PASS;
    }
    
    @SideOnly(Side.CLIENT)
    public void draw(TileEntityBarrel barrel, double x, double y, double z, float partialTicks)
    {
        if (renderSlave == null)
        {
            renderSlave = new EntityEndermite(barrel.getWorld());
            renderSlave.setInvisible(true);
            renderSlave.setSilent(true);
            renderSlave.noClip = true;
        }
    }
    
    @SideOnly(Side.CLIENT)
    public static void renderFluid(TileEntityBarrel barrel, double x, double y, double z, float partialTicks)
    {
        FluidStack fluidStack = barrel.getFluid();
    
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
        TextureMap textureMapBlocks = Minecraft.getMinecraft().getTextureMapBlocks();
        
        TextureAtlasSprite fluidTexture = textureMapBlocks.getAtlasSprite(fluidStack.getFluid().getStill(fluidStack).toString());
    
        float fluid = (float) barrel.fluidAmount / (float) barrel.fluidCapacity;
        float fluidLastTick = (float) barrel.fluidAmountLastTick / (float) barrel.fluidCapacity;
        float fullness = MathStuff.lerp(fluid, fluidLastTick, partialTicks);
        double contentsSize = 0.875 * fullness;
    
        GlStateManager.translate(x + 0.125, y + 0.0625, z + 0.125);
        GlStateManager.scale(0.75, 1, 0.75);
    
        RenderHelper.disableStandardItemLighting();
        
        if (barrel.getWorld().getBlockState(barrel.getPos()).getMaterial().isOpaque())
        {
            RenderUtil.renderContents(fluidTexture, contentsSize, new Color(fluidStack.getFluid().getColor(fluidStack)));
        }
        else
        {
            //TextureAtlasSprite fluidFlow = textureMapBlocks.getAtlasSprite(fluidStack.getFluid().getFlowing(fluidStack).toString());
            RenderUtil.renderContents3D(fluidTexture, fluidTexture, contentsSize, new Color(fluidStack.getFluid().getColor(fluidStack)));
        }
        
        
        RenderHelper.enableStandardItemLighting();
    
        GlStateManager.cullFace(GlStateManager.CullFace.BACK);
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
    
        GlStateManager.popMatrix();
    }
    
    @SideOnly(Side.CLIENT)
    public static void drawContents(TileEntityBarrel barrel, double x, double y, double z, float partialTicks)
    {
        ItemStack contents = barrel.getItem();
    
        if (contents.isEmpty())
            return;
    
        GlStateManager.pushMatrix();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
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
        
        Minecraft.getMinecraft().getItemRenderer().renderItem(renderSlave, contents, ItemCameraTransforms.TransformType.NONE);
    
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
}
