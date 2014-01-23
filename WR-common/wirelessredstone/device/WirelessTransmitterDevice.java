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
package wirelessredstone.device;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import wirelessredstone.ether.RedstoneEther;

public abstract class WirelessTransmitterDevice extends WirelessDevice {

	public WirelessTransmitterDevice(World world, EntityLivingBase entityliving) {
		super(world, entityliving);
	}

	@Override
	public String getInvName() {
		return "wirelessredstone.device.transmitter";
		// Wireless Transmitting Device
	}

	@Override
	public void doActivateCommand() {
		RedstoneEther.getInstance().addTransmitter(	this.getWorld(),
													this.xCoord,
													this.yCoord,
													this.zCoord,
													this.getFreq());
		RedstoneEther.getInstance().setTransmitterState(this.getWorld(),
														this.xCoord,
														this.yCoord,
														this.zCoord,
														this.getFreq(),
														true);
	}

	@Override
	public void doDeactivateCommand() {
		RedstoneEther.getInstance().remTransmitter(	this.getWorld(),
													this.xCoord,
													this.yCoord,
													this.zCoord,
													this.getFreq());
	}

}
