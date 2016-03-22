package com.flipnoter.advancedrfmachines.tileEntites;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;

/**
 * Created by Connor on 3/21/2016.
 */
public class BasicSolarPanelTileEntity extends TileEntity implements ISidedInventory, ITickable, IEnergyProvider {

    private int RFPT = 1;

    private ItemStack[] Inventory;
    private String InvName = "Basic Solar Panel";

    private EnergyStorage storage = new EnergyStorage(100000, 0, 100);

    public BasicSolarPanelTileEntity() {

        Inventory = new ItemStack[getSizeInventory()];

    }

    @Override
    public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {

        if(getStackInSlot(2) != null)
            storage.setMaxExtract((int)(100 + (10f * (getStackInSlot(2).stackSize / 8))));

        if(!worldObj.isRemote)
            worldObj.markBlockForUpdate(pos);

        return storage.extractEnergy(maxExtract, simulate);

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

        return hasCustomName() ? InvName : "container.Basic_Solar_Panel_TileEntity.name";

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

        return 5;

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

            if(getStackInSlot(4) != null)
                if(getStackInSlot(4).getItem() instanceof IEnergyContainerItem) {

                    IEnergyContainerItem item = (IEnergyContainerItem)getStackInSlot(4).getItem();
                    storage.extractEnergy(item.receiveEnergy(getStackInSlot(4), storage.getEnergyStored(), false), false);

                    upd = true;

                }

            if(worldObj.canSeeSky(pos.add(0, 1, 0))) {

                float multiplicator = 1.5f;
                float displacement = 1.2f;
                float mSunIntensity = 0f;

                float celestialAngleRadians = worldObj.getCelestialAngleRadians(1.0f);

                if(celestialAngleRadians > Math.PI)
                    celestialAngleRadians = (2 * 3.141592f - celestialAngleRadians);

                mSunIntensity = multiplicator * MathHelper.cos(celestialAngleRadians / displacement);
                mSunIntensity = Math.max(0, mSunIntensity);
                mSunIntensity = Math.min(1, mSunIntensity);

                if(mSunIntensity > 0) {

                    if(worldObj.isRaining())
                        mSunIntensity *= 0.5f;

                    if(worldObj.isThundering())
                        mSunIntensity *= 0.25f;

                    storage.setEnergyStored(storage.getEnergyStored() + (int)(RFPT * mSunIntensity));

                    upd = true;

                }
            }

            if((storage.getEnergyStored() > 0)) {

                for(int i = 0; i < 6; i++){

                    TileEntity tile = worldObj.getTileEntity(new BlockPos(pos.getX() + EnumFacing.values()[i].getFrontOffsetX(),
                            pos.getY() + EnumFacing.values()[i].getFrontOffsetY(),
                            pos.getZ() + EnumFacing.values()[i].getFrontOffsetZ()));

                    if(tile != null && tile instanceof IEnergyReceiver)
                        storage.extractEnergy(((IEnergyReceiver)tile).receiveEnergy(EnumFacing.values()[i].getOpposite(), storage.extractEnergy(storage.getMaxExtract(), true), false), false);

                }
            }
        }

        if(upd) {

            worldObj.markBlockForUpdate(pos);
            markDirty();

        }
    }
}
