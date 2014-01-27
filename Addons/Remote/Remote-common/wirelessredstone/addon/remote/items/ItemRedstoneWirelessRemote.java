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
package wirelessredstone.addon.remote.items;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import wirelessredstone.addon.remote.core.WirelessRemote;
import wirelessredstone.addon.remote.core.lib.IconLib;
import wirelessredstone.addon.remote.core.lib.ItemLib;
import wirelessredstone.addon.remote.network.packets.PacketRemoteCommands;
import wirelessredstone.client.network.handlers.ClientRedstoneEtherPacketHandler;
import wirelessredstone.core.lib.GuiLib;
import wirelessredstone.core.lib.NBTHelper;
import wirelessredstone.core.lib.NBTLib;
import wirelessredstone.device.ItemWirelessDevice;
import wirelessredstone.tileentity.TileEntityRedstoneWirelessR;

public class ItemRedstoneWirelessRemote extends ItemWirelessDevice {

	public ItemRedstoneWirelessRemote(int i) {
		super(i);
		maxStackSize = 1;
	}

	@Override
	protected void registerIconList(IconRegister iconRegister) {
		this.iconList = new Icon[2];
		this.iconList[0] = iconRegister.registerIcon(IconLib.WIRELESS_REMOTE_OFF);
		this.iconList[1] = iconRegister.registerIcon(IconLib.WIRELESS_REMOTE_ON);
	}

	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l, float a, float b, float c) {
		return this.onItemUseFirst(	itemstack,
									entityplayer,
									world,
									i,
									j,
									k,
									l,
									a,
									b,
									c);
	}

	@Override
	public boolean onItemUseFirst(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l, float a, float b, float c) {
		if (entityplayer.isSneaking()) {
			TileEntity tileentity = world.getBlockTileEntity(	i,
																j,
																k);
			if (tileentity != null) {
				if (tileentity instanceof TileEntityRedstoneWirelessR) {
					if (world.isRemote) {
						ClientRedstoneEtherPacketHandler.sendRedstoneEtherPacket(	PacketRemoteCommands.remoteCommands.updateReceiver.toString(),
																					((TileEntityRedstoneWirelessR) tileentity).getBlockCoord(0),
																					((TileEntityRedstoneWirelessR) tileentity).getBlockCoord(1),
																					((TileEntityRedstoneWirelessR) tileentity).getBlockCoord(2),
																					this.getFreq(itemstack),
																					false);
					}
					return true;
				}
			}

			entityplayer.openGui(	WirelessRemote.instance,
									GuiLib.GUIID_DEVICE,
									world,
									(int) Math.round(entityplayer.posX),
									(int) Math.round(entityplayer.posY),
									(int) Math.round(entityplayer.posZ));
			return false;
		}
		Block block = Block.blocksList[world.getBlockId(i,
														j,
														k)];
		if (!block.onBlockActivated(world,
									i,
									j,
									k,
									entityplayer,
									l,
									a,
									b,
									c)) {
			this.onItemRightClick(	itemstack,
									world,
									entityplayer);
		}
		return false;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack itemstack) {
		return 72000;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack itemstack) {
		return EnumAction.none;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		if (!entityplayer.isSneaking()) {
			entityplayer.setItemInUse(	itemstack,
										this.getMaxItemUseDuration(itemstack));
		} else {
			this.onItemUseFirst(itemstack,
								entityplayer,
								world,
								(int) Math.round(entityplayer.posX),
								(int) Math.round(entityplayer.posY),
								(int) Math.round(entityplayer.posZ),
								0,
								0,
								0,
								0);
		}
		return itemstack;
	}

	@Override
	public void onUsingItemTick(ItemStack itemstack, EntityPlayer player, int count) {
		WirelessRemote.proxy.activateRemote(player.getEntityWorld(),
											player,
											itemstack);
		if (!getState(itemstack)) {
			setState(	itemstack,
						true);
		}
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack itemstack, World world, EntityPlayer entityplayer, int itemInUseCount) {
		WirelessRemote.proxy.deactivateRemote(	world,
												entityplayer,
												itemstack);
		setState(	itemstack,
					false);
	}

	@Override
	public void onUpdate(ItemStack itemstack, World world, Entity entity, int i, boolean isHeld) {
		if (ItemLib.isWirelessRemote(itemstack)) {
			if (!itemstack.hasTagCompound()) {
				itemstack.stackTagCompound = new NBTTagCompound();
				getFreq(itemstack);
				getState(itemstack);
			}
		}
		if (entity instanceof EntityLivingBase) {
			EntityLivingBase entityliving = (EntityLivingBase) entity;
			if (!isHeld) {
				WirelessRemote.proxy.deactivateRemote(	world,
														entityliving,
														itemstack);
				setState(	itemstack,
							false);
			}
		}
	}

	@Override
	public Object getFreq(ItemStack itemstack) {
		if (ItemLib.isWirelessRemote(itemstack)) {
			return NBTHelper.getString(	itemstack,
										NBTLib.FREQUENCY,
										"0");
		}
		return "0";
	}

	@Override
	public boolean getState(ItemStack itemstack) {
		if (ItemLib.isWirelessRemote(itemstack)) {
			return NBTHelper.getBoolean(itemstack,
										NBTLib.STATE,
										false);
		}
		return false;
	}

	@Override
	public void setFreq(ItemStack itemstack, Object freq) {
		if (ItemLib.isWirelessRemote(itemstack)) {
			NBTHelper.setString(itemstack,
								NBTLib.FREQUENCY,
								String.valueOf(freq));
		}
	}

	@Override
	public void setState(ItemStack itemstack, boolean state) {
		if (ItemLib.isWirelessRemote(itemstack)) {
			NBTHelper.setBoolean(	itemstack,
									NBTLib.STATE,
									state);
		}
	}

	@Override
	protected void doDroppedByPlayer(ItemStack itemstack, EntityPlayer entityplayer) {
		WirelessRemote.proxy.deactivateRemote(	entityplayer.getEntityWorld(),
												entityplayer,
												itemstack);
	}
}
