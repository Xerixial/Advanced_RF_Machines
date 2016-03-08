package com.flipnoter.advancedrfmachines.gui;

import com.flipnoter.advancedrfmachines.guicontainer.ContainerFurnaceGenerator;
import com.flipnoter.advancedrfmachines.guicontainer.ContainerPoweredFurnace;
import com.flipnoter.advancedrfmachines.tileEntites.FurnaceGeneratorTileEntity;
import com.flipnoter.advancedrfmachines.tileEntites.PoweredFurnaceTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

/**
 * Created by Connor on 3/6/2016.
 */
public class ModGUIHandler implements IGuiHandler {

    public static final int Furnace_Generator_GUI = 0;
    public static final int Powered_Furnace_GUI = 1;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

        if(ID == Furnace_Generator_GUI)
            return new ContainerFurnaceGenerator(player.inventory, (FurnaceGeneratorTileEntity)world.getTileEntity(new BlockPos(x, y, z)));

        if(ID == Powered_Furnace_GUI)
            return new ContainerPoweredFurnace(player.inventory, (PoweredFurnaceTileEntity)world.getTileEntity(new BlockPos(x, y, z)));

        return null;

    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

        if(ID == Furnace_Generator_GUI)
            return new GUIFurnaceGenerator(player.inventory, (FurnaceGeneratorTileEntity)world.getTileEntity(new BlockPos(x, y, z)));

        if(ID == Powered_Furnace_GUI)
            return new GUIPoweredFurnace(player.inventory, (PoweredFurnaceTileEntity)world.getTileEntity(new BlockPos(x, y, z)));

        return null;

    }
}