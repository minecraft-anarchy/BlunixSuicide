package com.blunix.blunixsuicide.models;

import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TaskManager {
    private static Map<UUID, Integer> TASKS = new HashMap<>();
    private UUID uuid;

    public TaskManager(UUID uuid) {
        this.uuid = uuid;
    }

    public void stopTask() {
        Bukkit.getScheduler().cancelTask(getID());
        TASKS.remove(uuid);
    }

    public boolean hasID() {
        if (!TASKS.containsKey(uuid))
            return false;

        return true;
    }

    public int getID() {
        return TASKS.get(uuid);
    }

    public void setID(int id) {
        TASKS.put(uuid, id);
    }
}
