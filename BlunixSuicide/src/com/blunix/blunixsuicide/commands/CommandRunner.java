package com.blunix.blunixsuicide.commands;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.blunix.blunixsuicide.BlunixSuicide;
import com.blunix.blunixsuicide.events.PlayerSuicideEvent;
import com.blunix.blunixsuicide.models.SuicideMethod;
import com.blunix.blunixsuicide.util.Messager;

public class CommandRunner implements CommandExecutor {
	private BlunixSuicide plugin;

	public CommandRunner(BlunixSuicide plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!cmd.getName().equalsIgnoreCase("suicide"))
			return true;

		if (args.length == 0) { // /suicide
			if (!(sender instanceof Player)) {
				Messager.sendMessage(sender, "Console Died.");
				return true;
			}
			Player player = (Player) sender;
			PlayerSuicideEvent suicideEvent = new PlayerSuicideEvent(player, getRandomSuicideMethod());
			Bukkit.getPluginManager().callEvent(suicideEvent);
			return true;

		} else if (args[0].equalsIgnoreCase("reload")) { // /suicide reload
			if (!sender.hasPermission("blunixsuicide.reload")) {
				Messager.sendNoPermissionMessage(sender);
				return true;
			}
			plugin.reloadConfig();
			Messager.sendMessage(sender, "&aConfig reloaded.");
			return true;

		} else if (args[0].equalsIgnoreCase("player") && args.length > 1) { // /suicide player
			if (!sender.hasPermission("blunixsuicide.suicide.admin")) {
				Messager.sendNoPermissionMessage(sender);
				return true;
			}
			Player player = Bukkit.getPlayer(args[1]);
			if (player == null) {
				Messager.sendMessage(sender, "&cUnknown player.");
				return true;
			}
			SuicideMethod method = null;
			if (args.length == 2) // /suicide player <Player>
				method = getRandomSuicideMethod();

			else if (args.length > 2) { // /suicide player <Player> <Method>
				try {
					method = SuicideMethod.valueOf(args[2]);
				} catch (Exception e) {
					Messager.sendMessage(sender, "&cUnknown suicide method.");
					return true;
				}
			}
			if (method == null) {
				Messager.sendMessage(sender, "&cYou must specify a valid suicide method.");
				return true;
			}
			Messager.sendMessage(sender, "&c player is now suffering.");
			PlayerSuicideEvent suicideEvent = new PlayerSuicideEvent(player, method);
			Bukkit.getPluginManager().callEvent(suicideEvent);
			return true;

		} else if (args[0].equalsIgnoreCase("help")) {
			Messager.sendHelpMessage(sender);
			return true;

		} else {
			Messager.sendMessage(sender, "&cUnknown command. Type &l/suicide help &cto see the full command list.");
			return true;
		}
	}

	private SuicideMethod getRandomSuicideMethod() {
		Random random = new Random();
		return SuicideMethod.values()[random.nextInt(SuicideMethod.values().length)];
	}
}
