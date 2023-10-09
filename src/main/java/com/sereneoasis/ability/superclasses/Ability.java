package com.sereneoasis.ability.superclasses;

import org.bukkit.entity.Player;

public interface Ability {

    public void progress();

    public void remove();

    public Player getPlayer();

    public String getName();
}
