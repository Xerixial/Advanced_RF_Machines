package com.flipnoter.advancedrfmachines.tileEntites;

import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Connor on 3/6/2016.
 */
public class ModTileEntities {

    public static void init() {

        GameRegistry.registerTileEntity(FurnaceGeneratorTileEntity.class, "Furnace_Generator");
        GameRegistry.registerTileEntity(PoweredFurnaceTileEntity.class, "Powered_Furnace");

        GameRegistry.registerTileEntity(BasicConduitTileEntity.class, "Basic_Conduit");

        GameRegistry.registerTileEntity(BasicEnergyCellTileEntity.class, "Basic_EnergyCell");

    }
}
