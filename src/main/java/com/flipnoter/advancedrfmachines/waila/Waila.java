package com.flipnoter.advancedrfmachines.waila;

import com.flipnoter.advancedrfmachines.tileEntites.*;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraftforge.fml.common.event.FMLInterModComms;

/**
 * Created by Connor on 3/6/2016.
 */
public class Waila {

    public static void init() {

        FMLInterModComms.sendMessage("Waila", "register", "com.flipnoter.advancedrfmachines.waila.Waila.callbackRegister");

    }

    public static void callbackRegister(IWailaRegistrar registrar) {

        //registrar.registerBodyProvider(new WailaProviderPowerSender(), BasicConduitTileEntity.class);
        registrar.registerBodyProvider(new WailaProviderPowerSender(), FurnaceGeneratorTileEntity.class);
        registrar.registerBodyProvider(new WailaProviderPowerSender(), BasicSolarPanelTileEntity.class);
        //registrar.registerBodyProvider(new WailaProviderPowerSender(), BasicEnergyCellTileEntity.class);
        registrar.registerBodyProvider(new WailaProviderPowerReciever(), PoweredFurnaceTileEntity.class);

    }
}
