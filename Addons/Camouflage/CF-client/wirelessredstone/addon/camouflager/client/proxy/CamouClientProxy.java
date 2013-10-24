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
package wirelessredstone.addon.camouflager.client.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import wirelessredstone.addon.camouflager.client.presentation.gui.GuiRedstoneWirelessCamouflager;
import wirelessredstone.addon.camouflager.core.CamouCore;
import wirelessredstone.addon.camouflager.core.WirelessCamouflager;
import wirelessredstone.addon.camouflager.proxy.CamouCommonProxy;
import wirelessredstone.api.IWirelessDeviceData;
import wirelessredstone.tileentity.TileEntityRedstoneWireless;
import cpw.mods.fml.common.network.NetworkRegistry;

/**
 * WRClientProxy class
 * 
 * Executes client specific code
 * 
 * @author Eurymachus
 * 
 */
public class CamouClientProxy extends CamouCommonProxy {

	/**
	 * Power Configurator GUI
	 */
	public static GuiRedstoneWirelessCamouflager	guiCamou;

	@Override
	public void init() {
		initGUIs();
		super.init();
	}

	/**
	 * Initializes GUI objects.
	 */
	public static void initGUIs() {
		guiCamou = new GuiRedstoneWirelessCamouflager();
		NetworkRegistry.instance().registerGuiHandler(	WirelessCamouflager.instance,
														CamouCore.proxy);
		// TODO :: Overrides
	}

	@Override
	public void registerRenderInformation() {
		loadBlockTextures();
	}

	/**
	 * Loads all Block textures from ModLoader override and stores the indices
	 * into the sprite integers.
	 */
	public static void loadBlockTextures() {
		// MinecraftForgeClient.preloadTexture("/WirelessSprites/terrain.png");
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileentity = world.getBlockTileEntity(	x,
															y,
															z);
		if (tileentity instanceof TileEntityRedstoneWireless) {
			guiCamou.assTileEntity((TileEntityRedstoneWireless) tileentity);
			return guiCamou;
		}
		return null;
	}

	@Override
	public String getMinecraftDir() {
		return Minecraft.getMinecraftDir().toString();
	}

	@Override
	public void registerTileEntitySpecialRenderer(Class<? extends TileEntity> clazz) {
	}

	@Override
	public void activateGUI(World world, EntityPlayer entityplayer, IWirelessDeviceData devicedata) {
		if (!world.isRemote) {
			super.activateGUI(	world,
								entityplayer,
								devicedata);
		}
	}

	/**
	 * Retrieves the world object with NetHandler parameters.
	 * 
	 * @return Minecraft world object.
	 */
	@Override
	public World getWorld(NetHandler handler) {
		if (handler instanceof NetClientHandler) {
			return ((NetClientHandler) handler).getPlayer().worldObj;
		}
		return null;
	}

	@Override
	public void login(NetHandler handler, INetworkManager manager, Packet1Login login) {
		World world = getWorld(handler);
		if (world != null) {
			// ClientPacketHandler.sendPacket(((new
			// PacketRedstoneEther(PacketRedstoneWirelessCommands.wirelessCommands.fetchEther.toString())).getPacket()));
		}
	}

	@Override
	public void initPacketHandlers() {
		super.initPacketHandlers();
		// ///////////////////
		// Client Handlers //
		// ///////////////////
		// ClientPacketHandler.getPacketHandler(PacketIds.GUI).registerPacketHandler(
		// PacketPowerConfigCommands.powerConfigCommands.openGui.toString(),
		// new ClientRemoteOpenGui());
	}
}