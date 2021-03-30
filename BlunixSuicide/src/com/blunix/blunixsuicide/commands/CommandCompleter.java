package com.blunix.blunixsuicide.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.blunix.blunixsuicide.models.SuicideMethod;

public class CommandCompleter implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		ArrayList<String> arguments = new ArrayList<>();
		if (arguments.isEmpty()) {
			arguments.add("help");
			if (sender.hasPermission("blunixsuicide.suicide.admin"))
				arguments.add("player");
			if (sender.hasPermission("blunixsuicide.reload"))
				arguments.add("reload");
		}
		
		if (args.length >= 0 && args.length < 2)
			return getCompletion(arguments, args, 0);

		else if (args.length >= 1 && args.length < 3 && args[0].equalsIgnoreCase("player") && arguments.size() > 1)
			return null;

		else if (args.length >= 2 && args.length < 4 && args[0].equalsIgnoreCase("player") && arguments.size() > 1)
			return getCompletion(getMethodNames(), args, 2);

		arguments.clear();
		return arguments;
	}

	private ArrayList<String> getCompletion(ArrayList<String> arguments, String[] args, int index) {
		ArrayList<String> results = new ArrayList<>();
		for (String argument : arguments) {
			if (!argument.toLowerCase().startsWith(args[index].toLowerCase()))
				continue;

			results.add(argument);
		}
		return results;
	}

	private ArrayList<String> getMethodNames() {
		ArrayList<String> names = new ArrayList<>();
		for (SuicideMethod method : SuicideMethod.values())
			names.add(method.name());

		return names;
	}
}
