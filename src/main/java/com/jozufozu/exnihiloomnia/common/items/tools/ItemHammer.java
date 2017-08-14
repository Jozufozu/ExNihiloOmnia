package com.jozufozu.exnihiloomnia.common.items.tools;

import com.jozufozu.exnihiloomnia.ExNihilo;
import com.jozufozu.exnihiloomnia.common.items.ItemBaseTool;
import com.jozufozu.exnihiloomnia.common.registries.RegistryManager;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.Random;

@Mod.EventBusSubscriber(modid = ExNihilo.MODID)
public class ItemHammer extends ItemBaseTool
{
    public ItemHammer(ResourceLocation registryName, ToolMaterial toolMaterial)
    {
        super(registryName, toolMaterial);
    }
    
    @SubscribeEvent
    public static void onBreak(BlockEvent.HarvestDropsEvent event)
    {
        EntityPlayer player = event.getHarvester();
        
        if (player == null || !(player.getActiveItemStack().getItem() instanceof ItemHammer))
        {
            return;
        }
        
        World world = player.getEntityWorld();
    
        if (world.isRemote || player.isCreative())
            return;
    
        BlockPos pos = event.getPos();
        IBlockState blockState = world.getBlockState(pos);
    
        if (!RegistryManager.hammerable(blockState))
        {
            return;
        }
        
        event.getDrops().clear();
        Random rand = new Random();
    
        for (ItemStack drop : RegistryManager.getHammerRewards(world, player.getActiveItemStack(), player, blockState))
        {
            AxisAlignedBB blockBox = blockState.getBoundingBox(world, pos);
        
            blockBox = blockBox.offset(pos).shrink(0.125).offset(0, -0.125, 0);
        
            double xOff  = rand.nextDouble() * (blockBox.maxX - blockBox.minX);
            double yOff = rand.nextDouble() * (blockBox.maxY - blockBox.minY);
            double zOff = rand.nextDouble() * (blockBox.maxZ - blockBox.minZ);
        
            EntityItem entityitem = new EntityItem(player.world, blockBox.minX + xOff, blockBox.minY + yOff, blockBox.minZ + zOff, drop);
        
            entityitem.setDefaultPickupDelay();
            player.world.spawnEntity(entityitem);
        }
    }
    
    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        //Hammers have a built-in knockback effect
        Vec3d lookVec = attacker.getLookVec();
        target.knockBack(attacker, 1, -lookVec.x, -lookVec.z);
        return super.hitEntity(stack, target, attacker);
    }
    
    @Override
    public boolean canHarvestBlock(IBlockState blockIn)
    {
        Block block = blockIn.getBlock();
    
        if (block == Blocks.OBSIDIAN)
        {
            return this.toolMaterial.getHarvestLevel() == 3;
        }
        else if (block != Blocks.DIAMOND_BLOCK && block != Blocks.DIAMOND_ORE)
        {
            if (block != Blocks.EMERALD_ORE && block != Blocks.EMERALD_BLOCK)
            {
                if (block != Blocks.GOLD_BLOCK && block != Blocks.GOLD_ORE)
                {
                    if (block != Blocks.IRON_BLOCK && block != Blocks.IRON_ORE)
                    {
                        if (block != Blocks.LAPIS_BLOCK && block != Blocks.LAPIS_ORE)
                        {
                            if (block != Blocks.REDSTONE_ORE && block != Blocks.LIT_REDSTONE_ORE)
                            {
                                Material material = blockIn.getMaterial();
                            
                                if (material == Material.ROCK)
                                {
                                    return true;
                                }
                                else if (material == Material.IRON)
                                {
                                    return true;
                                }
                                else
                                {
                                    return material == Material.ANVIL;
                                }
                            }
                            else
                            {
                                return this.toolMaterial.getHarvestLevel() >= 2;
                            }
                        }
                        else
                        {
                            return this.toolMaterial.getHarvestLevel() >= 1;
                        }
                    }
                    else
                    {
                        return this.toolMaterial.getHarvestLevel() >= 1;
                    }
                }
                else
                {
                    return this.toolMaterial.getHarvestLevel() >= 2;
                }
            }
            else
            {
                return this.toolMaterial.getHarvestLevel() >= 2;
            }
        }
        else
        {
            return this.toolMaterial.getHarvestLevel() >= 2;
        }
    }
    
    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass, @Nullable EntityPlayer player, @Nullable IBlockState blockState)
    {
        return RegistryManager.hammerable(blockState) ? this.toolMaterial.getHarvestLevel() : -1;
    }
    
    @Override
    public float getStrVsBlock(ItemStack stack, IBlockState state)
    {
        return RegistryManager.hammerable(state) ? this.efficiencyOnProperMaterial : super.getStrVsBlock(stack, state);
    }
    
    @Override
    public boolean canDisableShield(ItemStack stack, ItemStack shield, EntityLivingBase entity, EntityLivingBase attacker)
    {
        return true;
    }
}
