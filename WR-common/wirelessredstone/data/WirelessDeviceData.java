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
package wirelessredstone.data;

import java.lang.reflect.Constructor;

import wirelessredstone.core.objectfactory.WirelessDeviceDataFactory;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;
import net.minecraft.src.WorldSavedData;

/**
 * Wireless device data.<br>
 * Contains ID, name, dimensions, frequency and state.<br>
 * Used for storing item data.
 * 
 * @author Eurymachus
 * 
 */
public abstract class WirelessDeviceData extends WorldSavedData {
	protected int id;
	protected String name;
	protected Byte dimension;
	protected String freq;
	protected boolean state;

	public WirelessDeviceData(String par1Str) {
		super(par1Str);
	}

	/**
	 * Set the device's ID based on a itemstack
	 * 
	 * @param itemstack The itemstack.
	 */
	public void setID(ItemStack itemstack) {
		this.setID(itemstack.getItemDamage());
	}

	/**
	 * Set the device's ID.
	 * 
	 * @param id Device ID.
	 */
	public void setID(int id) {
		this.id = id;
		this.markDirty();
	}

	/**
	 * Set the device's name.
	 * 
	 * @param name Device name.
	 */
	public void setName(String name) {
		this.name = name;
		this.markDirty();
	}

	/**
	 * Set the device's dimension based on world's worldType.
	 * 
	 * @param world The world object.
	 */
	public void setDimension(World world) {
		this.dimension = (byte) world.provider.dimensionId;
		this.markDirty();
	}

	/**
	 * Set the device's frequency.
	 * 
	 * @param freq Device frequency.
	 */
	public void setFreq(String freq) {
		this.freq = freq;
		this.markDirty();
	}

	/**
	 * Set the device's state.
	 * 
	 * @param state Device state.
	 */
	public void setState(boolean state) {
		this.state = state;
		this.markDirty();
	}

	/**
	 * Get the device ID.
	 * 
	 * @return Device ID.
	 */
	public int getID() {
		return this.id;
	}

	/**
	 * Get the device name.
	 * 
	 * @return Device name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get the device's dimension<br>
	 * 0 for normal world -1 for hell
	 * 
	 * @return Device dimension.
	 */
	public Byte getDimension() {
		return this.dimension;
	}

	/**
	 * Get the device's frequency.
	 * 
	 * @return Device frequency.
	 */
	public String getFreq() {
		return this.freq;
	}

	/**
	 * Get the device's state.
	 * 
	 * @return Device state.
	 */
	public boolean getState() {
		return this.state;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		this.id = nbttagcompound.getInteger("id");
		this.name = nbttagcompound.getString("name");
		this.dimension = nbttagcompound.getByte("dimension");
		this.freq = nbttagcompound.getString("freq");
		this.state = nbttagcompound.getBoolean("state");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setInteger("id", this.id);
		nbttagcompound.setString("name", this.name);
		nbttagcompound.setByte("dimension", this.dimension);
		nbttagcompound.setString("freq", this.freq);
		nbttagcompound.setBoolean("state", this.state);
	}

	public static WirelessDeviceData getDeviceData(Class<? extends WirelessDeviceData> wirelessData, String index, int id, String name, World world, EntityPlayer entityplayer) {
		index = index + "[" + id + "]";
		WirelessDeviceData data = WirelessDeviceDataFactory
				.getDeviceDataFromFactory(
						world,
						wirelessData,
						index,
						true
				);
		if (data == null) {
			data = WirelessDeviceDataFactory
					.getDeviceDataFromFactory(
							world,
							wirelessData,
							index,
							false
					);
			if (data != null) {
				world.setItemData(index, data);
				data.setID(id);
				data.setName(name);
				data.setDimension(world);
				data.setFreq("0");
				data.setState(false);
			} else {
				LoggerRedstoneWireless.getInstance(
						"WirelessDeviceData"
				).write(
						world.isRemote,
						"Index: " + index + ", not Found",
						LoggerRedstoneWireless.LogLevel.DEBUG
				);
			}
		}
		return data;
	}

	public static WirelessDeviceData getDeviceData(Class <? extends WirelessDeviceData> wirelessData, String defaultName, ItemStack itemstack, World world, EntityPlayer entityplayer) {
		String index = itemstack.getItem().getItemName();
		int id = itemstack.getItemDamage();
		String name = defaultName;
		return getDeviceData(wirelessData, index, id, name, world, entityplayer);
	}

}