package com.blunix.blunixsuicide.events;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.blunix.blunixsuicide.BlunixSuicide;
import com.blunix.blunixsuicide.util.SFXManager;

public class HeartAttackHandler implements Listener {
	private BlunixSuicide plugin;

	public HeartAttackHandler(BlunixSuicide plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		if (!plugin.getHeartAttackPlayers().containsKey(uuid) || !isWalking(event.getFrom(), event.getTo()))
			return;
		
		int currentSteps = plugin.getHeartAttackPlayers().get(uuid);
		if (currentSteps == 5) {
			player.setHealth(0);
			SFXManager.playPlayerSound(player, Sound.BLOCK_NETHER_WART_BREAK, 5, -1);
			plugin.getHeartAttackPlayers().remove(uuid);
		}
		else
			plugin.getHeartAttackPlayers().put(uuid, ++currentSteps);
	}

	private boolean isWalking(Location from, Location to) {
		if (from.getBlockX() == to.getBlockX() && from.getBlockY() == to.getBlockY()
				&& from.getBlockZ() == to.getBlockZ())
			return false;

		return true;
	}
}
