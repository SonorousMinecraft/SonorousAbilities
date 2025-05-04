package com.sonorous;

import com.nivixx.ndatabase.api.NDatabase;
import com.nivixx.ndatabase.api.repository.Repository;
import com.sonorous.ability.BendingManager;
import com.sonorous.ability.ComboManager;
import com.sonorous.ability.data.AbilityDataManager;
import com.sonorous.archetypes.data.ArchetypeDataManager;
import com.sonorous.command.SonorousCommand;
import com.sonorous.command.TabAutoCompletion;
import com.sonorous.config.ConfigManager;
import com.sonorous.listeners.SonorousAbilitiesListener;
import com.sonorous.storage.PlayerData;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.logging.Logger;

/**
 * @author Sakrajin
 * Main class used to create singletons and instantiate the plugin functionalities
 */
public class SonorousAbilities extends JavaPlugin {

    private static SonorousAbilities plugin;
    private static Logger log;
    private static Repository<String, PlayerData> repository;
    private static AbilityDataManager abilityDataManager;
    private static ArchetypeDataManager archetypeDataManager;
    private static ScoreboardManager scoreBoardManager;
    private static ComboManager comboManager;
    private static ConfigManager configManager;
    private static WorldGuardManager worldGuardManager;
    private static boolean isFlagRegistered = false;

    public static SonorousAbilities getPlugin() {
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
        SonorousAbilities.log = this.getLogger();

        this.getServer().getPluginManager().registerEvents(new SonorousAbilitiesListener(), this);
        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new BendingManager(), 0, 1);

        configManager = new ConfigManager();
        abilityDataManager = new AbilityDataManager();
        archetypeDataManager = new ArchetypeDataManager();
        comboManager = new ComboManager();
        repository = NDatabase.api().getOrCreateRepository(PlayerData.class);
        this.getCommand("sonorous").setExecutor(new SonorousCommand());
        this.getCommand("sonorous").setTabCompleter(new TabAutoCompletion());

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