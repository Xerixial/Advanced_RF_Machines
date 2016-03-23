package com.flipnoter.advancedrfmachines.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Created by Connor on 3/6/2016.
 */
public class UpgradeStorage extends Item {

    public UpgradeStorage() {

        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setUnlocalizedName("UpgradeStorage");
        this.setCreativeTab(ModItems.ADVRFMTab);

    }

    @Override
    public int getMetadata(int damage) {

        return damage;

    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {

        if(stack.getMetadata() >= 0 && stack.getMetadata() <= 3) {

            return "item.T" + (stack.getMetadata() + 1) + "UpgradeStorage";

        }

        return "Invalid_Storage_Upgrade";

    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {

        subItems.add(new ItemStack(itemIn, 1, 0));
        subItems.add(new ItemStack(itemIn, 1, 1));
        subItems.add(new ItemStack(itemIn, 1, 2));
        subItems.add(new ItemStack(itemIn, 1, 3));

    }
}
