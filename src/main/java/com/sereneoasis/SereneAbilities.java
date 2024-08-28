package com.sereneoasis;

import com.nivixx.ndatabase.api.NDatabase;
import com.nivixx.ndatabase.api.repository.Repository;
import com.sereneoasis.ability.BendingManager;
import com.sereneoasis.ability.ComboManager;
import com.sereneoasis.ability.data.AbilityDataManager;
import com.sereneoasis.archetypes.data.ArchetypeDataManager;
import com.sereneoasis.command.SereneCommand;
import com.sereneoasis.command.TabAutoCompletion;
import com.sereneoasis.config.ConfigManager;
import com.sereneoasis.listeners.SereneAbilitiesListener;
import com.sereneoasis.storage.PlayerData;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.UUID;
import java.util.logging.Logger;

/**
 * @author Sakrajin
 * Main class used to create singletons and instantiate the plugin functionalities
 */
public class SereneAbilities extends JavaPlugin {

    private static SereneAbilities plugin;
    private static Logger log;
    private static Repository<String, PlayerData> repository;
    private static AbilityDataManager abilityDataManager;
    private static ArchetypeDataManager archetypeDataManager;
    private static ScoreboardManager scoreBoardManager;
    private static ComboManager comboManager;
    private static ConfigManager configManager;
    private static WorldGuardManager worldGuardManager;
    private static boolean isFlagRegistered = false;

    public static SereneAbilities getPlugin() {
        return plugin;
    }

    public static Repository<String, PlayerData> getRepository() {
        return repository;
    }

    public static AbilityDataManager getAbilityDataManager() {
        return abilityDataManager;
    }

    public static ArchetypeDataManager getArchetypeDataManager() {
        return archetypeDataManager;
    }

    public static ScoreboardManager getScoreBoardManager() {
        return scoreBoardManager;
    }

    public static ComboManager getComboManager() {
        return comboManager;
    }

    public static WorldGuardManager getWorldGuardManager() {
        return worldGuardManager;
    }

    public static void main(String[] args) {

    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!isFlagRegistered) {
            WorldGuardManager.registerFlag();
            isFlagRegistered = true;
        }

    }

    @Override
    public void onEnable() {
        super.onEnable();
        plugin = this;
        SereneAbilities.log = this.getLogger();

        this.getServer().getPluginManager().registerEvents(new SereneAbilitiesListener(), this);
        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new BendingManager(), 0, 1);

        configManager = new ConfigManager();
        abilityDataManager = new AbilityDataManager();
        archetypeDataManager = new ArchetypeDataManager();
        comboManager = new ComboManager();
        repository = NDatabase.api().getOrCreateRepository(PlayerData.class);
        this.getCommand("serenity").setExecutor(new SereneCommand());
        this.getCommand("serenity").setTabCompleter(new TabAutoCompletion());

        worldGuardManager = new WorldGuardManager();

    }

    @Override
    public void onDisable() {
        super.onDisable();
//        Bukkit.getOnlinePlayers().forEach(player -> {
//            SereneAbilitiesBoard.removeScore(player);
//
//            SereneAbilitiesPlayer serenityPlayer = SereneAbilitiesPlayer.getSereneAbilitiesPlayer(player);
//
//            SereneAbilities.getComboManager().removePlayer(player);
//
//            SereneAbilitiesPlayer.upsertPlayer(serenityPlayer);
//
//            removeAttributePlayer(player, serenityPlayer);
//
//
//            SereneAbilitiesPlayer.removePlayerFromMap(player);
//        });


    }


}