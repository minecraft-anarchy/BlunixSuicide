package com.blunix.blunixsuicide.events;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;

import com.blunix.blunixsuicide.models.KillerMob;

@SuppressWarnings("deprecation")
public class MobAttack implements Listener {

	@EventHandler
    public void onKillerMobAttack(EntityDamageByEntityEvent event) {
        KillerMob killerMob = KillerMob.getKillerMob(event.getDamager());
        if (killerMob == null)
            return;
        if (!killerMob.isTarget((LivingEntity) event.getEntity())) {
            event.setCancelled(true);
            killerMob.resetTarget();
            return;
        }
        Player target = (Player) event.getEntity();
        double maxHealth = target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        event.setDamage(maxHealth * 0.40);
        event.setDamage(DamageModifier.ARMOR, 0);;
    }
}
