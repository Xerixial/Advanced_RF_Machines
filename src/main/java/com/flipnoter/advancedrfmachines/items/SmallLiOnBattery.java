package com.flipnoter.advancedrfmachines.items;

import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.ItemEnergyContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by Connor on 3/6/2016.
 */
public class SmallLiOnBattery extends ItemEnergyContainer implements IEnergyContainerItem {

    public SmallLiOnBattery() {

        this.setUnlocalizedName("Small_LiOn_Battery");
        this.setCreativeTab(ModItems.ADVRFMTab);
        this.setMaxStackSize(1);

        this.setCapacity(1000);
        this.setMaxTransfer(10);

    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {

        return !(getEnergyStored(stack) == getMaxEnergyStored(stack));

    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {

        return 1D - ((double)getEnergyStored(stack) / (double)getMaxEnergyStored(stack));

    }

    @Override
    public void addInformation(final ItemStack stack, final EntityPlayer player, final List list, final boolean extraInformation) {

         list.add("Stored " + getEnergyStored(stack) + " / " + getMaxEnergyStored(stack));

    }
}
