package com.flipnoter.advancedrfmachines.render;

import com.flipnoter.advancedrfmachines.advancedrfmachines;
import com.flipnoter.advancedrfmachines.items.ModItems;
import com.flipnoter.advancedrfmachines.items.UpgradeEfficiency;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

/**
 * Created by Connor on 3/6/2016.
 */
public class ItemRenderRegister {

    public static String modid = advancedrfmachines.MODID;

    static ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();

    public static void registerItemRenderer() {

        ModelBakery.registerItemVariants(ModItems.UpgradeEfficiency,
                new ModelResourceLocation(modid + ":" + "T1UpgradeEfficiency", "inventory"),
                new ModelResourceLocation(modid + ":" + "T2UpgradeEfficiency", "inventory"),
                new ModelResourceLocation(modid + ":" + "T3UpgradeEfficiency", "inventory"));

        register(ModItems.UpgradeEfficiency, 3);

        register(ModItems.SmallLiOnBattery);

    }

    public static void register(Item item) {

        mesher.register(item, 0, new ModelResourceLocation(modid + ":" + item.getUnlocalizedName().substring(5), "inventory"));

    }

    public static void register(Item item, int meta) {

        for(int i = 0; i < meta; i++) {

            ItemStack stack = new ItemStack(item, 1, i);

            mesher.register(item, i, new ModelResourceLocation(modid + ":" + item.getUnlocalizedName(stack).substring(5), "inventory"));

            System.out.println(item.getUnlocalizedName(stack).substring(5) + " " + i);

        }
    }
}