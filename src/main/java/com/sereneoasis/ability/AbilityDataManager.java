package com.sereneoasis.ability;

import com.sereneoasis.Element;
import com.sereneoasis.config.ConfigManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.inventory.ClickType;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AbilityDataManager {

    private static final Map<String, AbilityData> abilityDataMap = new ConcurrentHashMap<>();

    private static final Map<String, ComboData> comboDataMap = new ConcurrentHashMap<>();

    public static Map<String, ComboData> getComboDataMap()
    {
        return comboDataMap;
    }
    private FileConfiguration config;


    public AbilityDataManager()
    {
        config = ConfigManager.getOceanConfig();
        /*for (Element element : Element.values() )
        {
            for (String ability : config.getConfigurationSection(element.name()).getKeys(false))
            {
                ConfigurationSection abil = config.getConfigurationSection(element.name() + "." + ability);
                AbilityData abilityData = new AbilityData(abil.getString("description") , abil.getString("instructions"),
                        abil.getLong("chargetime"), abil.getLong("cooldown"),  abil.getLong("duration") , abil.getDouble("damage") ,
                        abil.getDouble("radius") , abil.getDouble("range") , abil.getDouble("speed") );
                abilityDataMap.put(ability, abilityData);
            }
        }*/


        for (String ability : config.getConfigurationSection(Element.OCEAN.name() + ".ability").getKeys(false))
        {
            ConfigurationSection abil = config.getConfigurationSection(Element.OCEAN.name() + ".ability" + "." + ability);
            AbilityData abilityData = new AbilityData(abil.getString("description") , abil.getString("instructions"),
                    abil.getLong("chargetime"), abil.getLong("cooldown"),  abil.getLong("duration") , abil.getDouble("damage") ,
                    abil.getDouble("radius") , abil.getDouble("range") , abil.getDouble("speed"), abil.getDouble("sourcerange"));
            abilityDataMap.put(ability, abilityData);
        }

//        for (String combo : config.getConfigurationSection(Element.OCEAN.name() + ".combo").getKeys(false))
//        {
//            ConfigurationSection abil = config.getConfigurationSection(Element.OCEAN.name() + ".combo" + "." + combo);
//
//            ArrayList<ComboManager.AbilityInformation> abilities = new ArrayList<>();
//            for (String ability : config.getConfigurationSection(Element.OCEAN.name() + ".combo" + "." + combo + ".usage").getKeys(false))
//            {
//                abilities.add(new ComboManager.AbilityInformation(ability.split(":")[0], ClickType.valueOf(ability.split(":")[1])));
//            }
//
//            ComboData comboData = new ComboData(abil.getString("description") , abil.getString("instructions"),
//                    abil.getLong("chargetime"), abil.getLong("cooldown"),  abil.getLong("duration") , abil.getDouble("damage") ,
//                    abil.getDouble("radius") , abil.getDouble("range") , abil.getDouble("speed"), abilities);
//
//            comboDataMap.put(combo, comboData);
//            abilityDataMap.put(combo, comboData);
//        }
    }

    public static AbilityData getAbilityData(String ability)
    {
        return abilityDataMap.get(ability);
    }

    public static boolean isAbility(String ability) {
        if (abilityDataMap.containsKey(ability)) {
            return true;
        }
        return false;
    }


    public static List<String> getElementAbilities(Element element)
    {
        if (element != null) {
            List<String> elementAbilities = new ArrayList<>();
            for (String ability : abilityDataMap.keySet())
            {
                if (abilityDataMap.get(ability).getElement().equals(element.name()))
                {
                    elementAbilities.add(ability);
                }
            }
            return elementAbilities;
        }
        return null;
    }


}
