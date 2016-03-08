package com.flipnoter.advancedrfmachines.items;

import com.flipnoter.advancedrfmachines.blocks.ModBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Connor on 3/6/2016.
 */
public class ModItems {

    public static Item T1UpgradeEfficiency; //Decrease consumption, Increase Generation
    public static Item T1UpgradeTransfer;
    public static Item T1UpgradeSpeed;
    public static Item T1UpgradeStorage;

    public static Item SmallLiOnBattery;

    public static final CreativeTabs ADVRFMTab = new CreativeTabs("Advanced_RF_Machines") {

        @Override
        public Item getTabIconItem() {

            return Item.getItemFromBlock(ModBlocks.FurnaceGenerator);

        }
    };

    public static void init() {

        register(T1UpgradeEfficiency = new T1UpgradeEfficiency());

        register(SmallLiOnBattery = new SmallLiOnBattery());

    }

    static void register(Item item) {

        GameRegistry.registerItem(item, item.getUnlocalizedName().substring(5));

    }
}
