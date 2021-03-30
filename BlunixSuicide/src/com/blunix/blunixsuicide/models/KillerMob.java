package com.blunix.blunixsuicide.models;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;

import com.blunix.blunixsuicide.BlunixSuicide;

public class KillerMob {
	private Player target;
	private Mob mob;

	public KillerMob(Player target, Mob mob) {
		this.target = target;
		this.mob = mob;
	}

	public static KillerMob getKillerMob(Entity entity) {
		BlunixSuicide plugin = BlunixSuicide.getInstance();
		for (KillerMob killerMob : plugin.getKillerMobs()) {
			if (!entity.getUniqueId().equals(killerMob.getMob().getUniqueId()))
				continue;

			return killerMob;
		}
		return null;
	}

	public void register() {
		BlunixSuicide.getInstance().getKillerMobs().add(this);
	}
	
	public void unregister() {
		BlunixSuicide.getInstance().getKillerMobs().remove(this);
	}

	public void resetTarget() {
		mob.setTarget(target);
	}

	public boolean isTarget(LivingEntity target) {
		return this.target.equals(target);
	}

	public Player getTarget() {
		return target;
	}

	public Mob getMob() {
		return mob;
	}
}
