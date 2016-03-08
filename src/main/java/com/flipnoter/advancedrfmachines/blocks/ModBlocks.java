package com.flipnoter.advancedrfmachines.blocks;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Connor on 3/5/2016.
 */
public class ModBlocks {

    public static Block FurnaceGenerator;
    public static Block PoweredFurnace;

    public static Block BasicConduit;

    public static Block BasicEnergyCell;

    public static void init() {

        register(FurnaceGenerator = new FurnaceGenerator());
        register(PoweredFurnace = new PoweredFurnace());

        register(BasicConduit = new BasicConduit());

        register(BasicEnergyCell = new BasicEnergyCell());

    }

    public static void register(Block block) {

        GameRegistry.registerBlock(block, block.getUnlocalizedName().substring(5));

    }
}
