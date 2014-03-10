/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version. This program is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU
 * Lesser General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>
 */
package com.slimevoid.wirelessredstone.block;

import java.util.Random;

import com.slimevoid.wirelessredstone.core.WirelessRedstone;
import com.slimevoid.wirelessredstone.core.lib.GuiLib;
import com.slimevoid.wirelessredstone.core.lib.IconLib;
import com.slimevoid.wirelessredstone.ether.RedstoneEther;
import com.slimevoid.wirelessredstone.tileentity.TileEntityRedstoneWireless;
import com.slimevoid.wirelessredstone.tileentity.TileEntityRedstoneWirelessT;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Redstone Wireless Transmitter
 * 
 * @author ali4z
 * 
 */
public class BlockRedstoneWirelessT extends BlockRedstoneWireless {

    @Override
    public void registerIcons(IIconRegister iconRegister) {
        this.iconBuffer = new IIcon[2][6];
        this.iconBuffer[0][0] = iconRegister.registerIcon(IconLib.WIRELESS_BOTTOM_OFF);
        this.iconBuffer[0][1] = iconRegister.registerIcon(IconLib.WIRELESS_TOP_OFF);
        this.iconBuffer[0][2] = iconRegister.registerIcon(IconLib.WIRELESS_TX_SIDE_OFF);
        this.iconBuffer[0][3] = iconRegister.registerIcon(IconLib.WIRELESS_TX_FRONT_OFF);
        this.iconBuffer[0][4] = iconRegister.registerIcon(IconLib.WIRELESS_TX_SIDE_OFF);
        this.iconBuffer[0][5] = iconRegister.registerIcon(IconLib.WIRELESS_TX_SIDE_OFF);
        this.iconBuffer[1][0] = iconRegister.registerIcon(IconLib.WIRELESS_BOTTOM_ON);
        this.iconBuffer[1][1] = iconRegister.registerIcon(IconLib.WIRELESS_TOP_ON);
        this.iconBuffer[1][2] = iconRegister.registerIcon(IconLib.WIRELESS_TX_SIDE_ON);
        this.iconBuffer[1][3] = iconRegister.registerIcon(IconLib.WIRELESS_TX_FRONT_ON);
        this.iconBuffer[1][4] = iconRegister.registerIcon(IconLib.WIRELESS_TX_SIDE_ON);
        this.iconBuffer[1][5] = iconRegister.registerIcon(IconLib.WIRELESS_TX_SIDE_ON);
    }

    /**
     * Constructor.<br>
     * Sets the Block ID.
     * 
     * @param i
     *            Block ID
     */
    public BlockRedstoneWirelessT(int i, float hardness, float resistance) {
        super(i, hardness, resistance);
        setStepSound(Block.soundTypeMetal);
    }

    /**
     * Stores state in metadata and marks Block for update.<br>
     * - Sets the transmitter state in the Ether.
     */
    @Override
    public void setState(World world, int i, int j, int k, boolean state) {
        super.setState(world,
                       i,
                       j,
                       k,
                       state);

        String freq = getFreq(world,
                              i,
                              j,
                              k);
        RedstoneEther.getInstance().setTransmitterState(world,
                                                        i,
                                                        j,
                                                        k,
                                                        freq,
                                                        state);
    }

    /**
     * Changes the frequency.<br>
     * - Remove transmitter from old frequency.<br>
     * - Add transmitter to new frequency.<br>
     * - Set transmitter state in the Ether.
     */
    @Override
    public void changeFreq(World world, int i, int j, int k, Object oldFreq, Object freq) {
        // Remove transmitter from current frequency on the ether.
        RedstoneEther.getInstance().remTransmitter(world,
                                                   i,
                                                   j,
                                                   k,
                                                   oldFreq);

        // Add transmitter to the ether on new frequency.
        RedstoneEther.getInstance().addTransmitter(world,
                                                   i,
                                                   j,
                                                   k,
                                                   freq);
        RedstoneEther.getInstance().setTransmitterState(world,
                                                        i,
                                                        j,
                                                        k,
                                                        freq,
                                                        getState(world,
                                                                 i,
                                                                 j,
                                                                 k));
    }

    /**
     * Is triggered after the Block and TileEntity is added to the world.<br>
     * - Add transmitter to ether.<br>
     * - Notify self of neighbor change. (forcing transmitter to check for
     * neighboring power sources)
     */
    @Override
    protected void onBlockRedstoneWirelessAdded(World world, int i, int j, int k) {
        RedstoneEther.getInstance().addTransmitter(world,
                                                   i,
                                                   j,
                                                   k,
                                                   getFreq(world,
                                                           i,
                                                           j,
                                                           k));

        onBlockRedstoneWirelessNeighborChange(world,
                                              i,
                                              j,
                                              k,
                                              Blocks.redstone_wire);
    }

    /**
     * Is triggered after the Block is removed from the world and before the
     * TileEntity is removed.<br>
     * - Remove transmitter from ether.
     */
    @Override
    protected void onBlockRedstoneWirelessRemoved(World world, int i, int j, int k) {
        RedstoneEther.getInstance().remTransmitter(world,
                                                   i,
                                                   j,
                                                   k,
                                                   getFreq(world,
                                                           i,
                                                           j,
                                                           k));
    }

    /**
     * Is triggered on Block activation unless overrides exits prematurely.<br>
     * - Associates the TileEntity with the GUI. - Opens the GUI.
     */
    @Override
    protected boolean onBlockRedstoneWirelessActivated(World world, int i, int j, int k, EntityPlayer entityplayer) {
        TileEntity tileentity = world.getTileEntity(i,
                                                    j,
                                                    k);

        if (tileentity != null
            && tileentity instanceof TileEntityRedstoneWirelessT) {
            entityplayer.openGui(WirelessRedstone.instance,
                                 GuiLib.GUIID_INVENTORY,
                                 world,
                                 i,
                                 j,
                                 k);
        }

        return true;
    }

    /**
     * Is triggered on Block's neighboring Block change.<br>
     * - Exit if the neighboring Block is not of the same type. - If Block is
     * getting powered directly or indirectly, and the power is not already on,
     * set the power on. - If Block is not getting powered directly or
     * indirectly, and the power is already on, set the power off.
     */
    @Override
    protected void onBlockRedstoneWirelessNeighborChange(World world, int i, int j, int k, Block block) {
        if (block.equals(this)) return;

        // It was not removed, can provide power and is indirectly getting
        // powered.
        if (block != null
            && !block.equals(Blocks.air)
            && !getState(world,
                         i,
                         j,
                         k)
            && (world.getBlockPowerInput(i,
                                         j,
                                         k) > 0 || world.isBlockIndirectlyGettingPowered(i,
                                                                                         j,
                                                                                         k))) setState(world,
                                                                                                       i,
                                                                                                       j,
                                                                                                       k,
                                                                                                       true);
        // There are no powering entities, state is deactivated.
        else if (getState(world,
                          i,
                          j,
                          k)
                 && !(world.getBlockPowerInput(i,
                                               j,
                                               k) > 0 || world.isBlockIndirectlyGettingPowered(i,
                                                                                               j,
                                                                                               k))) setState(world,
                                                                                                             i,
                                                                                                             j,
                                                                                                             k,
                                                                                                             false);
    }

    @Override
    protected IIcon getBlockRedstoneWirelessTexture(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        if (getState(iblockaccess,
                     i,
                     j,
                     k)) {
            return this.getIconFromStateAndSide(1,
                                                l);
        } else {
            return getBlockRedstoneWirelessTextureFromSide(l);
        }
    }

    @Override
    protected IIcon getBlockRedstoneWirelessTextureFromSide(int l) {
        return this.getIconFromStateAndSide(0,
                                            l);
    }

    @Override
    protected TileEntityRedstoneWireless getBlockRedstoneWirelessEntity() {
        return new TileEntityRedstoneWirelessT();
    }

    /**
     * Does nothing.
     */
    @Override
    protected void updateRedstoneWirelessTick(World world, int i, int j, int k, Random random) {
    }

    /**
     * Always returns false.<br>
     * Transmitters never emitt power.
     */
    @Override
    protected int isRedstoneWirelessPoweringTo(World world, int i, int j, int k, int l) {
        return 0;
    }

    /**
     * Always returns false.<br>
     * Transmitters never emitt power.
     */
    @Override
    protected int isRedstoneWirelessIndirectlyPoweringTo(World world, int i, int j, int k, int l) {
        return 0;
    }

    @Override
    protected boolean isBlockRedstoneWirelessSolidOnSide(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
        return true;
    }

    @Override
    protected boolean isBlockRedstoneWirelessOpaqueCube() {
        return true;
    }
}