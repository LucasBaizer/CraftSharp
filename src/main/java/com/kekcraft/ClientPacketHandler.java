package com.kekcraft;

import java.io.IOException;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;

public class ClientPacketHandler extends ServerPacketHandler {
	@SubscribeEvent
	public void onClientPacket(ClientCustomPacketEvent event) throws IOException {
		channelName = event.packet.channel();

		if (channelName.equals(KekCraft.networkChannelName)) {
			ModPacket.processPacketOnClientSide(event.packet.payload(), event.packet.getTarget());
		}
	}
}