package com.flipnoter.advancedrfmachines.tileEntites;

import cofh.api.energy.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;

/**
 * Created by Connor on 3/5/2016.
 */
public class BasicConduitTileEntity extends TileEntity implements ITickable, IEnergyHandler {

    private EnergyStorage storage = new EnergyStorage(10000, 100);

    private EnumFacing lastExtract;

    public BasicConduitTileEntity() {}

    @Override
    public int receiveEnergy(EnumFacing facing, int maxReceive, boolean simulate) {

        lastExtract = facing;

        worldObj.markBlockForUpdate(pos);
        markDirty();

        return storage.receiveEnergy(maxReceive, simulate);

    }

    @Override
    public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {

        if(lastExtract == from) {

            lastExtract = null;

            return 0;

        }

        lastExtract = from;

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
    public void writeToNBT(NBTTagCompound nbt) {

        super.writeToNBT(nbt);

        storage.writeToNBT(nbt);

    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {

        super.readFromNBT(nbt);

        storage.readFromNBT(nbt);

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

        if(!worldObj.isRemote) {

            if(storage.getEnergyStored() < storage.getMaxEnergyStored()) {
                // get power from neighbors who have more than this conductor
                tryGetEnergy(EnumFacing.UP);
                tryGetEnergy(EnumFacing.NORTH);
                tryGetEnergy(EnumFacing.WEST);
                tryGetEnergy(EnumFacing.SOUTH);
                tryGetEnergy(EnumFacing.EAST);
                tryGetEnergy(EnumFacing.DOWN);

            }
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

                int extract = Math.min(storage.getMaxReceive(), pe.getEnergyStored(otherDir));

                if(pe.canConnectEnergy(otherDir) && storage.getEnergyStored() < pe.getEnergyStored(otherDir)) {

                    storage.receiveEnergy(pe.extractEnergy(otherDir, extract, false), false);

                    worldObj.markBlockForUpdate(pos);
                    markDirty();

                    worldObj.markBlockForUpdate(coord);
                    e.markDirty();

                }
            }
        }
    }
}
