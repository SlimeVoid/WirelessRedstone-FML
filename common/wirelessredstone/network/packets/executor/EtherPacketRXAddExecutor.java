package wirelessredstone.network.packets.executor;

import net.minecraft.src.World;
import wirelessredstone.ether.RedstoneEther;
import wirelessredstone.network.packets.PacketRedstoneEther;

/**
 * Execute a add receiver command.<br>
 * <br>
 * Adds the receiver to the ether.
 * 
 * @author ali4z
 */
public class EtherPacketRXAddExecutor implements IEtherPacketExecutor {

	@Override
	public void execute(PacketRedstoneEther packet, World world) {
		RedstoneEther.getInstance().addReceiver(
				world,
				packet.xPosition,
				packet.yPosition,
				packet.zPosition,
				packet.getFreq());
	}

}