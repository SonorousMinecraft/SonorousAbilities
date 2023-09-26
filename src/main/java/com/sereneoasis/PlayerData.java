package com.sereneoasis;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.nivixx.ndatabase.api.annotation.Indexed;
import com.nivixx.ndatabase.api.annotation.NTable;
import com.nivixx.ndatabase.api.model.NEntity;
import org.bukkit.entity.Player;

import java.util.UUID;

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
    private PlayerDataAbilities abilities;

    public void setAbilities(PlayerDataAbilities playerDataAbilities)
    {
        this.abilities = playerDataAbilities;
    }

    public PlayerDataAbilities getAbilities()
    {
        return this.abilities;
    }

    @JsonProperty("element")
    private String element;

    public void setElement(String element)
    {
        this.element = element;
    }

    public String getElement()
    {
        return this.element;
    }



}
