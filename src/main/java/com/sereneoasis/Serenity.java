package com.sereneoasis;


import com.nivixx.ndatabase.api.NDatabase;
import com.nivixx.ndatabase.api.repository.Repository;
import com.sereneoasis.command.SerenityCommand;
import com.sereneoasis.listeners.SerenityListener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.UUID;
import java.util.logging.Logger;

public class Serenity extends JavaPlugin {

    private static Serenity plugin;

    public static Serenity getPlugin() {
        return plugin;
    }

    private static Logger log;

    private static Repository<UUID, PlayerData> repository;
    public static Repository<UUID, PlayerData> getRepository() {
        return repository;
    }


    private static ScoreboardManager scoreBoardManager;

    public static ScoreboardManager getScoreBoardManager() {
        return scoreBoardManager;
    }


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
        CoreAbility.registerPluginAbilities();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}