package wirelessredstone.network.packets.executor;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import wirelessredstone.api.IEtherPacketExecutor;
import wirelessredstone.ether.RedstoneEther;
import wirelessredstone.network.packets.PacketWireless;
import wirelessredstone.tileentity.TileEntityRedstoneWireless;
import wirelessredstone.tileentity.TileEntityRedstoneWirelessT;

public class ClientEtherPacketTXAddExecutor implements IEtherPacketExecutor {

	@Override
	public void execute(PacketWireless packet, World world, EntityPlayer entityplayer) {
		TileEntity tileentity = packet.getTarget(world);
		if (
				tileentity != null && 
				tileentity instanceof TileEntityRedstoneWirelessT
		) {
			((TileEntityRedstoneWireless) tileentity).setFreq(packet.getDeviceFreq().toString());
/*		} else {
			tileentity = new TileEntityRedstoneWirelessT();
			((TileEntityRedstoneWireless) tileentity).setFreq(packet.getFreq().toString());
			world.setBlockTileEntity(
					packet.xPosition,
					packet.yPosition, 
					packet.zPosition, 
					tileentity
			);*/
		}
		RedstoneEther.getInstance().addTransmitter(
				world,
				packet.xPosition, 
				packet.yPosition, 
				packet.zPosition,
				packet.getDeviceFreq().toString()
		);
	}

}
