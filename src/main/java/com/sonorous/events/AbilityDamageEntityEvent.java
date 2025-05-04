package com.sonorous.events;

import com.sonorous.ability.superclasses.CoreAbility;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


/**
 * @author Sakrajin
 * Event to represent when ability damages another entity
 */
public class AbilityDamageEntityEvent extends Event implements Cancellable {

    private Player attacker;
    private LivingEntity target;
    private CoreAbility coreAbility;

    private double damage;

    public AbilityDamageEntityEvent(Player attacker, LivingEntity target, CoreAbility coreAbility, double damage) {
        this.attacker = attacker;
        this.target = target;
        this.coreAbility = coreAbility;
        this.damage = damage;
    }

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

    public Player getAttacker() {
        return attacker;
    }

    public LivingEntity getTarget() {
        return target;
    }

    public CoreAbility getCoreAbility() {
        return coreAbility;
    }

    public double getDamage() {
        return damage;
    }
}
