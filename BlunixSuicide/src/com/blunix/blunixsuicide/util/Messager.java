package com.blunix.blunixsuicide.util;

import org.bukkit.command.CommandSender;

public class Messager {

	public static void sendMessage(CommandSender sender, String message) {
		sender.sendMessage(ColorUtil.formatColor(message));
	}

	public static void sendHelpMessage(CommandSender sender) {
		String message = "&6&lCommands\n";
		if (sender.hasPermission("blunixsuicide.suicide"))
			message += "&4/suicide &6- &cStarts a random suicide animation.\n";
		if (sender.hasPermission("blunixsuicide.suicide.admin")) {
			message += "&4/suicide player <Player> &6 &cStarts a random suicide animation for the specified player.\n";
			message += "&4/suicide player <Player> <Method> &6 &cStarts the specified suicide animation for the specified player.\n";
		}
		if (sender.hasPermission("blunixsuicide.reload"))
			message += "&4/suicide reload &6- &cReloads the plugin's config.\n";
		
		message += "&4/suicide help &6- &cDisplays this list.\n";
		
		sendMessage(sender, message);
	}

	public static void sendNoPermissionMessage(CommandSender sender) {
		sendMessage(sender, "&cYou do not have permissions to use this command!");
	}
}
