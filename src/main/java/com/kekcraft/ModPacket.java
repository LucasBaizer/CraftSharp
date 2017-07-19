package com.kekcraft;

import java.io.IOException;

import com.kekcraft.api.ui.MachineTileEntity;

import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ModPacket {
	public static void sendTileEntityUpdate(MachineTileEntity machine) {
		try {
			KekCraft.channel.sendToAll(createMachinePacket(machine));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static FMLProxyPacket createMachinePacket(SerializableEntity machine) throws IOException {
		ByteBufOutputStream out = new ByteBufOutputStream(Unpooled.buffer());
		machine.write(out);
		FMLProxyPacket thePacket = new FMLProxyPacket(out.buffer(), KekCraft.networkChannelName);
		out.close();

		return thePacket;
	}

	public static void processPacketOnClientSide(ByteBuf parBB, Side parSide) throws IOException {
		try {
			if (parSide == Side.CLIENT) {
				World world = Minecraft.getMinecraft().theWorld;
				ByteBufInputStream in = new ByteBufInputStream(parBB);

				int x = in.readInt();
				int y = in.readInt();
				int z = in.readInt();

				MachineTileEntity entity = (MachineTileEntity) getTileEntityByID(x, y, z, world);

				// checks to make sure entity was not destroyed between packet
				// send and packet receive
				if (entity != null) {
					entity.read(in);
				}
				in.close();
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	public static void processPacketOnServerSide(ByteBuf payload, Side parSide) {
		if (parSide == Side.SERVER) {
			// nothing sent to server by client
		}
	}

	public static TileEntity getTileEntityByID(int x, int y, int z, World world) {
		return world.getTileEntity(x, y, z);
	}
}
