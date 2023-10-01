package com.sereneoasis;

import com.nivixx.ndatabase.api.NDatabase;
import com.nivixx.ndatabase.api.repository.Repository;
import com.sereneoasis.ability.*;
import com.sereneoasis.command.SerenityCommand;
import com.sereneoasis.config.ConfigManager;
import com.sereneoasis.listeners.SerenityListener;
import com.sereneoasis.storage.PlayerData;
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

    private static AbilityDataManager abilityDataManager;

    public static AbilityDataManager getAbilityDataManager() {
        return abilityDataManager;
    }

    private static ScoreboardManager scoreBoardManager;

    public static ScoreboardManager getScoreBoardManager() {
        return scoreBoardManager;
    }

    private static ComboManager comboManager;

    public static ComboManager getComboManager() {
        return comboManager;
    }

    private static ConfigManager configManager;




    public static void main(String[] args) {

    }


    @Override
    public void onEnable() {
        super.onEnable();
        plugin = this;
        Serenity.log = this.getLogger();


        this.getServer().getPluginManager().registerEvents(new SerenityListener(), this);
        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new BendingManager(), 0, 1);

        configManager = new ConfigManager();
        abilityDataManager = new AbilityDataManager();
        comboManager = new ComboManager();
        repository = NDatabase.api().getOrCreateRepository(PlayerData.class);

        this.getCommand("serenity").setExecutor(new SerenityCommand());
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}