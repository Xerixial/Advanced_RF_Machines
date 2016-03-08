package com.flipnoter.advancedrfmachines;

import com.flipnoter.advancedrfmachines.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by Connor on 3/5/2016.
 */
@Mod(modid = advancedrfmachines.MODID, name = advancedrfmachines.MODNAME, version = advancedrfmachines.VERSION)
public class advancedrfmachines {

    public static final String MODID = "advancedrfmachines";
    public static final String MODNAME = "Advanced RF Machines";
    public static final String VERSION = "1.0";

    @Mod.Instance
    public static advancedrfmachines instance = new advancedrfmachines();

    @SidedProxy(clientSide="com.flipnoter.advancedrfmachines.proxy.ClientProxy", serverSide="com.flipnoter.advancedrfmachines.proxy.ServerProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public static void preInit(final FMLPreInitializationEvent event) {

        proxy.preInit(event);

    }

    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {

        proxy.init(event);

    }

    @Mod.EventHandler
    public void postInit(final FMLPostInitializationEvent event) {

        proxy.postInit(event);

    }
}
