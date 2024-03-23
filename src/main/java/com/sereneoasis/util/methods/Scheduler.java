package com.sereneoasis.util.methods;

import com.sereneoasis.Serenity;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;

public class Scheduler {

    public interface Task {
        void doTask();
    }

    public static void performTaskLater(long time, Task task){
        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.runTaskLater(Serenity.getPlugin(), task::doTask, time);
    }
}
