package com.flipnoter.advancedrfmachines.items;

import com.flipnoter.advancedrfmachines.blocks.ModBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Connor on 3/6/2016.
 */
public class ModItems {

    public static Item UpgradeEfficiency; //Decrease consumption, Increase Generation
    public static Item UpgradeTransfer;
    public static Item UpgradeSpeed;
    public static Item UpgradeStorage;

    public static Item SmallLiOnBattery;

    public static final CreativeTabs ADVRFMTab = new CreativeTabs("Advanced_RF_Machines") {

        @Override
        public Item getTabIconItem() {

            return Item.getItemFromBlock(ModBlocks.FurnaceGenerator);

        }
    };

    public static void init() {

        register(UpgradeEfficiency = new UpgradeEfficiency());

        register(SmallLiOnBattery = new SmallLiOnBattery());

    }

    static void register(Item item) {

        GameRegistry.registerItem(item, item.getUnlocalizedName().substring(5));

    }
}
