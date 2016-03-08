package com.flipnoter.advancedrfmachines.tileEntites;

import cofh.api.energy.*;
import com.flipnoter.advancedrfmachines.blocks.FurnaceGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.*;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Connor on 3/5/2016.
 */
public class PoweredFurnaceTileEntity extends TileEntity implements ISidedInventory, ITickable, IEnergyReceiver {

    private int buffer, fullBurn = 200, RFPT = 10;

    private static boolean converting;

    private ItemStack[] Inventory;
    private String InvName = "Powered Furnace";

    private EnergyStorage storage = new EnergyStorage(100000, 100);

    public PoweredFurnaceTileEntity() {

        Inventory = new ItemStack[getSizeInventory()];

    }

    @Override
    public int receiveEnergy(EnumFacing facing, int maxReceive, boolean simulate) {

        return storage.receiveEnergy(maxReceive, simulate);

    }

    @Override
    public int getEnergyStored(EnumFacing from) {

        return storage.getEnergyStored();

    }

    @Override
    public int getMaxEnergyStored(EnumFacing from) {

        return storage.getMaxEnergyStored();

    }

    @Override
    public boolean canConnectEnergy(EnumFacing from) {

        return true;

    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {

        return new int[0];

    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {

        return false;

    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {

        return false;

    }

    @Override
    public String getName() {

        return hasCustomName() ? InvName : "container.Powered_Furnace_TileEntity.name";

    }

    public void setCustomName(String name) {

        InvName = name;

    }

    @Override
    public boolean hasCustomName() {

        return InvName != null && !InvName.equals("");

    }

    @Override
    public IChatComponent getDisplayName() {

        return hasCustomName() ? new ChatComponentText(getName()) : new ChatComponentTranslation(getName());

    }

    @Override
    public int getSizeInventory() {

        return 6;

    }

    @Override
    public int getInventoryStackLimit() {

        return 64;

    }

    @Override
    public ItemStack getStackInSlot(int index) {

        if(index < 0 || index >= getSizeInventory())
            return null;

        return Inventory[index];

    }

    @Override
    public ItemStack decrStackSize(int index, int count) {

        if(getStackInSlot(index) != null) {
            ItemStack itemstack;

            if(getStackInSlot(index).stackSize <= count) {

                itemstack = getStackInSlot(index);
                setInventorySlotContents(index, null);
                markDirty();
                return itemstack;

            }
            else
            {

                itemstack = getStackInSlot(index).splitStack(count);

                if(getStackInSlot(index).stackSize <= 0) {

                    setInventorySlotContents(index, null);

                }
                else
                {

                    //Just to show that changes happened
                    setInventorySlotContents(index, getStackInSlot(index));

                }

                markDirty();
                return itemstack;

            }
        }
        else
        {

            return null;

        }
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {

        ItemStack stack = getStackInSlot(index);
        setInventorySlotContents(index, null);
        return stack;

    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {

        if(index < 0 || index >= getSizeInventory())
            return;

        if(stack != null && stack.stackSize > getInventoryStackLimit())
            stack.stackSize = getInventoryStackLimit();

        if(stack != null && stack.stackSize == 0)
            stack = null;

        Inventory[index] = stack;
        markDirty();

    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {

        return worldObj.getTileEntity(getPos()) == this && player.getDistanceSq(pos.add(0.5, 0.5, 0.5)) <= 64;

    }

    @Override
    public void openInventory(EntityPlayer player) {}

    @Override
    public void closeInventory(EntityPlayer player) {}

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {

        return false;

    }

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
    public void writeToNBT(NBTTagCompound nbt) {

        super.writeToNBT(nbt);

        storage.writeToNBT(nbt);

        nbt.setInteger("buffer", buffer);
        nbt.setInteger("fullBurn", fullBurn);

        nbt.setBoolean("converting", converting);

        NBTTagList list = new NBTTagList();

        for(int i = 0; i < getSizeInventory(); ++i) {

            if(getStackInSlot(i) != null) {

                NBTTagCompound stackTag = new NBTTagCompound();
                stackTag.setByte("Slot", (byte)i);
                getStackInSlot(i).writeToNBT(stackTag);
                list.appendTag(stackTag);

            }
        }

        nbt.setTag("Items", list);

        if(hasCustomName())
            nbt.setString("CustomName", InvName);

    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {

        super.readFromNBT(nbt);

        storage.readFromNBT(nbt);

        buffer = nbt.getInteger("buffer");
        fullBurn = nbt.getInteger("fullBurn");

        converting = nbt.getBoolean("converting");

        NBTTagList list = nbt.getTagList("Items", 10);

        for(int i = 0; i < list.tagCount(); ++i) {

            NBTTagCompound stackTag = list.getCompoundTagAt(i);
            int slot = stackTag.getByte("Slot") & 255;
            setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(stackTag));

        }

        if(nbt.hasKey("CustomName", 8)) {

            setCustomName(nbt.getString("CustomName"));

        }
    }

    @Override
    public void clear() {

        for(int i = 0; i < getSizeInventory(); i++)
            setInventorySlotContents(i, null);

    }

    @Override
    public Packet getDescriptionPacket() {

        NBTTagCompound tagCompound = new NBTTagCompound();
        writeToNBT(tagCompound);
        return new S35PacketUpdateTileEntity(pos, 1, tagCompound);

    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {

        readFromNBT(pkt.getNbtCompound());

    }

    public int getTimeRemaining() {

        return buffer;

    }

    public int getBurnTime() {

        return fullBurn;

    }

    float getEffMod() {

        float mod = 1;

        if(getStackInSlot(1) != null)
            mod += 1f * (getStackInSlot(1).stackSize / 8);

        return mod;

    }

    private boolean canSmelt() {

        if(Inventory[0] == null) {

            return false;

        }
        else
        {

            ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(Inventory[0]);

            if(itemstack == null)
                return false;

            if(Inventory[1] == null)
                return true;

            if(!Inventory[1].isItemEqual(itemstack))
                return false;

            int result = Inventory[1].stackSize + itemstack.stackSize;

            return result <= getInventoryStackLimit() && result <= Inventory[1].getMaxStackSize(); //Forge BugFix: Make it respect stack sizes properly.

        }
    }

    public void smeltItem() {

        if(this.canSmelt()) {

            ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(Inventory[0]);

            if(Inventory[1] == null) {

                Inventory[1] = itemstack.copy();

            }
            else if(Inventory[1].getItem() == itemstack.getItem()) {

                Inventory[1].stackSize += itemstack.stackSize; // Forge BugFix: Results may have multiple items

            }

            Inventory[0].stackSize--;

            if(Inventory[0].stackSize <= 0) {

                Inventory[0] = null;

            }
        }
    }

    @Override
    public void update() {

        boolean upd = false;

        if(!worldObj.isRemote) {

            if(storage.getEnergyStored() < storage.getMaxEnergyStored()){
                // get power from neighbors who have more than this conductor
                tryGetEnergy(EnumFacing.UP);
                tryGetEnergy(EnumFacing.NORTH);
                tryGetEnergy(EnumFacing.WEST);
                tryGetEnergy(EnumFacing.SOUTH);
                tryGetEnergy(EnumFacing.EAST);
                tryGetEnergy(EnumFacing.DOWN);

            }

            if(getStackInSlot(5) != null)
                if(getStackInSlot(5).getItem() instanceof IEnergyContainerItem) {

                    IEnergyContainerItem item = (IEnergyContainerItem)getStackInSlot(5).getItem();
                    item.extractEnergy(getStackInSlot(5), storage.receiveEnergy(item.extractEnergy(getStackInSlot(5), storage.getEnergyStored(), true), false), false);

                    upd = true;

                }

            if(Inventory[0] != null) {

                if(buffer == 0 && this.canSmelt()) {

                    buffer = getBurnTime();

                }

                if(buffer > 0 && canSmelt()) {

                    buffer--;

                    storage.setEnergyStored(storage.getEnergyStored() - RFPT);

                    if(buffer == 0) {

                        this.smeltItem();

                    }

                    upd = true;

                }
                else
                {

                    buffer = getBurnTime();

                }
            }
            else
            {

                buffer = getBurnTime();

            }
        }

        if(upd) {

            worldObj.markBlockForUpdate(pos);
            markDirty();

        }
    }

    void tryGetEnergy(EnumFacing dir) {

        float deficit = Math.max(storage.getMaxEnergyStored() - storage.getEnergyStored(), storage.getMaxExtract());

        if(deficit > 0) {

            EnumFacing otherDir = null;
            BlockPos coord = null;

            switch(dir) {

                case UP:
                    coord = getPos().add(0,1,0);
                    otherDir = EnumFacing.DOWN;
                    break;
                case DOWN:
                    coord = getPos().add(0,-1,0);
                    otherDir = EnumFacing.UP;
                    break;
                case NORTH:
                    coord = getPos().add(0,0,-1);
                    otherDir = EnumFacing.SOUTH;
                    break;
                case SOUTH:
                    coord = getPos().add(0,0,1);
                    otherDir = EnumFacing.NORTH;
                    break;
                case EAST:
                    coord = getPos().add(1,0,0);
                    otherDir = EnumFacing.WEST;
                    break;
                case WEST:
                    coord = getPos().add(-1,0,0);
                    otherDir = EnumFacing.EAST;

            }

            TileEntity e = getWorld().getTileEntity(coord);

            if(e instanceof IEnergyHandler || e instanceof IEnergyProvider) {

                IEnergyProvider pe = (IEnergyProvider)e;

                if(pe.canConnectEnergy(otherDir)) {

                    storage.receiveEnergy(pe.extractEnergy(otherDir, (int)deficit, false), false);

                    worldObj.markBlockForUpdate(pos);
                    markDirty();

                    worldObj.markBlockForUpdate(coord);
                    e.markDirty();

                }
            }
        }
    }
}
