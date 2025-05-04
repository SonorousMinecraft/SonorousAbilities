package com.sonorous.abilityuilities.particles;

import com.sonorous.ability.superclasses.CoreAbility;
import com.sonorous.util.AbilityStatus;
import com.sonorous.util.DamageHandler;
import com.sonorous.util.methods.ArchetypeVisuals;
import com.sonorous.util.methods.Entities;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Basic particle based blast ability
 */
public class Blast extends CoreAbility {

    private final boolean directable;

    private final Location loc, origin;
    private Vector dir;


    private final String name;

    private final ArchetypeVisuals.ArchetypeVisual archetypeVisual;

    private double angle = 0;

    private final boolean shouldDamage;


    public Blast(Player player, String name, boolean directable, ArchetypeVisuals.ArchetypeVisual archetypeVisual, boolean shouldDamage) {
        super(player, name);
        this.name = name;
        this.directable = directable;
        this.archetypeVisual = archetypeVisual;
        this.shouldDamage = shouldDamage;
        this.loc = player.getEyeLocation();
        this.origin = loc.clone();
        this.dir = loc.getDirection();
        this.abilityStatus = AbilityStatus.SHOT;
        start();
    }


    @Override
    public void progress() {
        if (abilityStatus != AbilityStatus.COMPLETE) {
            if (loc.distanceSquared(origin) > range * range) {
                this.abilityStatus = AbilityStatus.COMPLETE;
            }

            if (directable) {
                dir = player.getEyeLocation().getDirection().normalize();
            }

            if (shouldDamage) {
                if (DamageHandler.damageEntity(Entities.getAffected(loc, hitbox, player), player, this, damage)) {
                    abilityStatus = AbilityStatus.DAMAGED;

                }
            }

            loc.add(dir.clone().multiply(speed));
            archetypeVisual.playShotVisual(loc, dir, angle, size, radius, 10, 1, 5);
            angle += 36 * speed;
        }
    }

    public Location getLoc() {
        return loc;
    }


    @Override
    public String getName() {
        return name;
    }
}
