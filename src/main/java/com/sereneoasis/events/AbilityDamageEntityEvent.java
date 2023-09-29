package com.sereneoasis.events;

import com.sereneoasis.ability.Ability;
import com.sereneoasis.ability.CoreAbility;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class AbilityDamageEntityEvent extends Event implements Cancellable {

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean b) {

    }

    @Override
    public HandlerList getHandlers() {
        return null;
    }
}
