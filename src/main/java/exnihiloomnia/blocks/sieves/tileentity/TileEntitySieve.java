package exnihiloomnia.blocks.sieves.tileentity;

import exnihiloomnia.ENOConfig;
import exnihiloomnia.blocks.ENOBlocks;
import exnihiloomnia.blocks.automation.tile.TileSifter;
import exnihiloomnia.client.particles.ParticleSieve;
import exnihiloomnia.items.meshs.ISieveMesh;
import exnihiloomnia.items.meshs.ItemMesh;
import exnihiloomnia.registries.sifting.SieveRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class TileEntitySieve extends TileEntity implements ITickable, IInventory {
	protected ItemStack mesh;
	protected ItemStack contents;
	protected IBlockState contentsState;

    protected boolean hasSifter;
	
	protected int work = 0;
	protected int workMax = 1000;
	protected int workSpeed = 30;
	
	protected int updateTimer = 0;
	protected int updateTimerMax = 2; //Sync if an update is required.
	protected boolean updateQueued = false;
	protected boolean updateTimerRunning = false;
	
	protected int workThisCycle = 0;
	protected int workPerCycleLimit = 120;
	protected int workCycleTimer = 0;
	protected int workCycleTimerMax = 20;
	
	protected boolean spawningParticles = false;
	protected int spawnParticlesTimer = 0;
	protected int spawnParticlesTimerMax = 5;
	
	@Override
	public void update() 
	{
		if (!this.worldObj.isRemote)
		{
			//TileEntity sifter = worldObj.getTileEntity(pos.down());
            //hasSifter = sifter != null && sifter instanceof TileSifter;

			//Speed throttling.
			workCycleTimer++;

			if (workCycleTimer > workCycleTimerMax)
			{
				workThisCycle = 0;
				workCycleTimer = 0;
			}

			//Packet throttling
			if (updateTimerRunning)
			{
				updateTimer++;

				if (updateTimer > updateTimerMax)
				{
					updateTimer = 0;
					if (updateQueued)
					{
						updateQueued = false;
                        getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);

					}
					else
					{
						updateTimerRunning = false;
					}
				}
			}
		}
		else
		{
			if (spawningParticles)
			{
				generateParticles(contentsState);
				
				spawnParticlesTimer++;
				if (spawnParticlesTimer > spawnParticlesTimerMax)
				{
					spawningParticles = false;
				}
			}
		}
	}

	//Send update packets to each client.
	public void sync()
	{
		if (getWorld() != null && !getWorld().isRemote)
		{
			if (!updateTimerRunning)
			{
				updateTimerRunning = true;
				getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
			}
			else
			{
				this.updateQueued = true;
			}
		}
	}

    public boolean hasSifter() {
        return hasSifter;
    }
	
	public boolean hasMesh()
	{
		return this.mesh != null;
	}
	
	public ItemStack getMesh()
	{
		return this.mesh;
	}
	
	public void setMesh(ItemStack mesh)
	{
		this.mesh = mesh;
		sync();
	}
	
	public ItemStack getContents()
	{
		return contents;
	}
	
	public void setContents(ItemStack input)
	{
		this.contents = input;
		
		if (contents != null)
		{
			Block block = Block.getBlockFromItem(contents.getItem());

			if (block != null)
			{
				contentsState = block.getBlockState().getBaseState();
			}
		}
		else
		{
			contentsState = null;
		}
		
		sync();
	}
	
	public boolean canWork()
	{
		return this.contents != null;
	}
	
	public void doWork()
	{
        this.spawningParticles = true;
        addThrottledWork(workSpeed);
		if (!this.worldObj.isRemote)
		{
			if (work > workMax)
			{
				if (contentsState != null)
				{
					for (ItemStack i : SieveRegistry.generateRewards(contentsState))
					{
						EntityItem entityitem = new EntityItem(getWorld(), pos.getX() + 0.5f, pos.up().getY() + 0.5f, pos.getZ() + 0.5f, i);

						entityitem.motionX = getWorld().rand.nextGaussian() * 0.05F;
						entityitem.motionY = (0.2d);
						entityitem.motionZ = getWorld().rand.nextGaussian() * 0.05F;
						entityitem.setDefaultPickupDelay();
						
						getWorld().spawnEntityInWorld(entityitem);
					}
				}
				
				work = 0;
				contents = null;
				
				if (this.mesh != null)
				{
					if (mesh.attemptDamageItem(1, worldObj.rand))
					{
						getWorld().playSound(null, pos, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.BLOCKS, 0.5f, 2.5f);
						setMesh(null);
					}
				}
			}
			
			sync();
			markDirty();
		}
	}

	public void setWorkSpeed(int speed) {this.workSpeed = speed;}
	
	private void addThrottledWork(int workIn)
	{
		if (workThisCycle + workIn > workPerCycleLimit)
		{
			this.work += workPerCycleLimit - workThisCycle;
		}
		else
		{
			this.work += workIn;
		}
	}
	
	public float getProgress()
	{
		return (float)work / (float)workMax;
	}
	
	//Subclasses which don't want to use the replacable meshes can override this directly.
	public TextureAtlasSprite getMeshTexture()
	{
		if (mesh != null)
			return ((ISieveMesh) mesh.getItem()).getMeshTexture();
		else
			return null;
	}
	
	public void startSpawningParticles()
	{
		this.spawningParticles = true;
		this.spawnParticlesTimer = 0;
	}
	
	@SideOnly(Side.CLIENT)
	private void generateParticles(IBlockState block)
	{
		if (block != null)
		{
			TextureAtlasSprite texture = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(block);

			for (int x = 0; x < 6; x++)
			{	
				ParticleSieve dust = new ParticleSieve(worldObj, 
						pos.getX() + 0.8d * worldObj.rand.nextFloat() + 0.15d, 
						pos.getY() + 0.585d, 
						pos.getZ() + 0.8d * worldObj.rand.nextFloat() + 0.15d, 
						0.0d, 0.0d, 0.0d, texture);
				
				Minecraft.getMinecraft().effectRenderer.addEffect(dust);
			}
		}
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) 
	{
		return !oldState.getBlock().equals(newState.getBlock());
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		
		work = compound.getInteger("work");
		if(compound.getBoolean("particles"))
			startSpawningParticles();
		
		NBTTagList items = compound.getTagList("items", Constants.NBT.TAG_COMPOUND);
		
		NBTTagCompound meshTag = items.getCompoundTagAt(0);
		setMesh(ItemStack.loadItemStackFromNBT(meshTag));
		
		NBTTagCompound contentsTag = items.getCompoundTagAt(1);
		setContents(ItemStack.loadItemStackFromNBT(contentsTag));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		
		compound.setInteger("work", work);
		compound.setBoolean("particles", spawningParticles);
		
		NBTTagList items = new NBTTagList();
		
		NBTTagCompound meshTag = new NBTTagCompound();
		if (mesh != null)
		{
			mesh.writeToNBT(meshTag);
		}
		items.appendTag(meshTag);
		
		NBTTagCompound contentsTag = new NBTTagCompound();
		if (contents != null)
		{
			contents.writeToNBT(contentsTag);
		}
		items.appendTag(contentsTag);
		
		compound.setTag("items", items);
		return compound;
	}

	@Override
	public NBTTagCompound getUpdateTag()
	{
		return this.writeToNBT(new NBTTagCompound());
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag)
	{
		this.readFromNBT(tag);
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		NBTTagCompound tag = new NBTTagCompound();
		this.writeToNBT(tag);

		return new SPacketUpdateTileEntity(this.getPos(), this.getBlockMetadata(), tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		NBTTagCompound tag = pkt.getNbtCompound();
		this.readFromNBT(tag);
	}


	//for automation (IInventory)
    @Override
    public int getSizeInventory() {return 2;}

    @Override
    public ItemStack getStackInSlot(int index) {return null;}

    @Override
    public ItemStack decrStackSize(int index, int count) {return null;}

    @Override
    public ItemStack removeStackFromSlot(int index) {return null;}

    @Override
    public void setInventorySlotContents(int index, @Nullable ItemStack stack) {
        if (isItemValidForSlot(index, stack) && ENOConfig.sieve_automation) {
            if (index == 0 && hasMesh())
                contents = stack;
            if (index == 1)
                mesh = stack;
            getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
        }

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        Block block = Block.getBlockFromItem(stack.getItem());
        if (ENOConfig.sieve_automation) {
			if (block != null)
				return index == 0 && contents == null && SieveRegistry.isSiftable(block.getStateFromMeta(stack.getMetadata()));
			else
				return index == 1 && stack.getItem() instanceof ISieveMesh && mesh == null && !ENOConfig.classic_sieve;
		}
		return false;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {return false;}

    @Override
    public void openInventory(EntityPlayer player) {}

    @Override
    public void closeInventory(EntityPlayer player) {}

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {}

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {}

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public String getName() {return ENOBlocks.SIEVE_WOOD.getUnlocalizedName();}

    public boolean hasCustomName() {return false;}

    public ITextComponent getDisplayName() {return null;}
}
