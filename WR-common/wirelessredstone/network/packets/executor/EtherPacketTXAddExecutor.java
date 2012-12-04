package wirelessredstone.network.packets.executor;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.World;
import wirelessredstone.api.IEtherPacketExecutor;
import wirelessredstone.ether.RedstoneEther;
import wirelessredstone.network.packets.PacketRedstoneEther;
import wirelessredstone.network.packets.PacketWireless;

/**
 * Execute a add transmitter command.<br>
 * <br>
 * Adds the transmitter to the ether.
 * 
 * @author ali4z
 */
public class EtherPacketTXAddExecutor implements IEtherPacketExecutor {

	@Override
	public void execute(PacketWireless packet, World world, EntityPlayer entityplayer) {
		RedstoneEther.getInstance().addTransmitter(
				world,
				packet.xPosition,
				packet.yPosition,
				packet.zPosition,
				packet.getFreq());
	}

}