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
package wirelessredstone.addon.powerdirector.proxy;

import java.io.File;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import wirelessredstone.addon.powerdirector.core.PowerDirector;
import wirelessredstone.addon.powerdirector.network.packets.PacketPowerDirectorCommands;
import wirelessredstone.addon.powerdirector.network.packets.executors.PacketPowerDirectorSettingsExecutor;
import wirelessredstone.addon.powerdirector.overrides.BlockRedstoneWirelessROverridePC;
import wirelessredstone.api.ICommonProxy;
import wirelessredstone.core.WRCore;
import wirelessredstone.inventory.ContainerRedstoneWireless;
import wirelessredstone.network.ServerPacketHandler;
import wirelessredstone.network.packets.core.PacketIds;
import cpw.mods.fml.common.network.NetworkRegistry;

public class PowerDirectorCommonProxy implements ICommonProxy {

	@Override
	public void registerRenderInformation() {
	}

	@Override
	public void registerConfiguration(File configFile) {
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return new ContainerRedstoneWireless(world.getBlockTileEntity(	x,
																		y,
																		z));
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

	@Override
	public String getMinecraftDir() {
		return ".";
	}

	@Override
	public void registerTileEntitySpecialRenderer(Class<? extends TileEntity> clazz) {

	}

	@Override
	public void init() {
		NetworkRegistry.instance().registerGuiHandler(	PowerDirector.instance,
														PowerDirector.proxy);
		PacketPowerDirectorCommands.registerCommands();
	}

	@Override
	public World getWorld(NetHandler handler) {
		return null;
	}

	@Override
	public void login(NetHandler handler, INetworkManager manager, Packet1Login login) {
	}

	@Override
	public void initPacketHandlers() {
		// ///////////////////
		// Server Executor //
		// ///////////////////
		ServerPacketHandler.getPacketHandler(PacketIds.ADDON).registerPacketHandler(PacketPowerDirectorCommands.powerConfigCommands.setDirection.toString(),
																					new PacketPowerDirectorSettingsExecutor());
		ServerPacketHandler.getPacketHandler(PacketIds.ADDON).registerPacketHandler(PacketPowerDirectorCommands.powerConfigCommands.setInDirection.toString(),
																					new PacketPowerDirectorSettingsExecutor());
	}

	@Override
	public void connectionClosed(INetworkManager manager) {
	}

	@Override
	public void addOverrides() {
		WRCore.addOverrideToReceiver(new BlockRedstoneWirelessROverridePC());
	}
}
