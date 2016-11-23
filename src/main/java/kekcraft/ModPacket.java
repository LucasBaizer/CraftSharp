package kekcraft;

import java.io.IOException;

import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import kekcraft.api.ui.GeneratorTileEntity;
import kekcraft.api.ui.MachineTileEntity;
import kekcraft.api.ui.energy.ElectricMachineTileEntity;
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

	public static FMLProxyPacket createMachinePacket(MachineTileEntity machine) throws IOException {
		ByteBufOutputStream out = new ByteBufOutputStream(Unpooled.buffer());

		out.writeInt(machine.xCoord);
		out.writeInt(machine.yCoord);
		out.writeInt(machine.zCoord);

		if (machine instanceof GeneratorTileEntity) {
			out.writeInt(((GeneratorTileEntity) machine).getEnergy().getEnergyStored());
		} else {
			if (machine instanceof ElectricMachineTileEntity) {
				ElectricMachineTileEntity e = (ElectricMachineTileEntity) machine;
				out.writeInt(e.getEnergy().getEnergyStored());
			}
			out.writeInt(machine.getCurrentCookTime());
			out.writeInt(machine.getCookTime());
		}

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
				if (entity instanceof GeneratorTileEntity) {
					((GeneratorTileEntity) entity).getEnergy().setEnergyStored(in.readInt());
				} else {
					if (entity instanceof ElectricMachineTileEntity) {
						ElectricMachineTileEntity e = (ElectricMachineTileEntity) entity;
						e.getEnergy().setEnergyStored(in.readInt());
					}
					entity.setCurrentCookTime(in.readInt());
					entity.setCookTime(in.readInt());
				}

				in.close();
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	public static void processPacketOnServerSide(ByteBuf payload, Side parSide) {
		if (parSide == Side.SERVER) {
			// nothing send to server my client
		}
	}

	public static TileEntity getTileEntityByID(int x, int y, int z, World world) {
		return world.getTileEntity(x, y, z);
	}
}
