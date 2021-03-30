package com.blunix.blunixsuicide.events;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Rabbit.Type;
import org.bukkit.entity.Vindicator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.blunix.blunixsuicide.BlunixSuicide;
import com.blunix.blunixsuicide.models.KillerMob;
import com.blunix.blunixsuicide.models.SuicideMethod;
import com.blunix.blunixsuicide.models.TaskManager;
import com.blunix.blunixsuicide.util.ColorUtil;
import com.blunix.blunixsuicide.util.ConfigManager;
import com.blunix.blunixsuicide.util.Messager;
import com.blunix.blunixsuicide.util.SFXManager;

public class SuicideHandler implements Listener {
	private BlunixSuicide plugin;
	private ConfigManager config;
	private Map<UUID, String> suicidePlayers;
	private int taskID;

	public SuicideHandler(BlunixSuicide plugin) {
		this.plugin = plugin;
		config = new ConfigManager(plugin);
		suicidePlayers = new HashMap<UUID, String>();
	}

	// Handle different suicide methods
	@EventHandler
	public void onPlayerSuicide(PlayerSuicideEvent event) {
		Player player = event.getPlayer();
		if (suicidePlayers.containsKey(player.getUniqueId())) {
			Messager.sendMessage(player, "&cYou might want to wait a little more before killing yourself again...");
			return;
		}
		Location playerLocation = player.getLocation();
		SuicideMethod method = event.getMethod();
		final String deathMessage = config.getDeathMessage(method).replace("%PLAYER%", player.getDisplayName());
		final HeartAttackHandler heartAttackHandler = new HeartAttackHandler(plugin);
		suicidePlayers.put(player.getUniqueId(), deathMessage);

		for (PotionEffect effect : player.getActivePotionEffects())
			player.removePotionEffect(effect.getType());

		switch (method) {
		case VOID:
			playerLocation.setY(-30);
			player.teleport(playerLocation);
			SFXManager.playPlayerSound(player, Sound.ENTITY_ILLUSIONER_MIRROR_MOVE, 1, 2);
			break;

		case LIGHTNING:
			startLightningKill(player);
			break;

		case JOHNNY:
			startJohnnyKill(player, playerLocation);
			break;

		case KILLER_BUNNIES:
			startBunnyKill(player, playerLocation);
			break;

		case EXPLOSION:
			explosionKill(player);
			break;

		case HEART_ATTACK:
			player.damage(1);
			Messager.sendMessage(player, "&4You've been hit by the Five Point Palm Exploding Heart technique.");
			plugin.getHeartAttackPlayers().put(player.getUniqueId(), 0);
			Bukkit.getPluginManager().registerEvents(heartAttackHandler, plugin);
			break;

		default:
			break;
		}
		long deathDelay = config.getKillTime();
		if (method == SuicideMethod.HEART_ATTACK)
			deathDelay *= 2.5;

		Bukkit.getScheduler().runTaskLater(plugin, () -> {
			if (method == SuicideMethod.HEART_ATTACK)
				HandlerList.unregisterAll(heartAttackHandler);
			if (player == null || !suicidePlayers.containsKey(player.getUniqueId()))
				return;
			if (!suicidePlayers.get(player.getUniqueId()).equals(deathMessage))
				return;

			player.setHealth(0);
		}, deathDelay);
	}

	// Block player mouse buttons
	@EventHandler
	public void onPlayerClick(PlayerInteractEvent event) {
		if (!suicidePlayers.containsKey(event.getPlayer().getUniqueId()))
			return;

		event.setCancelled(true);
	}

	// Handle player death
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		UUID uuid = player.getUniqueId();
		if (!suicidePlayers.containsKey(uuid))
			return;

		event.setDeathMessage(suicidePlayers.get(uuid));
		suicidePlayers.remove(uuid);
	}

	private void startLightningKill(Player player) {
		new BukkitRunnable() {

			@Override
			public void run() {
				if (player == null || player.getHealth() <= 0) {
					cancel();
					return;
				}
				player.getWorld().strikeLightning(player.getLocation());
			}

		}.runTaskTimer(plugin, 0, 20);
	}

	private void startJohnnyKill(Player target, Location location) {
		ItemStack axe = new ItemStack(Material.DIAMOND_AXE);
		axe.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 20);
		Vindicator johnny = (Vindicator) location.getWorld().spawnEntity(location, EntityType.VINDICATOR);
		johnny.setCustomName("Jack Torrance");
		johnny.setCustomNameVisible(true);
		johnny.setTarget(target);
		johnny.setInvulnerable(true);
		johnny.getEquipment().setItemInMainHand(axe);

		KillerMob killerMob = new KillerMob(target, johnny);
		killerMob.register();
		startMobKilling(killerMob);

		Messager.sendMessage(target, "&4HERE IS JOHNNY!");
		SFXManager.playPlayerSound(target, Sound.ENTITY_VINDICATOR_AMBIENT, 1, -1);
	}

	private void startBunnyKill(Player target, Location location) {
		Rabbit bunny = (Rabbit) location.getWorld().spawnEntity(location, EntityType.RABBIT);
		bunny.setCustomName(ColorUtil.formatColor("&4Killer Bunny"));
		bunny.setCustomNameVisible(true);
		bunny.setTarget(target);
		bunny.setInvulnerable(true);
		bunny.setRabbitType(Type.THE_KILLER_BUNNY);

		KillerMob killerMob = new KillerMob(target, bunny);
		killerMob.register();
		startMobKilling(killerMob);
	}

	private void explosionKill(Player player) {
		Location playerLocation = player.getLocation().subtract(0, 1, 0);
		playerLocation.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, playerLocation, 20, 0.5, 0.5, 0.5);
		playerLocation.getWorld().playSound(playerLocation, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);

		Vector velocity = player.getVelocity();
		velocity.setY(1 + velocity.getY() * 2);
		player.setVelocity(velocity);
		player.setHealth(0);
	}

	private void startMobKilling(KillerMob killerMob) {
		Entity mob = killerMob.getMob();
		Player target = killerMob.getTarget();
		TaskManager taskManager = new TaskManager(mob.getUniqueId());

		taskID = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
			if (!taskManager.hasID())
				taskManager.setID(taskID);
			if (target == null || target.getHealth() <= 0) {
				killerMob.unregister();
				mob.remove();
				taskManager.stopTask();
				return;
			}
			Location mobLocation = mob.getLocation();
			Location playerLocation = target.getLocation();
			if (mobLocation.distance(playerLocation) > 2)
				mob.teleport(target);

		}, 0L, 20L).getTaskId();
	}
}
