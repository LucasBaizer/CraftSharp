package com.craftsharp.cmd;

import java.util.Arrays;
import java.util.List;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class DoxCommand implements ICommand {
	@Override
	public int compareTo(Object o) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "dox";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/dox <username>";
	}

	@Override
	public List<String> getCommandAliases() {
		return Arrays.asList("dox");
	}

	@Override
	public void processCommand(ICommandSender sender, String[] argString) {
		World world = sender.getEntityWorld();

		if (!world.isRemote) {
			if (argString.length == 0) {
				sender.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
				return;
			}
			@SuppressWarnings("unchecked")
			List<EntityPlayerMP> players = FMLClientHandler.instance().getServer()
					.getConfigurationManager().playerEntityList;
			for (EntityPlayerMP player : players) {
				if (player.getDisplayName().equalsIgnoreCase(argString[0])) {
					MinecraftServer.getServer().getConfigurationManager()
							.sendChatMsg(new ChatComponentText(player.getDisplayName() + " just got DOXED!"));
					MinecraftServer.getServer().getConfigurationManager()
							.sendChatMsg(new ChatComponentText(player.getDisplayName() + " is at " + (int) player.posX
									+ ", " + (int) player.posY + ", " + (int) player.posZ + "."));
					return;
				}
			}
			sender.addChatMessage(new ChatComponentText("Unknown player: " + argString[0]));
		}
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender var1) {
		return true;
	}

	@Override
	public List<?> addTabCompletionOptions(ICommandSender var1, String[] var2) {
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] var1, int var2) {
		return var2 == 0;
	}
}