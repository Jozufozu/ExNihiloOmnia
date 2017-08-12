package com.jozufozu.exnihiloomnia.common.items.tools;

import com.jozufozu.exnihiloomnia.ExNihilo;
import com.jozufozu.exnihiloomnia.common.items.ItemBaseTool;
import com.jozufozu.exnihiloomnia.common.registries.RegistryManager;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = ExNihilo.MODID)
public class ItemHammer extends ItemBaseTool
{
    public ItemHammer(ResourceLocation registryName, ToolMaterial toolMaterial)
    {
        super(registryName, toolMaterial);
    }
    
    private static List<BlockPos> hammeredPositions = new ArrayList<>();
    
    @SubscribeEvent
    public static void onBreak(BlockEvent.HarvestDropsEvent event)
    {
        if (hammeredPositions.contains(event.getPos()))
        {
            event.getDrops().clear();
            hammeredPositions.remove(event.getPos());
        }
    }
    
    @Override
    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player)
    {
        World world = player.getEntityWorld();
        
        if (world.isRemote || player.isCreative())
            return false;
        
        IBlockState blockState = world.getBlockState(pos);
        
        if (!RegistryManager.hammerable(blockState))
        {
            return false;
        }
        
        hammeredPositions.add(pos.toImmutable());

        Random rand = new Random();
        
        for (ItemStack drop : RegistryManager.getHammerRewards(world, stack, player, blockState))
        {
            AxisAlignedBB blockBox = blockState.getBoundingBox(world ,pos);
    
            blockBox = blockBox.offset(pos).shrink(0.125).offset(0, -0.125, 0);
            
            double xOff  = rand.nextDouble() * (blockBox.maxX - blockBox.minX);
            double yOff = rand.nextDouble() * (blockBox.maxY - blockBox.minY);
            double zOff = rand.nextDouble() * (blockBox.maxZ - blockBox.minZ);
            
            EntityItem entityitem = new EntityItem(player.world, blockBox.minX + xOff, blockBox.minY + yOff, blockBox.minZ + zOff, drop);
            
            entityitem.setDefaultPickupDelay();
            player.world.spawnEntity(entityitem);
        }
        return false;
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
        return RegistryManager.hammerable(blockIn);
    }
    
    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass, @Nullable EntityPlayer player, @Nullable IBlockState blockState)
    {
        return RegistryManager.hammerable(blockState) ? toolMaterial.getHarvestLevel() : -1;
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
