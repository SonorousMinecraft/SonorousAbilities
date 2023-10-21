package com.sereneoasis.ability.superclasses;


import org.bukkit.entity.Player;

/**
 * @author Sakrajin
 * Interface to define the function of all abilities.
 */
public interface Ability {

    public void progress();

    public void remove();

    public Player getPlayer();

    public String getName();

}
