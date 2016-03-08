package com.flipnoter.advancedrfmachines.render;

import com.flipnoter.advancedrfmachines.advancedrfmachines;
import com.flipnoter.advancedrfmachines.blocks.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;

/**
 * Created by Connor on 3/6/2016.
 */
public class BlockRenderRegister {

    public static String modid = advancedrfmachines.MODID;

    public static void registerBlockRenderer() {

        register(ModBlocks.FurnaceGenerator);
        register(ModBlocks.PoweredFurnace);

        register(ModBlocks.BasicConduit);

        register(ModBlocks.BasicEnergyCell);

    }

    public static void register(Block block) {

        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), 0, new ModelResourceLocation(modid + ":" + block.getUnlocalizedName().substring(5), "inventory"));

    }
}
