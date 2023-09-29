package com.sereneoasis.ability;

import com.sereneoasis.Serenity;
import com.sereneoasis.config.Config;
import com.sereneoasis.config.ConfigFile;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class AbilityDataManager {

    private FileConfiguration config;

    public AbilityDataManager()
    {
        config = Config.getConfig();
        config.get("");

    }
}
