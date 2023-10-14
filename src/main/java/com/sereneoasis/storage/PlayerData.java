package com.sereneoasis.storage;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.nivixx.ndatabase.api.annotation.Indexed;
import com.nivixx.ndatabase.api.annotation.NTable;
import com.nivixx.ndatabase.api.model.NEntity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author Sakrajin
 * Used by NDatabase API to represent players data within the database
 */
@NTable(name = "player_data", schema = "", catalog = "")
public class PlayerData extends NEntity<UUID> {

    public PlayerData() { }

    @JsonProperty("name")
    private String name;

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    @JsonProperty("abilities")
    private HashMap<Integer, String> abilities;

    public void setAbilities(HashMap<Integer, String> abilities)
    {
        this.abilities = abilities;
    }

    public HashMap<Integer, String> getAbilities()
    {
        return this.abilities;
    }

    @JsonProperty("archetype")
    private String archetype;

    public void setArchetype(String archetype)
    {
        this.archetype = archetype;
    }

    public String getArchetype()
    {
        return this.archetype;
    }

}
