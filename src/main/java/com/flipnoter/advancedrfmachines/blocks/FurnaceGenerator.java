package com.flipnoter.advancedrfmachines.blocks;

import com.flipnoter.advancedrfmachines.advancedrfmachines;
import com.flipnoter.advancedrfmachines.gui.ModGUIHandler;
import com.flipnoter.advancedrfmachines.items.ModItems;
import com.flipnoter.advancedrfmachines.tileEntites.FurnaceGeneratorTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by Connor on 3/5/2016.
 */
public class FurnaceGenerator extends BlockContainer {

    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    public FurnaceGenerator() {

        super(Material.iron);
        this.setUnlocalizedName("Furnace_Generator");
        this.setCreativeTab(ModItems.ADVRFMTab);
        this.setHardness(6f);
        this.setResistance(10f);
        this.setHarvestLevel("pickaxe", 3);
        this.isBlockContainer = true;

        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));

    }

    @Override
    protected BlockState createBlockState() {

        return new BlockState(this, new IProperty[] {FACING});

    }

    @Override
    public IBlockState getStateFromMeta(int meta) {

        EnumFacing facing = EnumFacing.getHorizontal(meta);

        return this.getDefaultState().withProperty(FACING, facing);

    }

    @Override
    public int getMetaFromState(IBlockState state) {

        EnumFacing facing = state.getValue(FACING);

        return facing.getHorizontalIndex();

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

        if(!world.isRemote)
            player.openGui(advancedrfmachines.instance, ModGUIHandler.Furnace_Generator_GUI, world, pos.getX(), pos.getY(), pos.getZ());

        world.markBlockForUpdate(pos);

        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {

        return new FurnaceGeneratorTileEntity();

    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {

        FurnaceGeneratorTileEntity te = (FurnaceGeneratorTileEntity)world.getTileEntity(pos);
        InventoryHelper.dropInventoryItems(world, pos, te);

        super.breakBlock(world, pos, state);

    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {

        this.setDefaultFacing(worldIn, pos, state);

    }

    private void setDefaultFacing(World worldIn, BlockPos pos, IBlockState state) {

        if(!worldIn.isRemote) {

            Block block = worldIn.getBlockState(pos.north()).getBlock();
            Block block1 = worldIn.getBlockState(pos.south()).getBlock();
            Block block2 = worldIn.getBlockState(pos.west()).getBlock();
            Block block3 = worldIn.getBlockState(pos.east()).getBlock();
            EnumFacing enumfacing = state.getValue(FACING);

            if(enumfacing == EnumFacing.NORTH && block.isFullBlock() && !block1.isFullBlock()) {

                enumfacing = EnumFacing.SOUTH;

            }
            else if(enumfacing == EnumFacing.SOUTH && block1.isFullBlock() && !block.isFullBlock()) {

                enumfacing = EnumFacing.NORTH;

            }
            else if(enumfacing == EnumFacing.WEST && block2.isFullBlock() && !block3.isFullBlock()) {

                enumfacing = EnumFacing.EAST;

            }
            else if(enumfacing == EnumFacing.EAST && block3.isFullBlock() && !block2.isFullBlock()) {

                enumfacing = EnumFacing.WEST;

            }

            worldIn.setBlockState(pos, state.withProperty(FACING, enumfacing), 2);

        }
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {

        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());

    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {

        worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);

        if(stack.hasDisplayName()) {

            ((FurnaceGeneratorTileEntity)worldIn.getTileEntity(pos)).setCustomName(stack.getDisplayName());

        }
    }

    @Override
    public boolean onBlockEventReceived(World worldIn, BlockPos pos, IBlockState state, int eventID, int eventParam) {

        super.onBlockEventReceived(worldIn, pos, state, eventID, eventParam);

        TileEntity tileentity = worldIn.getTileEntity(pos);

        return tileentity != null && tileentity.receiveClientEvent(eventID, eventParam);

    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {

        super.updateTick(worldIn, pos, state, rand);

        if(((FurnaceGeneratorTileEntity)worldIn.getTileEntity(pos)).getTimeRemaining() > 0) {

            this.setLightLevel(1);

        }
        else
        {

            this.setLightLevel(0);

        }

        worldIn.markBlockForUpdate(pos);

    }
}
