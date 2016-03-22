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
public class UpgradeEfficiency extends Item {

    public UpgradeEfficiency() {

        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setUnlocalizedName("UpgradeEfficiency");
        this.setCreativeTab(ModItems.ADVRFMTab);

    }

    @Override
    public int getMetadata(int damage) {

        return damage;

    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {

        if(stack.getMetadata() == 0 || stack.getMetadata() == 1 || stack.getMetadata() == 2) {

            this.setMaxStackSize(8);

            if(stack.getMetadata() == 1)
                this.setMaxStackSize(16);

            if(stack.getMetadata() == 2)
                this.setMaxStackSize(64);

            return "item.T" + (stack.getMetadata() + 1) + "UpgradeEfficiency";

        }

        return "Invalid_Efficiency_Upgrade";

    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {

        subItems.add(new ItemStack(itemIn, 1, 0));
        subItems.add(new ItemStack(itemIn, 1, 1));
        subItems.add(new ItemStack(itemIn, 1, 2));

    }
}
