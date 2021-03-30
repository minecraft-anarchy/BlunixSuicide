package com.blunix.blunixsuicide.events;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

import com.blunix.blunixsuicide.models.KillerMob;

public class MobTarget implements Listener {

    @EventHandler
    public void onMobTarget(EntityTargetEvent event) {
        KillerMob killerMob = KillerMob.getKillerMob(event.getEntity());
        if (killerMob == null)
            return;
        if (!(event.getEntity() instanceof LivingEntity))
            return;
        if (!killerMob.isTarget((LivingEntity) event.getEntity())) {
            killerMob.resetTarget();
            return;
        }
    }
}
