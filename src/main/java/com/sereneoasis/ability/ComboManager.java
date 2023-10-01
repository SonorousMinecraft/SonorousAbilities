package com.sereneoasis.ability;

import com.sereneoasis.SerenityPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ComboManager {

    private static Map<Player, ArrayList<AbilityInformation>> RECENTLY_USED = new ConcurrentHashMap<>();

    private static Map<String, ComboData> COMBO_ABILITIES = new HashMap<>();

    public ComboManager()
    {
        RECENTLY_USED.clear();
        COMBO_ABILITIES.clear();
        COMBO_ABILITIES = AbilityDataManager.getComboDataMap();
    }


    public void removePlayer(Player player)
    {
        RECENTLY_USED.remove(player);
    }

    public void addRecentlyUsed(Player player, String name, ClickType clickType)
    {
        AbilityInformation abilityInformation = new AbilityInformation(name, clickType);
        ArrayList<AbilityInformation> recentAbilities = RECENTLY_USED.get(player);
        if (recentAbilities == null)
        {
            recentAbilities = new ArrayList<>();
        }
        recentAbilities.add(abilityInformation);
        if (recentAbilities.size() > 8)
        {
            recentAbilities.remove(0);
        }
        RECENTLY_USED.put(player, recentAbilities);

        checkForCombo(player);
    }

    private void checkForCombo(Player player)
    {
        SerenityPlayer sPlayer = SerenityPlayer.getSerenityPlayer(player);
        for (String ability : COMBO_ABILITIES.keySet())
        {
            ArrayList<AbilityInformation>abilities = COMBO_ABILITIES.get(ability).getAbilities();
            if (abilities.get(abilities.size() - 1).getName().equals( sPlayer.getHeldAbility()))
            {
                if (RECENTLY_USED.get(player).containsAll(abilities))
                {
                    switch (ability)
                    {

                    }
                }
            }

        }
    }


    public static class AbilityInformation
    {

        private String name;
        private ClickType clickType;
        public AbilityInformation(final String name, ClickType clickType)
        {
            this.name = name;
            this.clickType = clickType;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ClickType getClickType() {
            return clickType;
        }

        public void setClickType(ClickType clickType) {
            this.clickType = clickType;
        }
    }


}








