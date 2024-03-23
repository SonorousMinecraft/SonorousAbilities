package com.sereneoasis.abilityuilities.particles;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.methods.*;
import org.bukkit.Location;
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

    private ArchetypeVisuals.ArchetypeVisual archetypeVisual;

    public SourcedBlast(Player player, String name, boolean directable, ArchetypeVisuals.ArchetypeVisual archetypeVisual, boolean selfPush) {
        super(player, name);
        this.shot = false;
        this.selfPush = selfPush;
        this.name = name;
        this.directable = directable;
        this.archetypeVisual = archetypeVisual;
        this.loc = Locations.getFacingLocationObstructed(player.getEyeLocation(), player.getEyeLocation().getDirection(), sourceRange).subtract(player.getEyeLocation().getDirection().clone().multiply(radius));
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
        archetypeVisual.playVisual(loc, size, radius, 10, 1, 5);
        //TDBs.playTDBs(loc, DisplayBlock.AIR, 5, size, hitbox);
        //Particles.spawnParticle(particle, loc, 5, hitbox, 0);

    }

    public void setHasClicked() {
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
