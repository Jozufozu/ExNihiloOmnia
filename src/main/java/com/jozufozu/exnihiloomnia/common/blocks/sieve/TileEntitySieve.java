package com.jozufozu.exnihiloomnia.common.blocks.sieve;

import com.jozufozu.exnihiloomnia.advancements.ExNihiloTriggers;
import com.jozufozu.exnihiloomnia.client.ParticleSieve;
import com.jozufozu.exnihiloomnia.common.items.tools.IMesh;
import com.jozufozu.exnihiloomnia.common.registries.RegistryManager;
import com.jozufozu.exnihiloomnia.common.registries.recipes.SieveRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntitySieve extends TileEntity implements ITickable
{
    private ItemStackHandler itemHandler = new SieveItemHandler(2);
    
    public int requiredTime;
    
    public int countdown;
    public int countdownLastTick;
    
    private EntityPlayer user;
    private int workTimer;
    
    public void queueWork(EntityPlayer player, ItemStack held)
    {
        this.user = player;
        
        workTimer += 4;
    }
    
    @Override
    public void update()
    {
        countdownLastTick = countdown;
        
        if (requiredTime == 0)
            return;
        
        if (workTimer > 0)
        {
            workTimer--;
            countdown--;
    
            if (world.isRemote)
            {
                ItemStack mimic = itemHandler.getStackInSlot(0);
                for (int i = 0; i < world.rand.nextInt(10) + 25; i++)
                {
                    Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleSieve(world, mimic, pos));
                }
            }
            
            markDirty();
        }
        
        if (countdown <= 0)
        {
            if (!world.isRemote)
            {
                rollRewards();
                
                if (itemHandler.getStackInSlot(1).attemptDamageItem(1, world.rand, null))
                {
                    this.world.playSound(null, this.pos, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.BLOCKS, 0.8F, 0.8F + this.world.rand.nextFloat() * 0.4F);
                    
                    setStackInSlot(1, ItemStack.EMPTY);
                }
    
                SoundType soundType = blockSound(user);
                this.world.playSound(null, this.pos, soundType.getBreakSound(), SoundCategory.BLOCKS, 0.8F, soundType.getPitch() * 0.8f + this.world.rand.nextFloat() * 0.4F);
            }
            
            reset();
        }
    }
    
    /**
     * Takes the given item and checks to see if it can be sifted. If it can, it will decrease the stack by one and fill the sieve
     */
    public void insertContents(EntityPlayer user, ItemStack contents)
    {
        if (itemHandler.getStackInSlot(0) != ItemStack.EMPTY)
            return;
        
        if (RegistryManager.siftable(contents))
        {
            ItemStack insert = new ItemStack(contents.getItem(), 1, contents.getMetadata());
            
            if (user == null || !user.isCreative())
                contents.shrink(1);
            
            workTimer = 0;
            
            requiredTime = 0;
    
            for (SieveRecipe recipe : RegistryManager.SIFTING)
            {
                if (recipe.matches(insert))
                    requiredTime = Math.max(requiredTime, recipe.getSiftTime());
            }
            
            countdownLastTick = 0;
            countdown = requiredTime;
            
            setStackInSlot(0, insert);
    
            SoundEvent soundEvent = SoundEvents.BLOCK_GRAVEL_HIT;
    
            ItemStack sifting = itemHandler.getStackInSlot(0);
            Block block = Block.getBlockFromItem(sifting.getItem());
    
            if (block != Blocks.AIR)
                soundEvent = block.getSoundType(block.getStateFromMeta(sifting.getMetadata()), world, pos, user).getPlaceSound();
            
            world.playSound(null, pos, soundEvent, SoundCategory.BLOCKS, 0.4f, 0.75f + world.rand.nextFloat() * 0.1f);
        }
    }
    
    public boolean hasContents()
    {
        return itemHandler.getStackInSlot(0) != ItemStack.EMPTY;
    }
    
    public ItemStack getContents()
    {
        return itemHandler.getStackInSlot(0);
    }
    
    public void trySetMesh(EntityPlayer user, ItemStack mesh)
    {
        if (!(mesh.getItem() instanceof IMesh))
            return;
        
        ItemStack toInsert = mesh.copy();
        
        if (user != null)
            user.inventory.deleteStack(mesh);
        
        setStackInSlot(1, toInsert);
    }
    
    public void removeMesh(EntityPlayer user)
    {
        ItemHandlerHelper.giveItemToPlayer(user,itemHandler.getStackInSlot(1));
        
        setStackInSlot(1, ItemStack.EMPTY);
    }
    
    public boolean hasMesh()
    {
        return itemHandler.getStackInSlot(1) != ItemStack.EMPTY;
    }
    
    public ItemStack getMesh()
    {
        return itemHandler.getStackInSlot(1);
    }
    
    public SoundType blockSound(EntityPlayer user)
    {
        SoundType soundEvent = SoundType.GROUND;
    
        ItemStack sifting = itemHandler.getStackInSlot(0);
        Block block = Block.getBlockFromItem(sifting.getItem());
        
        if (block != Blocks.AIR)
            soundEvent = block.getSoundType(block.getStateFromMeta(sifting.getMetadata()), world, pos, user);
        
        return soundEvent;
    }
    
    public void rollRewards()
    {
        markDirty();
    
        NonNullList<ItemStack> drops = NonNullList.create();
    
        ItemStack contents = getContents();
        ItemStack mesh = getMesh();
        
        for (SieveRecipe recipe : RegistryManager.SIFTING)
        {
            if (recipe.matches(contents))
            {
                NonNullList<ItemStack> roll = recipe.getOutput().roll(this.user, mesh, world.rand);
                
                if (user instanceof EntityPlayerMP)
                    ExNihiloTriggers.USE_SIEVE_TRIGGER.trigger((EntityPlayerMP) user, recipe.getRegistryName(), roll);
                
                drops.addAll(roll);
            }
        }
        
        for (ItemStack drop : drops)
        {
            double posX = world.rand.nextDouble() * 0.75 + 0.125;
            double posZ = world.rand.nextDouble() * 0.75 + 0.125;
            
            EntityItem entityitem = new EntityItem(world, pos.getX() + posX, pos.getY() + 1.0d, pos.getZ() + posZ, drop);
            
            double motionMag = 0.08;
            
            entityitem.motionX = 0.5 * (world.rand.nextFloat() * motionMag * 2 - motionMag);
            entityitem.motionY = world.rand.nextFloat() * motionMag * 1.6;
            entityitem.motionZ = 0.5 * (world.rand.nextFloat() * motionMag * 2 - motionMag);
            
            entityitem.setDefaultPickupDelay();
            world.spawnEntity(entityitem);
        }
    }
    
    public void reset()
    {
        workTimer = 0;
        countdown = 0;
        countdownLastTick = 0;
        requiredTime = 0;
    
        setStackInSlot(0, ItemStack.EMPTY);
    }
    
    public void setStackInSlot(int slot, ItemStack stack)
    {
        itemHandler.setStackInSlot(slot, stack);
    
    
        IBlockState blockState = this.world.getBlockState(this.getPos());
        this.world.notifyBlockUpdate(this.getPos(), blockState, blockState, 3);
        markDirty();
    }
    
    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }
    
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(getPos(), 1, this.getUpdateTag());
    }
    
    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        this.readFromNBT(packet.getNbtCompound());
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setTag("inventory", itemHandler.serializeNBT());
        compound.setInteger("countdown", countdown);
        compound.setInteger("requiredTime", requiredTime);
        return super.writeToNBT(compound);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        itemHandler.deserializeNBT(compound.getCompoundTag("inventory"));
        countdownLastTick = countdown = compound.getInteger("countdown");
        requiredTime = compound.getInteger("requiredTime");
    }
    
    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }
    
    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T) itemHandler : super.getCapability(capability, facing);
    }
    
    private class SieveItemHandler extends ItemStackHandler
    {
        public SieveItemHandler(int size)
        {
            super(size);
        }
    
        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
        {
            if (slot == 1 && !(stack.getItem() instanceof IMesh))
                return stack;
        
            if (slot == 0 && !RegistryManager.siftable(stack))
                return stack;
            
            return super.insertItem(slot, stack, simulate);
        }
    
        @Override
        protected int getStackLimit(int slot, @Nonnull ItemStack stack)
        {
            return slot == 0 ? 1 : super.getStackLimit(slot, stack);
        }
    }
}
