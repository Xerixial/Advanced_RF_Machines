package com.flipnoter.advancedrfmachines.tileEntites;

import cofh.api.energy.*;
import com.flipnoter.advancedrfmachines.blocks.BasicEnergyCell;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;

/**
 * Created by Connor on 3/5/2016.
 */
public class BasicEnergyCellTileEntity extends TileEntity implements ISidedInventory, ITickable, IEnergyHandler {

    private ItemStack[] Inventory;
    private String InvName = "Basic Energy Cell";

    private EnumFacing lastExtract;

    private EnergyStorage storage = new EnergyStorage(100000, 1000);

    public BasicEnergyCellTileEntity() {

        Inventory = new ItemStack[getSizeInventory()];

    }

    @Override
    public int receiveEnergy(EnumFacing facing, int maxReceive, boolean simulate) {

        return storage.receiveEnergy(maxReceive, simulate);

    }

    @Override
    public int extractEnergy(EnumFacing facing, int maxExtract, boolean simulate) {

        if(!canConnectEnergy(facing))
            return 0;

        int meta = worldObj.getBlockState(pos).getBlock().getMetaFromState(worldObj.getBlockState(pos));

        EnumFacing front = EnumFacing.values()[meta];

        if(front == EnumFacing.DOWN && facing == EnumFacing.UP)
            return storage.extractEnergy(maxExtract, simulate);

        if(front == EnumFacing.UP && facing == EnumFacing.DOWN)
            return storage.extractEnergy(maxExtract, simulate);

        if(front == EnumFacing.NORTH && facing == EnumFacing.WEST)
            return storage.extractEnergy(maxExtract, simulate);

        if(front == EnumFacing.SOUTH && facing == EnumFacing.EAST)
            return storage.extractEnergy(maxExtract, simulate);

        if(front == EnumFacing.WEST && facing == EnumFacing.SOUTH)
            return storage.extractEnergy(maxExtract, simulate);

        if(front == EnumFacing.EAST && facing == EnumFacing.NORTH)
            return storage.extractEnergy(maxExtract, simulate);

        return 0;

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

        int meta = worldObj.getBlockState(pos).getBlock().getMetaFromState(worldObj.getBlockState(pos));

        EnumFacing front = EnumFacing.values()[meta];

        if(front == EnumFacing.DOWN || front == EnumFacing.UP)
            if(from == EnumFacing.DOWN || from == EnumFacing.UP)
                return true;

        if(front == EnumFacing.NORTH || front == EnumFacing.SOUTH)
            if(from == EnumFacing.EAST || from == EnumFacing.WEST)
                return true;

        if(front == EnumFacing.EAST || front == EnumFacing.WEST)
            if(from == EnumFacing.NORTH || from == EnumFacing.SOUTH)
                return true;

        return false;

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

        return hasCustomName() ? InvName : "container.Basic_EnergyCell_TileEntity.name";

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

        return 2;

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

            if(storage.getEnergyStored() < storage.getMaxEnergyStored())
                lastExtract = null;

            if(getStackInSlot(5) != null)
                if(getStackInSlot(5).getItem() instanceof IEnergyContainerItem) {

                    IEnergyContainerItem item = (IEnergyContainerItem) getStackInSlot(5).getItem();
                    item.extractEnergy(getStackInSlot(5), storage.receiveEnergy(item.extractEnergy(getStackInSlot(5), storage.getEnergyStored(), true), false), false);

                    upd = true;

                }
        }

        if(upd) {

            worldObj.markBlockForUpdate(pos);
            markDirty();

        }
    }

    void tryGetEnergy(EnumFacing dir) {

        if(lastExtract == dir || storage.getEnergyStored() == storage.getMaxEnergyStored())
            return;

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

                if(pe.canConnectEnergy(otherDir) && !(e instanceof BasicConduitTileEntity)) {

                    storage.receiveEnergy(pe.extractEnergy(otherDir, (int)deficit, false), false);

                    worldObj.markBlockForUpdate(pos);
                    markDirty();

                    worldObj.markBlockForUpdate(coord);
                    e.markDirty();

                }

                if(pe.canConnectEnergy(otherDir) && e instanceof BasicConduitTileEntity) {

                    lastExtract = dir;

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
