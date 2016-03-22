package com.flipnoter.advancedrfmachines.blocks;

import com.flipnoter.advancedrfmachines.advancedrfmachines;
import com.flipnoter.advancedrfmachines.gui.ModGUIHandler;
import com.flipnoter.advancedrfmachines.tileEntites.BasicSolarPanelTileEntity;
import com.flipnoter.advancedrfmachines.tileEntites.FurnaceGeneratorTileEntity;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * Created by Connor on 3/21/2016.
 */
public class BasicSolarPanel extends BlockContainer {

    protected BasicSolarPanel() {

        super(Material.rock);
        this.setUnlocalizedName("Basic_Solar_Panel");
        this.isBlockContainer = true;

    }

    @Override
    public boolean isOpaqueCube() {

        return true;

    }

    @Override
    public int getRenderType() {

        return 3;

    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {

        //if(!world.isRemote)
          //  player.openGui(advancedrfmachines.instance, ModGUIHandler.Furnace_Generator_GUI, world, pos.getX(), pos.getY(), pos.getZ());

        world.markBlockForUpdate(pos);

        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {

        return new BasicSolarPanelTileEntity();

    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {

        BasicSolarPanelTileEntity te = (BasicSolarPanelTileEntity)world.getTileEntity(pos);
        InventoryHelper.dropInventoryItems(world, pos, te);

        super.breakBlock(world, pos, state);

    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {

        if(stack.hasDisplayName()) {

            ((BasicSolarPanelTileEntity)worldIn.getTileEntity(pos)).setCustomName(stack.getDisplayName());

        }
    }

    @Override
    public boolean onBlockEventReceived(World worldIn, BlockPos pos, IBlockState state, int eventID, int eventParam) {

        super.onBlockEventReceived(worldIn, pos, state, eventID, eventParam);

        TileEntity tileentity = worldIn.getTileEntity(pos);

        return tileentity != null && tileentity.receiveClientEvent(eventID, eventParam);

    }
}
