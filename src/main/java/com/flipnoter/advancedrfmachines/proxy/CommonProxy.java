package com.flipnoter.advancedrfmachines.proxy;

import com.flipnoter.advancedrfmachines.advancedrfmachines;
import com.flipnoter.advancedrfmachines.blocks.ModBlocks;
import com.flipnoter.advancedrfmachines.gui.ModGUIHandler;
import com.flipnoter.advancedrfmachines.items.ModItems;
import com.flipnoter.advancedrfmachines.tileEntites.ModTileEntities;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

/**
 * Created by Connor on 3/6/2016.
 */
public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {

        ModItems.init();

        ModBlocks.init();

        ModTileEntities.init();

    }

    public void init(FMLInitializationEvent event) {

        //ModRecipes.Init();

        //GameRegistry.registerWorldGenerator(new OreGenerator(), 0);

        NetworkRegistry.INSTANCE.registerGuiHandler(advancedrfmachines.instance, new ModGUIHandler());

    }

    public void postInit(FMLPostInitializationEvent event) {

    }
}
