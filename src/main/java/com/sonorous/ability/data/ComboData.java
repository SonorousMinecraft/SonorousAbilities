package com.sonorous.ability.data;

import com.sonorous.ability.ComboManager;
import com.sonorous.archetypes.Archetype;

import java.util.ArrayList;

/**
 * @author Sakrajin
 * Adds additional functionality to {@link AbilityData}.
 */
public class ComboData extends AbilityData {

    private ArrayList<ComboManager.AbilityInformation> abilities;

    public ComboData(Archetype archetype, String description, String instructions,
                     long chargetime, long cooldown, long duration,
                     double damage, double hitbox, double radius, double range, double speed, double sourceRange, double size, ArrayList<ComboManager.AbilityInformation> abilities) {
        super(archetype, description, instructions, chargetime, cooldown, duration, damage, hitbox, radius, range, speed, sourceRange, size);
        this.abilities = abilities;
    }

    public ArrayList<ComboManager.AbilityInformation> getAbilities() {
        return abilities;
    }
}
