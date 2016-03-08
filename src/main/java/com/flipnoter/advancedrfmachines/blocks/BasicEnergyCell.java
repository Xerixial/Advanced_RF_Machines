package com.flipnoter.advancedrfmachines.blocks;

import com.flipnoter.advancedrfmachines.advancedrfmachines;
import com.flipnoter.advancedrfmachines.gui.ModGUIHandler;
import com.flipnoter.advancedrfmachines.items.ModItems;
import com.flipnoter.advancedrfmachines.tileEntites.BasicEnergyCellTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockPistonBase;
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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Created by Connor on 3/5/2016.
 */
public class BasicEnergyCell extends BlockContainer {

    public static final PropertyDirection FACING = PropertyDirection.create("facing");

    public BasicEnergyCell() {

        super(Material.iron);
        this.setUnlocalizedName("Basic_EnergyCell");
        this.setCreativeTab(ModItems.ADVRFMTab);
        this.setHardness(6f);
        this.setResistance(10f);
        this.setHarvestLevel("pickaxe", 3);
        this.isBlockContainer = true;
        this.setLightOpacity(0);

        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));

        this.setBlockBounds(4f / 16f, 0, 4f / 16f, 12f / 16f, 1, 12f / 16f);

    }

    @Override
    protected BlockState createBlockState() {

        return new BlockState(this, new IProperty[] {FACING});

    }

    @Override
    public IBlockState getStateFromMeta(int meta) {

        EnumFacing facing = EnumFacing.values()[meta];

        return this.getDefaultState().withProperty(FACING, facing);

    }

    @Override
    public int getMetaFromState(IBlockState state) {

        EnumFacing facing = state.getValue(FACING);

        return facing.getIndex();

    }

    @Override
    public boolean isOpaqueCube() {

        return false;

    }

    @Override
    public int getRenderType() {

        return 3;

    }

    @Override
    public boolean isFullBlock() {

        return false;

    }

    @Override
    public boolean isFullCube() {

        return false;

    }

    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess world, final BlockPos pos) {

        EnumFacing front = EnumFacing.values()[getMetaFromState(world.getBlockState(pos))];

        if(front == EnumFacing.DOWN || front == EnumFacing.UP)
            setBlockBounds(4f / 16f, 0, 4f / 16f, 12f / 16f, 1, 12f / 16f);

        if(front == EnumFacing.NORTH || front == EnumFacing.SOUTH)
            setBlockBounds(0, 4f / 16f, 4f / 16f, 1, 12f / 16f, 12f / 16f);

        if(front == EnumFacing.EAST || front == EnumFacing.WEST)
            setBlockBounds(4f / 16f, 4f / 16f, 0, 12f / 16f, 12f / 16f, 1);

    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {

        if(!world.isRemote)
            //player.openGui(advancedrfmachines.instance, ModGUIHandler.Powered_Furnace_GUI, world, pos.getX(), pos.getY(), pos.getZ());

        world.markBlockForUpdate(pos);

        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {

        return new BasicEnergyCellTileEntity();

    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {

        BasicEnergyCellTileEntity te = (BasicEnergyCellTileEntity)world.getTileEntity(pos);
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
            Block block4 = worldIn.getBlockState(pos.up()).getBlock();
            Block block5 = worldIn.getBlockState(pos.down()).getBlock();

            EnumFacing enumfacing = state.getValue(FACING);

            if(enumfacing == EnumFacing.DOWN && block5.isFullBlock() && !block4.isFullBlock()) {

                enumfacing = EnumFacing.UP;

            }
            else if(enumfacing == EnumFacing.UP && block4.isFullBlock() && !block5.isFullBlock()) {

                enumfacing = EnumFacing.DOWN;

            }
            else if(enumfacing == EnumFacing.NORTH && block.isFullBlock() && !block1.isFullBlock()) {

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

        return this.getDefaultState().withProperty(FACING, BlockPistonBase.getFacingFromEntity(worldIn, pos, placer));

    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {

        worldIn.setBlockState(pos, state.withProperty(FACING, BlockPistonBase.getFacingFromEntity(worldIn, pos, placer)), 2);

        if(stack.hasDisplayName()) {

            ((BasicEnergyCellTileEntity)worldIn.getTileEntity(pos)).setCustomName(stack.getDisplayName());

        }
    }

    @Override
    public boolean onBlockEventReceived(World worldIn, BlockPos pos, IBlockState state, int eventID, int eventParam) {

        super.onBlockEventReceived(worldIn, pos, state, eventID, eventParam);

        TileEntity tileentity = worldIn.getTileEntity(pos);

        return tileentity != null && tileentity.receiveClientEvent(eventID, eventParam);

    }
}
