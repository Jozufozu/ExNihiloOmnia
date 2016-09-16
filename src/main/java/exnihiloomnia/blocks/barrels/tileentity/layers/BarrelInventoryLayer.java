package exnihiloomnia.blocks.barrels.tileentity.layers;

import java.util.ArrayList;

import exnihiloomnia.ENO;
import exnihiloomnia.blocks.barrels.architecture.BarrelState;
import exnihiloomnia.blocks.barrels.tileentity.TileEntityBarrel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.Constants;

public class BarrelInventoryLayer extends BarrelStateLayer implements ISidedInventory {
	protected ArrayList<ItemStack> output = new ArrayList<ItemStack>();
	protected ItemStack contents = null;
	protected int MAX_OUTPUT_QUEUE_SIZE = 1;
	
	private int[] SLOTS_AVAILABLE_FROM_TOP = new int[]{1};
	private int[] SLOTS_AVAILABLE_FROM_BOTTOM = new int[]{0};
	private int[] SLOTS_NONE = new int[]{};
	
	public void addOutput(ItemStack item) {
		if (item != null && item.stackSize > 0) {
			output.add(item);
		}
	}
	
	public void setContents(ItemStack item) {
		if (item != null) {
			this.contents = item;
			((TileEntityBarrel)this).requestSync();
		}
		else {
			if (contents != null) {
				this.contents = null;
				this.state.onExtractContents((TileEntityBarrel)this);
			}
		}
	}
	
	public ItemStack getContents() {
		return contents;
	}
	
	@Override
	public int getSizeInventory() {
		return 2;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		if (index == 0) {
			if (contents != null && this.state.canExtractContents((TileEntityBarrel)this)) {
				return contents;
			}
			else if (output.size() > 0) {
				return output.get(0);
			}
		}
		
		return null;
	}

	@Override
	public ItemStack decrStackSize(int index, int count)  {
		ItemStack item = getStackInSlot(index);
		
        if (item != null && count > 0)  {
            if (item.stackSize <= count)  {
                setInventorySlotContents(index, null);
            }
            else {
                item = item.splitStack(count);
            }
        }
        
        return item;
	}

	@Override
  	public ItemStack removeStackFromSlot(int index) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack)  {
		if (stack == null || stack.getItem() == null) {
			if (contents != null) {
				setContents(null);
			}
			else if (index == 0 && output.size() > 0) {
				output.remove(0);
			}
		}
		else {
			if (index == 1) {
				TileEntityBarrel barrel = (TileEntityBarrel)this;
				
				barrel.getState().useItem(null, null, barrel, stack);
			}
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return false;
	}

	@Override
	public void openInventory(EntityPlayer player) {}

	@Override
	public void closeInventory(EntityPlayer player) {}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if (this.output.size() < MAX_OUTPUT_QUEUE_SIZE) {
			TileEntityBarrel barrel = (TileEntityBarrel)this;
			BarrelState state = barrel.getState();
			
			return state.canUseItem(barrel, stack);
		}
		else {
			ENO.log.error("insert rejected due to output being full.");
		}
		
		return false;
	}

	@Override
	public int getField(int id)  {
		return 0;
	}

	@Override
	public void setField(int id, int value) {}

	@Override
	public int getFieldCount()  {
		return 0;
	}

	@Override
	public void clear()  {
		output.clear();
	}

	@Override
	public String getName() {
		TileEntityBarrel barrel = (TileEntityBarrel)this;
		return barrel.getBlockType().getUnlocalizedName();
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public ITextComponent getDisplayName() {
		return null;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		if (side == EnumFacing.DOWN) {
			return SLOTS_AVAILABLE_FROM_BOTTOM;
		}
		
		if (side == EnumFacing.UP) {
			return SLOTS_AVAILABLE_FROM_TOP;
		}
		
		return SLOTS_NONE;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack stack, EnumFacing direction) {
		if (direction == EnumFacing.UP && index == 1) {
			TileEntityBarrel barrel = (TileEntityBarrel)this;
			BarrelState state = barrel.getState();
			
			if (state != null)
				return state.canUseItem(barrel, stack);
		}
		
		return false;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		if (direction == EnumFacing.DOWN && index == 0) {
			if (output.size() > 0) {
				return true;
			} 
			else if (contents != null && state.canExtractContents((TileEntityBarrel)this)) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		NBTTagList items = compound.getTagList("items", Constants.NBT.TAG_COMPOUND);
		
		for (int x = 0; x < items.tagCount(); x++) {
			NBTTagCompound item = items.getCompoundTagAt(x);
			output.add(ItemStack.loadItemStackFromNBT(item));
		}

		NBTTagList content = compound.getTagList("content", Constants.NBT.TAG_COMPOUND);
		
		if (content.tagCount() > 0) {
			NBTTagCompound item = content.getCompoundTagAt(0);
			contents = ItemStack.loadItemStackFromNBT(item);
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);

		NBTTagList items = new NBTTagList();
		
		for (int x = 0; x < output.size(); x++) {
			NBTTagCompound item = new NBTTagCompound();
			output.get(x).writeToNBT(item);
			items.appendTag(item);
		}
		
		compound.setTag("items", items);

		NBTTagList content = new NBTTagList();
		
		if (contents != null) {
			NBTTagCompound item = new NBTTagCompound();
			contents.writeToNBT(item);
			content.appendTag(item);
		}
		
		compound.setTag("content", content);
		return compound;
	}
}
