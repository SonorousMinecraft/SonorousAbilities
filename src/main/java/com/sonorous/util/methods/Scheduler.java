package com.sonorous.util.methods;

import com.sonorous.SonorousAbilities;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;

public class Scheduler {

    public static void performTaskLater(long time, Task task) {
        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.runTaskLater(SonorousAbilities.getPlugin(), task::doTask, time);
    }

    public interface Task {
        void doTask();
    }
}
