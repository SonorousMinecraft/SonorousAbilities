package com.sereneoasis;


import com.nivixx.ndatabase.api.NDatabase;
import com.nivixx.ndatabase.api.repository.Repository;
import com.sereneoasis.command.SerenityCommand;
import com.sereneoasis.listeners.SerenityListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;
import java.util.logging.Logger;

public class Serenity extends JavaPlugin {

    public static Serenity plugin;
    public static Logger log;

    public static Repository<UUID, PlayerData> repository;

    public static void main(String[] args) {

    }

    @Override
    public void onEnable() {
        super.onEnable();
        plugin = this;
        Serenity.log = this.getLogger();


        this.getServer().getPluginManager().registerEvents(new SerenityListener(), this);
        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new BendingManager(), 0, 1);

        repository = NDatabase.api().getOrCreateRepository(PlayerData.class);

        this.getCommand("serenity").setExecutor(new SerenityCommand());
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}