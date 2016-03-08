package com.flipnoter.advancedrfmachines.blocks;

import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import cofh.api.energy.IEnergyStorage;
import com.flipnoter.advancedrfmachines.advancedrfmachines;
import com.flipnoter.advancedrfmachines.gui.ModGUIHandler;
import com.flipnoter.advancedrfmachines.items.ModItems;
import com.flipnoter.advancedrfmachines.tileEntites.BasicConduitTileEntity;
import com.flipnoter.advancedrfmachines.tileEntites.FurnaceGeneratorTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Created by Connor on 3/6/2016.
 */
public class BasicConduit extends Block implements ITileEntityProvider {

    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool WEST = PropertyBool.create("west");
    public static final PropertyBool UP = PropertyBool.create("up");
    public static final PropertyBool DOWN = PropertyBool.create("down");

    public BasicConduit() {

        super(Material.rock);
        this.setUnlocalizedName("Basic_Conduit");
        this.setCreativeTab(ModItems.ADVRFMTab);
        this.setHardness(6f);
        this.setResistance(10f);
        this.setHarvestLevel("pickaxe", 3);
        this.setLightOpacity(0);

        this.setDefaultState(this.blockState.getBaseState()
                .withProperty(WEST, false).withProperty(DOWN, false).withProperty(SOUTH, false).withProperty(EAST, false).withProperty(UP, false).withProperty(NORTH, false));

        this.setBlockBounds(6f / 16f, 6f / 16f, 6f / 16f, 10f / 16f, 10f / 16f, 10f / 16f);

    }

    @Override
    protected BlockState createBlockState() {

        return new BlockState(this, new IProperty[] { WEST, DOWN, SOUTH, EAST, UP, NORTH });

    }

    @Override
    public int getMetaFromState(IBlockState state) {

        return 0;

    }

    @Override
    public IBlockState getActualState(final IBlockState bs, final IBlockAccess world, final BlockPos coord) {

        IBlockState oldBS = bs;

        return bs
                .withProperty(WEST, this.canConnectTo(world ,coord, oldBS, EnumFacing.WEST, coord.west()))
                .withProperty(DOWN, this.canConnectTo(world, coord, oldBS, EnumFacing.DOWN, coord.down()))
                .withProperty(SOUTH, this.canConnectTo(world, coord, oldBS, EnumFacing.SOUTH, coord.south()))
                .withProperty(EAST, this.canConnectTo(world, coord, oldBS, EnumFacing.EAST, coord.east()))
                .withProperty(UP, this.canConnectTo(world, coord, oldBS, EnumFacing.UP, coord.up()))
                .withProperty(NORTH, this.canConnectTo(world, coord, oldBS, EnumFacing.NORTH, coord.north()));

    }

    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess world, final BlockPos coord) {

        final IBlockState bs = world.getBlockState(coord);

        IBlockState oldBS = bs;

        final boolean connectNorth = this.canConnectTo(world, coord, oldBS, EnumFacing.NORTH, coord.north());
        final boolean connectSouth = this.canConnectTo(world, coord, oldBS, EnumFacing.SOUTH, coord.south());
        final boolean connectWest = this.canConnectTo(world, coord, oldBS, EnumFacing.WEST, coord.west());
        final boolean connectEast = this.canConnectTo(world, coord, oldBS, EnumFacing.EAST, coord.east());
        final boolean connectUp = this.canConnectTo(world, coord, oldBS, EnumFacing.UP, coord.up());
        final boolean connectDown = this.canConnectTo(world, coord, oldBS, EnumFacing.DOWN, coord.down());

        float radius = 0.125f;
        float rminus = 0.5f - radius;
        float rplus = 0.5f + radius;

        float x1 = rminus;
        float x2 = rplus;
        float y1 = rminus;
        float y2 = rplus;
        float z1 = rminus;
        float z2 = rplus;

        if(connectNorth)
            z1 = 0.0f;

        if(connectSouth)
            z2 = 1.0f;

        if(connectWest)
            x1 = 0.0f;

        if(connectEast)
            x2 = 1.0f;

        if(connectDown)
            y1 = 0.0f;

        if(connectUp)
            y2 = 1.0f;

        this.setBlockBounds(x1, y1, z1, x2, y2, z2);
    }

    /**
     * Calculates the collision boxes for this block.
     */
    @Override
    public void addCollisionBoxesToList(final World world, final BlockPos coord, final IBlockState bs, final AxisAlignedBB box, final List collisionBoxList, final Entity entity) {

        IBlockState oldBS = bs;

        final boolean connectNorth = this.canConnectTo(world, coord, oldBS, EnumFacing.NORTH, coord.north());
        final boolean connectSouth = this.canConnectTo(world, coord, oldBS, EnumFacing.SOUTH, coord.south());
        final boolean connectWest = this.canConnectTo(world, coord, oldBS, EnumFacing.WEST, coord.west());
        final boolean connectEast = this.canConnectTo(world, coord, oldBS, EnumFacing.EAST, coord.east());
        final boolean connectUp = this.canConnectTo(world, coord, oldBS, EnumFacing.UP, coord.up());
        final boolean connectDown = this.canConnectTo(world, coord, oldBS, EnumFacing.DOWN, coord.down());

        float radius = 0.125f;
        float rminus = 0.5f - radius;
        float rplus = 0.5f + radius;

        this.setBlockBounds(rminus, rminus, rminus, rplus, rplus, rplus);
        super.addCollisionBoxesToList(world, coord, bs, box, collisionBoxList, entity);

        if(connectUp) {

            this.setBlockBounds(rminus, rminus, rminus, rplus, 1f, rplus);
            super.addCollisionBoxesToList(world, coord, bs, box, collisionBoxList, entity);

        }

        if(connectDown) {

            this.setBlockBounds(rminus, 0f, rminus, rplus, rplus, rplus);
            super.addCollisionBoxesToList(world, coord, bs, box, collisionBoxList, entity);

        }

        if(connectEast) {

            this.setBlockBounds(rminus, rminus, rminus, 1f, rplus, rplus);
            super.addCollisionBoxesToList(world, coord, bs, box, collisionBoxList, entity);

        }

        if(connectWest) {

            this.setBlockBounds(0f, rminus, rminus, rplus, rplus, rplus);
            super.addCollisionBoxesToList(world, coord, bs, box, collisionBoxList, entity);

        }

        if(connectSouth) {

            this.setBlockBounds(rminus, rminus, rminus, rplus, rplus, 1f);
            super.addCollisionBoxesToList(world, coord, bs, box, collisionBoxList, entity);

        }

        if(connectNorth) {

            this.setBlockBounds(rminus, rminus, 0f, rplus, rplus, rplus);
            super.addCollisionBoxesToList(world, coord, bs, box, collisionBoxList, entity);

        }
    }

    protected boolean canConnectTo(IBlockAccess w, BlockPos thisBlock, IBlockState bs, EnumFacing face, BlockPos otherBlock){

        TileEntity other = w.getTileEntity(otherBlock);

        if(other instanceof IEnergyHandler || other instanceof IEnergyProvider || other instanceof IEnergyReceiver) {

            if(other instanceof IEnergyHandler || other instanceof IEnergyProvider)
                return ((IEnergyProvider)other).canConnectEnergy(face);

            if(other instanceof IEnergyReceiver)
                return ((IEnergyReceiver)other).canConnectEnergy(face);

        }

        return false;

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
    public TileEntity createNewTileEntity(World world, int meta) {

        return new BasicConduitTileEntity();

    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {

        super.breakBlock(world, pos, state);

    }

    @Override
    public boolean onBlockEventReceived(World worldIn, BlockPos pos, IBlockState state, int eventID, int eventParam) {

        super.onBlockEventReceived(worldIn, pos, state, eventID, eventParam);

        TileEntity tileentity = worldIn.getTileEntity(pos);

        return tileentity != null && tileentity.receiveClientEvent(eventID, eventParam);

    }
}
