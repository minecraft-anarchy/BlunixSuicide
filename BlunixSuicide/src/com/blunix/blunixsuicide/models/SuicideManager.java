//package com.blunix.blunixsuicide.models;
//
//import com.blunix.blunixsuicide.BlunixSuicide;
//import com.blunix.blunixsuicide.util.ColorUtil;
//import com.blunix.blunixsuicide.util.ConfigManager;
//import org.bukkit.Bukkit;
//import org.bukkit.Location;
//import org.bukkit.entity.*;
//
//import java.util.Random;
//
//public class SuicideManager {
//    private BlunixSuicide plugin;
//    private ConfigManager config;
//
//    public SuicideManager(BlunixSuicide plugin) {
//        this.plugin = plugin;
//        this.config = new ConfigManager(plugin);
//    }
//
//    public void suicide(Player player) {
//        Random random = new Random();
//        SuicideMethod method = SuicideMethod.values()[random.nextInt(SuicideMethod.values().length)];
//        if (method == SuicideMethod.PASSIVE_MOB)
//            spawnMobs(player, plugin.getKillerMobTypes().get(random.nextInt(plugin.getKillerMobTypes().size())));
//
//        plugin.getSuicidePlayers().add(player);
//        Bukkit.getScheduler().runTaskLater(plugin, () -> {
//            player.setHealth(0);
//            plugin.getSuicidePlayers().remove(player);
//        }, config.getKillTime());
//    }
//
//    private void spawnMobs(Player player, EntityType type) {
//        Random random = new Random();
//        Location location = player.getLocation();
//        int amount = random.nextInt(5 - 1) + 1;
//        for (int i = 0; i < amount; i++) {
//            Entity entity = location.getWorld().spawnEntity(location, type);
//            if (!(entity instanceof Mob))
//                continue;
//
//            Mob mob = (Mob) entity;
//            mob.setInvulnerable(true);
//            mob.setTarget(player);
//
//            switch (type) {
//                case PANDA:
//                    mob.setCustomName(ColorUtil.formatColor("&aHuggy Panda"));
//                    mob.setCustomNameVisible(true);
//                    ((Panda) mob).setBaby();
//                    break;
//
//                case RABBIT:
//                    mob.setCustomName(ColorUtil.formatColor("&4The Killer Bunny"));
//                    mob.setCustomNameVisible(true);
//                    ((Rabbit) mob).setRabbitType(Rabbit.Type.THE_KILLER_BUNNY);
//                    break;
//
//                case LLAMA:
//                    mob.setCustomName(ColorUtil.formatColor("&5Fortnite Llama"));
//                    mob.setCustomNameVisible(true);
//                    break;
//
//                case COW:
//                    mob.setCustomName(ColorUtil.formatColor("&4Angry Cow"));
//                    mob.setCustomNameVisible(true);
//                    break;
//
//                default:
//                    break;
//            }
//            KillerMob killerMob = new KillerMob(type, player, mob);
//            plugin.getKillerMobs().add(killerMob);
//            KillerMobTask killerMobTask = new KillerMobTask(plugin);
//            killerMobTask.startKilling(killerMob);
//
//            Bukkit.getScheduler().runTaskLater(plugin, () -> {
//                TaskManager taskManager = new TaskManager(mob.getUniqueId());
//                taskManager.stopTask();
//                plugin.getSuicidePlayers().remove(player);
//                mob.remove();
//            }, config.getKillTime());
//        }
//    }
//}
