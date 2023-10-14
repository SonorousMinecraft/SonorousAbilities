package com.sereneoasis.ability.data;

/**
 * @author Sakrajin
 * Stores all the data for a specific ability.
 */
public class AbilityData {

    protected String archetype, description, instructions;
    protected long chargetime, cooldown,  duration;

    protected double damage, radius, range, speed, sourceRange;

    public AbilityData(String description, String instructions,
                       long chargetime, long cooldown, long duration,
                       double damage, double radius, double range, double speed, double sourceRange) {

        this.description = description;
        this.instructions = instructions;

        this.chargetime = chargetime;
        this.cooldown = cooldown;
        this.duration = duration;

        this.damage = damage;
        this.radius = radius;
        this.range = range;
        this.speed = speed;
        this.sourceRange = sourceRange;
    }

    public String getArchetype() {
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
}