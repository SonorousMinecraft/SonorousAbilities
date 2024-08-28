package com.sereneoasis.storage;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.nivixx.ndatabase.api.annotation.NTable;
import com.nivixx.ndatabase.api.model.NEntity;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author Sakrajin
 * Used by NDatabase API to represent players data within the database
 */
@NTable(name = "player_data", schema = "", catalog = "")
public class PlayerData extends NEntity<String> {

    @JsonProperty("name")
    private String name;
    @JsonProperty("abilities")
    private HashMap<Integer, String> abilities;
    @JsonProperty("archetype")
    private String archetype;
    @JsonProperty("presets")
    private HashMap<String, HashMap<Integer, String>> presets;

    public PlayerData() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<Integer, String> getAbilities() {
        return this.abilities;
    }

    public void setAbilities(HashMap<Integer, String> abilities) {
        this.abilities = abilities;
    }

    public String getArchetype() {
        return this.archetype;
    }

    public void setArchetype(String archetype) {
        this.archetype = archetype;
    }

    public HashMap<String, HashMap<Integer, String>> getPresets() {
        return presets;
    }

    public void setPresets(HashMap<String, HashMap<Integer, String>> presets) {
        this.presets = presets;
    }


}
