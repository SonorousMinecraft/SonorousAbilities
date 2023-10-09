package com.sereneoasis.ability.data;

import com.sereneoasis.ability.ComboManager;

import java.util.ArrayList;

public class ComboData extends AbilityData {

    private ArrayList<ComboManager.AbilityInformation> abilities;
    public ComboData(String description, String instructions, long chargetime, long cooldown, long duration, double damage, double radius, double range, double speed, double sourcerange, ArrayList<ComboManager.AbilityInformation> abilities) {
        super(description, instructions, chargetime, cooldown, duration, damage, radius, range, speed, sourcerange);
        this.abilities = abilities;
    }

    public ArrayList<ComboManager.AbilityInformation> getAbilities() {
        return abilities;
    }
}
