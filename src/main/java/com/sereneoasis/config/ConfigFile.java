package com.sereneoasis.config;

import com.sereneoasis.SereneAbilities;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * @author Sakrajin
 * Used to create configuration files.
 */
public class ConfigFile {

    public FileConfiguration config;
    private SereneAbilities plugin;
    private File file;

    public ConfigFile(String name) {
        this(new File(name + ".yml"));
    }

    public ConfigFile(File file) {
        this.plugin = SereneAbilities.getPlugin();
        this.file = new File(plugin.getDataFolder() + File.separator + file);
        this.config = YamlConfiguration.loadConfiguration(this.file);
        reloadConfig();
    }

    public File getFile() {
        return file;
    }

    public void createConfig() {
        if (!file.getParentFile().exists()) {
            try {
                file.getParentFile().mkdir();
                plugin.getLogger().info("Generating new directory for " + file.getName());
            } catch (Exception e) {
                plugin.getLogger().info("Failed to generate directory");
                e.printStackTrace();
            }
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
                plugin.getLogger().info("Generating new " + file.getName());
            } catch (Exception e) {
                plugin.getLogger().info("Failed to generate " + file.getName());
                e.printStackTrace();
            }
        }
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void reloadConfig() {
        createConfig();
        try {
            config.load(file);
            plugin.getLogger().info("Loading configuration");
        } catch (Exception e) {
            plugin.getLogger().info("Failed to reload " + file.getName());
            e.printStackTrace();
        }
    }

    public void saveConfig() {
        try {
            config.options().copyDefaults(true);
            config.save(file);
            plugin.getLogger().info("Successfully saved configuration");
        } catch (Exception e) {
            plugin.getLogger().info("Failed to save configuration");
            e.printStackTrace();
        }
    }
}
