package wirelessredstone.api;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.World;
import wirelessredstone.network.packets.PacketWireless;

public interface IDevicePacketExecutor extends IPacketExecutor {
	/**
	 * Execute the packet.
	 * 
	 * @param packet The redstone wireless device packet.
	 * @param world The world object.
	 * @param entityplayer the player
	 */
	public void execute(PacketWireless packet, World world, EntityPlayer entityplayer);
}
