package com.sereneoasis;


import com.sereneoasis.listeners.SerenityListener;
import org.bukkit.plugin.java.JavaPlugin;

public class Serenity extends JavaPlugin {
    public static void main(String[] args) {

    }

    @Override
    public void onEnable() {
        super.onEnable();

        this.getServer().getPluginManager().registerEvents(new SerenityListener(), this);
        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new BendingManager(), 0, 1);

    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}