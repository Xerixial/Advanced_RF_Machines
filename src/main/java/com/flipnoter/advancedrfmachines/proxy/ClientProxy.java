package com.flipnoter.advancedrfmachines.proxy;

import com.flipnoter.advancedrfmachines.render.BlockRenderRegister;
import com.flipnoter.advancedrfmachines.render.ItemRenderRegister;
import com.flipnoter.advancedrfmachines.waila.Waila;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by Connor on 3/6/2016.
 */
public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {

        super.preInit(event);

    }

    @Override
    public void init(FMLInitializationEvent event) {

        super.init(event);

        ItemRenderRegister.registerItemRenderer();
        BlockRenderRegister.registerBlockRenderer();

        Waila.init();

    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {

        super.postInit(event);

    }
}
