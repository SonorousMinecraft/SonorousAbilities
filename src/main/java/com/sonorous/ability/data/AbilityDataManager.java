package com.sonorous.ability.data;

import com.sonorous.ability.ComboManager;
import com.sonorous.archetypes.Archetype;
import com.sonorous.config.ConfigManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Initialises all configuration values for abilities.
 */
public class AbilityDataManager {

    private static final Map<String, AbilityData> abilityDataMap = new ConcurrentHashMap<>();

    private static final Map<String, ComboData> comboDataMap = new ConcurrentHashMap<>();

    public AbilityDataManager() {

        FileConfiguration config;
        for (Archetype archetype : Archetype.values()) {

            config = ConfigManager.getConfig(archetype).getConfig();
            if (config.getConfigurationSection(archetype.toString() + ".ability") != null) {
                for (String ability : Objects.requireNonNull(config.getConfigurationSection(archetype + ".ability")).getKeys(false)) {
                    ConfigurationSection abil = config.getConfigurationSection(archetype + ".ability" + "." + ability);
                    assert abil != null;
                    AbilityData abilityData = new AbilityData(archetype, abil.getString("description"), abil.getString("instructions"),
                            abil.getLong("chargetime"), abil.getLong("cooldown"), abil.getLong("duration"),
                            abil.getDouble("damage"), abil.getDouble("hitbox"),
                            abil.getDouble("radius"), abil.getDouble("range"), abil.getDouble("speed"), abil.getDouble("sourcerange"), abil.getDouble("size"));
                    abilityDataMap.put(ability, abilityData);
                }
            }
        }

        for (Archetype archetype : Archetype.values()) {
            config = ConfigManager.getConfig(archetype).getConfig();

            if (config.getConfigurationSection(archetype.toString() + ".combo") != null) {
                for (String combo : Objects.requireNonNull(config.getConfigurationSection(archetype + ".combo")).getKeys(false)) {
                    ConfigurationSection abil = config.getConfigurationSection(archetype + ".combo" + "." + combo);

                    ArrayList<ComboManager.AbilityInformation> abilities = new ArrayList<>();
                    assert abil != null;
                    for (String usageAbilities : abil.getStringList(".usage")) {
                        abilities.add(new ComboManager.AbilityInformation(usageAbilities.split(":")[0], ClickType.valueOf(usageAbilities.split(":")[1])));
                    }

                    ComboData comboData = new ComboData(archetype, abil.getString("description"), abil.getString("instructions"),
                            abil.getLong("chargetime"), abil.getLong("cooldown"), abil.getLong("duration"),
                            abil.getDouble("damage"), abil.getDouble("hitbox"),
                            abil.getDouble("radius"), abil.getDouble("range"), abil.getDouble("speed"), abil.getDouble("sourcerange"), abil.getDouble("size"), abilities);
                    abilityDataMap.put(combo, comboData);
                    comboDataMap.put(combo, comboData);
                }
            }
        }
    }

    public static Map<String, ComboData> getComboDataMap() {
        return comboDataMap;
    }

    public static AbilityData getAbilityData(String ability) {
        return abilityDataMap.get(ability);
    }

    public static boolean isNotAbility(String ability) {
        return !abilityDataMap.containsKey(ability);
    }


    public static List<String> getArchetypeAbilities(Archetype archetype) {
        if (archetype != null) {
            List<String> archetypeAbilities = new ArrayList<>();
            for (String ability : abilityDataMap.keySet()) {
                if (abilityDataMap.get(ability).getArchetype().equals(archetype)) {
                    archetypeAbilities.add(ability);
                }
            }
            return archetypeAbilities;
        }
        return null;
    }

    public static boolean isCombo(String ability) {
        return comboDataMap.containsKey(ability);
    }

}
