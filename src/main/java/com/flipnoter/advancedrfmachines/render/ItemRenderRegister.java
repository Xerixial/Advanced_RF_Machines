package com.flipnoter.advancedrfmachines.render;

import com.flipnoter.advancedrfmachines.advancedrfmachines;
import com.flipnoter.advancedrfmachines.items.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Created by Connor on 3/6/2016.
 */
public class ItemRenderRegister {

    public static String modid = advancedrfmachines.MODID;

    static ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();

    public static void registerItemRenderer() {

        register(ModItems.UpgradeEfficiency, 4);
        register(ModItems.UpgradeStorage, 4);

        register(ModItems.SmallLiOnBattery);

    }

    public static void register(Item item) {

        mesher.register(item, 0, new ModelResourceLocation(modid + ":" + item.getUnlocalizedName().substring(5), "inventory"));

    }

    public static void register(Item item, int meta) {

        ModelResourceLocation[] models = new ModelResourceLocation[meta];

        ModelResourceLocation model;

        for(int i = 0; i < meta; i++) {

            ItemStack stack = new ItemStack(item, 1, i);

            model = new ModelResourceLocation(modid + ":" + item.getUnlocalizedName(stack).substring(5), "inventory");

            mesher.register(item, i, model);

            models[i] = model;

        }

        ModelBakery.registerItemVariants(item, models);

    }
}