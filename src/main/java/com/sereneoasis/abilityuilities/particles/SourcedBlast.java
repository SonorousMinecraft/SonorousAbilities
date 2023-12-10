package com.sereneoasis.abilityuilities.particles;

import com.sereneoasis.ability.superclasses.Ability;
import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.AbilityStatus;

import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.methods.Entities;
import com.sereneoasis.util.methods.Locations;
import com.sereneoasis.util.methods.Particles;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * @author Sakrajin
 * Basic particle based blast ability
 */
public class SourcedBlast extends CoreAbility {

    private boolean shot, directable, selfPush;

    private Location loc, origin;
    private Vector dir;

    private String name;

    private Particle particle;
    public SourcedBlast(Player player, String name, boolean directable, Particle particle, boolean selfPush) {
        super(player, name);
        this.shot = false;
        this.selfPush = selfPush;
        this.name = name;
        this.directable = directable;
        this.particle = particle;
        this.loc = Locations.getFacingLocationObstructed(player.getEyeLocation(), player.getEyeLocation().getDirection(), sourceRange);
        this.abilityStatus = AbilityStatus.SOURCE_SELECTED;
        start();
    }


    @Override
    public void progress() {

        if (shot) {
            if (loc.distance(origin) > range) {
                this.abilityStatus = AbilityStatus.COMPLETE;
            }

            if (directable) {
                dir = player.getEyeLocation().getDirection().normalize();
            }

            DamageHandler.damageEntity(Entities.getAffected(loc, hitbox, player), player, this, damage);
            for (Entity e : Entities.getEntitiesAroundPoint(loc, radius)) {
                if (e.getUniqueId() == player.getUniqueId() && selfPush || e.getUniqueId() != player.getUniqueId()) {
                    e.setVelocity(this.dir.clone().multiply(speed));
                }
            }
            loc.add(dir.clone().multiply(speed));
        }
        Particles.spawnParticle(particle, loc, 5, hitbox, 0);

    }

    public void setHasClicked(){
        if (abilityStatus == AbilityStatus.SOURCE_SELECTED) {
            shot = true;
            this.origin = player.getEyeLocation().clone();
            this.dir = origin.getDirection();
        }
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public String getName() {
        return name;
    }
}
