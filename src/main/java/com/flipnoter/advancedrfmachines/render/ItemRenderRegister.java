package com.flipnoter.advancedrfmachines.render;

import com.flipnoter.advancedrfmachines.advancedrfmachines;
import com.flipnoter.advancedrfmachines.items.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;

/**
 * Created by Connor on 3/6/2016.
 */
public class ItemRenderRegister {

    public static String modid = advancedrfmachines.MODID;

    public static void registerItemRenderer() {

        register(ModItems.T1UpgradeEfficiency);

        register(ModItems.SmallLiOnBattery);

    }

    public static void register(Item item) {

        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(modid + ":" + item.getUnlocalizedName().substring(5), "inventory"));

    }
}
