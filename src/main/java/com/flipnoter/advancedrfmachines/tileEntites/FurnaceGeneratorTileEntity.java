package com.flipnoter.advancedrfmachines.tileEntites;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyProvider;
import com.flipnoter.advancedrfmachines.blocks.FurnaceGenerator;
import com.flipnoter.advancedrfmachines.items.ModItems;
import com.flipnoter.advancedrfmachines.items.SmallLiOnBattery;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.lwjgl.Sys;

import java.io.Console;

/**
 * Created by Connor on 3/5/2016.
 */
public class FurnaceGeneratorTileEntity extends TileEntity implements ISidedInventory, ITickable, IEnergyProvider {

    private int buffer, fullBurn, RFPT = 10;

    private static boolean converting;

    private ItemStack[] Inventory;
    private String InvName = "Furnace Generator";

    private EnergyStorage storage = new EnergyStorage(100000, 0, 100);

    public FurnaceGeneratorTileEntity() {

        Inventory = new ItemStack[getSizeInventory()];

    }

    @Override
    public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {

        if(getStackInSlot(2) != null)
            storage.setMaxExtract((int)(100 + (10f * (getStackInSlot(2).stackSize / 8))));

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

        return hasCustomName() ? InvName : "container.Furnace_Generator_TileEntity.name";

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

    public static int getItemBurnTime(ItemStack stack) {

        if(stack == null) {

            return 0;

        }
        else
        {

            Item item = stack.getItem();

            if(item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.air) {

                Block block = Block.getBlockFromItem(item);

                if(block == Blocks.wooden_slab)
                    return 150;

                if(block.getMaterial() == Material.wood)
                    return 300;

                if(block == Blocks.coal_block)
                    return 16000;

            }

            if(item instanceof ItemTool && ((ItemTool)item).getToolMaterialName().equals("WOOD")) return 200;
            if(item instanceof ItemSword && ((ItemSword)item).getToolMaterialName().equals("WOOD")) return 200;
            if(item instanceof ItemHoe && ((ItemHoe)item).getMaterialName().equals("WOOD")) return 200;
            if(item == Items.stick) return 100;
            if(item == Items.coal) return 1600;
            if(item == Items.lava_bucket) return 20000;
            if(item == item.getItemFromBlock(Blocks.sapling)) return 100;
            if(item == Items.blaze_rod) return 2400;
            return GameRegistry.getFuelValue(stack);

        }
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

            if(buffer > 0 && storage.getEnergyStored() < storage.getMaxEnergyStored()) {

                storage.setEnergyStored(storage.getEnergyStored() + Math.min((int)(RFPT * getEffMod()), storage.getMaxEnergyStored() - storage.getEnergyStored()));
                buffer -= 1;

                upd = true;

            }

            if(getStackInSlot(0) != null && getItemBurnTime(getStackInSlot(0)) > 0) {

                if(storage.getEnergyStored() < storage.getMaxEnergyStored() - 10) {

                    converting = true;

                }
                else
                {

                    converting = false;

                }

                if(converting && buffer == 0) {

                    buffer = getItemBurnTime(getStackInSlot(0));
                    fullBurn = getItemBurnTime(getStackInSlot(0));

                    Inventory[0].stackSize--;

                    if(this.Inventory[0].stackSize == 0) {

                        this.Inventory[0] = Inventory[0].getItem().getContainerItem(Inventory[0]);

                    }

                    upd = true;

                }
            }
        }

        if(upd) {

            worldObj.markBlockForUpdate(pos);
            markDirty();

        }
    }
}
