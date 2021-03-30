package com.blunix.blunixsuicide.util;

import com.blunix.blunixsuicide.BlunixSuicide;
import com.blunix.blunixsuicide.models.SuicideMethod;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class ConfigManager {
	private BlunixSuicide plugin;

	public ConfigManager(BlunixSuicide plugin) {
		this.plugin = plugin;
	}

	public long getKillTime() {
		return (long) plugin.getConfig().getDouble("kill-time") * 20;
	}

	public String getDeathMessage(SuicideMethod method) {
		return ColorUtil.formatColor(
				plugin.getConfig().getString("suicide-messages." + method.name().toLowerCase().replace("_", "-")));
	}

	public List<EntityType> getKillerMobTypes() {
		List<String> typeNames = plugin.getConfig().getStringList("killer-mobs");
		List<EntityType> types = new ArrayList<>();
		for (String typeName : typeNames) {
			EntityType type;
			try {
				type = EntityType.valueOf(typeName);
			} catch (Exception e) {
				Bukkit.getLogger()
						.info("[BlunixSuicide] There was an error reading \"" + typeName + "\" from config.yml");
				continue;
			}
			types.add(type);
		}
		return types;
	}
}
