package com.blunix.blunixsuicide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.EntityType;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.blunix.blunixsuicide.commands.CommandCompleter;
import com.blunix.blunixsuicide.commands.CommandRunner;
import com.blunix.blunixsuicide.events.MobAttack;
import com.blunix.blunixsuicide.events.MobTarget;
import com.blunix.blunixsuicide.events.SuicideHandler;
import com.blunix.blunixsuicide.models.KillerMob;
import com.blunix.blunixsuicide.util.ConfigManager;

public class BlunixSuicide extends JavaPlugin {
	private List<EntityType> killerMobTypes = new ArrayList<>();
	private List<KillerMob> killerMobs = new ArrayList<>();

	private Map<UUID, Integer> heartAttackPlayers = new HashMap<>();

	@Override
	public void onEnable() {
		saveDefaultConfig();

		getCommand("suicide").setExecutor(new CommandRunner(this));
		getCommand("suicide").setTabCompleter(new CommandCompleter());

		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new SuicideHandler(this), this);
		pm.registerEvents(new MobTarget(), this);
		pm.registerEvents(new MobAttack(), this);

		registerKillerMobTypes();
	}

	@Override
	public void onDisable() {

	}

	public static BlunixSuicide getInstance() {
		return BlunixSuicide.getPlugin(BlunixSuicide.class);
	}

	public void registerKillerMobTypes() {
		ConfigManager config = new ConfigManager(this);
		killerMobTypes = config.getKillerMobTypes();
	}

	public List<EntityType> getKillerMobTypes() {
		return killerMobTypes;
	}

	public List<KillerMob> getKillerMobs() {
		return killerMobs;
	}
	
	public Map<UUID, Integer> getHeartAttackPlayers() {
		return heartAttackPlayers;
	}
}
