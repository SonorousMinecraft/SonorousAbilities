package com.sereneoasis.ability.data;

import com.sereneoasis.archetypes.Archetype;

/**
 * @author Sakrajin
 * Stores all the data for a specific ability.
 */
public class AbilityData {

    protected Archetype archetype;

    protected String description, instructions;
    protected long chargetime, cooldown, duration;

    protected double damage, hitbox, radius, range, speed, sourceRange, size;

    public AbilityData(Archetype archetype, String description, String instructions,
                       long chargetime, long cooldown, long duration,
                       double damage, double hitbox, double radius, double range, double speed, double sourceRange, double size) {

        this.archetype = archetype;
        this.description = description;
        this.instructions = instructions;

        this.chargetime = chargetime;
        this.cooldown = cooldown;
        this.duration = duration;

        this.damage = damage;
        this.hitbox = hitbox;
        this.radius = radius;
        this.range = range;
        this.speed = speed;
        this.sourceRange = sourceRange;
        this.size = size;
    }

    public Archetype getArchetype() {
        return archetype;
    }

    public String getDescription() {
        return description;
    }

    public String getInstructions() {
        return instructions;
    }

    public long getCooldown() {
        return cooldown;
    }

    public long getChargetime() {
        return chargetime;
    }

    public long getDuration() {
        return duration;
    }

    public double getDamage() {
        return damage;
    }

    public double getHitbox() {
        return hitbox;
    }

    public double getRadius() {
        return radius;
    }

    public double getRange() {
        return range;
    }

    public double getSpeed() {
        return speed;
    }

    public double getSourceRange() {
        return sourceRange;
    }

    public double getSize() {
        return size;
    }
}