package com.craftsharp;

import java.io.IOException;

import com.craftsharp.api.ui.MachineTileEntity;

import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class ModPacket {
	public static void sendTileEntityUpdate(MachineTileEntity machine) {
		try {
			CraftSharp.channel.sendToAll(createMachinePacket(machine));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println(
					"An error occured while serializing a machine-- the update (server-to-clients) will not be sent!");
			System.err.println("If this happened while a world loaded, then it is likely not an issue.");
			e.printStackTrace();
		}
	}

	public static void sendTileEntityUpdateToServer(MachineTileEntity machine) {
		try {
			CraftSharp.channel.sendToServer(createMachinePacket(machine));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println(
					"An error occured while serializing a machine-- the update (client-to-server) will not be sent!");
			System.err.println("If this happened while a world loaded, then it is likely not an issue.");
			e.printStackTrace();
		}
	}

	public static FMLProxyPacket createMachinePacket(SerializableEntity machine) throws IOException {
		ByteBufOutputStream out = new ByteBufOutputStream(Unpooled.buffer());
		machine.write(out);
		FMLProxyPacket thePacket = new FMLProxyPacket(out.buffer(), CraftSharp.networkChannelName);
		out.close();

		return thePacket;
	}

	public static void processPacketOnClientSide(ByteBuf parBB, Side parSide) throws IOException {
		try {
			if (parSide == Side.CLIENT) {
				ByteBufInputStream in = new ByteBufInputStream(parBB);

				int dim = in.readInt();
				if (Minecraft.getMinecraft().theWorld.provider.dimensionId == dim) {
					int x = in.readInt();
					int y = in.readInt();
					int z = in.readInt();

					MachineTileEntity entity = (MachineTileEntity) Minecraft.getMinecraft().theWorld.getTileEntity(x, y,
							z);

					// checks to make sure entity was not destroyed between
					// packet
					// send and packet receive
					if (entity != null) {
						entity.read(in);
					}

				}
				in.close();
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	public static void processPacketOnServerSide(ByteBuf payload, Side parSide) throws IOException {
		if (parSide == Side.SERVER) {
			ByteBufInputStream in = new ByteBufInputStream(payload.copy());

			int dim = in.readInt();
			int x = in.readInt();
			int y = in.readInt();
			int z = in.readInt();

			World world = DimensionManager.getWorld(dim);

			MachineTileEntity entity = (MachineTileEntity) world.getTileEntity(x, y, z);

			// checks to make sure entity was not destroyed between
			// packet
			// send and packet receive
			if (entity != null) {
				entity.read(in);
			}
			
			in.close();

			CraftSharp.channel.sendToAll(new FMLProxyPacket(payload, CraftSharp.networkChannelName));
		}
	}
}
