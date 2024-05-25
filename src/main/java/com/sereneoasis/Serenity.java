package com.sereneoasis;

import com.nivixx.ndatabase.api.NDatabase;
import com.nivixx.ndatabase.api.repository.Repository;
import com.sereneoasis.ability.BendingManager;
import com.sereneoasis.ability.ComboManager;
import com.sereneoasis.ability.data.AbilityDataManager;
import com.sereneoasis.archetypes.data.ArchetypeDataManager;
import com.sereneoasis.command.SerenityCommand;
import com.sereneoasis.command.TabAutoCompletion;
import com.sereneoasis.config.ConfigManager;
import com.sereneoasis.displays.SerenityBoard;
import com.sereneoasis.listeners.SerenityListener;
import com.sereneoasis.storage.PlayerData;
import com.sk89q.worldguard.WorldGuard;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.UUID;
import java.util.logging.Logger;

import static com.sereneoasis.SerenityPlayer.removeAttributePlayer;

/**
 * @author Sakrajin
 * Main class used to create singletons and instantiate the plugin functionalities
 */
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

    private static ArchetypeDataManager archetypeDataManager;

    public static ArchetypeDataManager getArchetypeDataManager() {
        return archetypeDataManager;
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

    private static WorldGuardManager worldGuardManager;

    public static WorldGuardManager getWorldGuardManager() {
        return worldGuardManager;
    }

    public static void main(String[] args) {

    }

    @Override
    public void onLoad() {
        super.onLoad();
        WorldGuardManager.registerFlag();

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
        archetypeDataManager = new ArchetypeDataManager();
        comboManager = new ComboManager();
        repository = NDatabase.api().getOrCreateRepository(PlayerData.class);
        this.getCommand("serenity").setExecutor(new SerenityCommand());
        this.getCommand("serenity").setTabCompleter(new TabAutoCompletion());

        worldGuardManager = new WorldGuardManager();

    }

    @Override
    public void onDisable() {
        super.onDisable();
//        Bukkit.getOnlinePlayers().forEach(player -> {
//            SerenityBoard.removeScore(player);
//
//            SerenityPlayer serenityPlayer = SerenityPlayer.getSerenityPlayer(player);
//
//            Serenity.getComboManager().removePlayer(player);
//
//            SerenityPlayer.upsertPlayer(serenityPlayer);
//
//            removeAttributePlayer(player, serenityPlayer);
//
//
//            SerenityPlayer.removePlayerFromMap(player);
//        });


    }


}