package com.sonorous.ability.superclasses;


import org.bukkit.entity.Player;

/**
 * Interface to define the function of all abilities.
 */
public interface Ability {

     void progress() throws ReflectiveOperationException;

     void remove();

     Player getPlayer();

     String getName();

}
