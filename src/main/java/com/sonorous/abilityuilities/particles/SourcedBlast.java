package com.sonorous.abilityuilities.particles;

import com.sonorous.ability.superclasses.CoreAbility;
import com.sonorous.util.AbilityStatus;
import com.sonorous.util.DamageHandler;
import com.sonorous.util.methods.ArchetypeVisuals;
import com.sonorous.util.methods.Entities;
import com.sonorous.util.methods.Locations;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Basic particle based blast ability
 */
public class SourcedBlast extends CoreAbility {

    private boolean shot;
    private final boolean directable;
    private final boolean selfPush;

    private final Location loc;
    private Location origin;
    private Vector dir;

    private final String name;

    private final ArchetypeVisuals.ArchetypeVisual archetypeVisual;

    private final boolean shouldDamage;

    public SourcedBlast(Player player, String name, boolean directable, ArchetypeVisuals.ArchetypeVisual archetypeVisual, boolean selfPush, boolean shouldDamage) {
        super(player, name);
        this.shot = false;
        this.selfPush = selfPush;
        this.shouldDamage = shouldDamage;
        this.name = name;
        this.directable = directable;
        this.archetypeVisual = archetypeVisual;
        this.loc = Locations.getFacingLocationObstructed(player.getEyeLocation(), player.getEyeLocation().getDirection(), sourceRange).subtract(player.getEyeLocation().getDirection().clone().multiply(radius));
        this.abilityStatus = AbilityStatus.SOURCE_SELECTED;


        start();
    }


    @Override
    public void progress() {

        if (abilityStatus == AbilityStatus.SHOT) {
            if (loc.distance(origin) > range) {
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
            for (Entity e : Entities.getEntitiesAroundPoint(loc, radius)) {
                if (e.getUniqueId() == player.getUniqueId() && selfPush || e.getUniqueId() != player.getUniqueId()) {
                    e.setVelocity(this.dir.clone().multiply(speed));
                }
            }
            loc.add(dir.clone().multiply(speed));
        }
        archetypeVisual.playVisual(loc, size, radius, 10, 10, 5);
        //TDBs.playTDBs(loc, DisplayBlock.AIR, 5, size, hitbox);
        //Particles.spawnParticle(particle, loc, 5, hitbox, 0);

    }

    public Location getLoc() {
        return loc;
    }

    public void setDir() {
        dir = player.getEyeLocation().getDirection().normalize();
    }

    public void setHasClicked() {
        if (abilityStatus == AbilityStatus.SOURCE_SELECTED) {
            abilityStatus = AbilityStatus.SHOT;
            this.origin = player.getEyeLocation().clone();
            this.dir = origin.getDirection();
        }
    }


    @Override
    public String getName() {
        return name;
    }
}
