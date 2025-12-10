package com.sonorous.abilityuilities.particles;

import com.sonorous.ability.superclasses.CoreAbility;
import com.sonorous.util.AbilityStatus;
import com.sonorous.util.DamageHandler;
import com.sonorous.util.methods.ArchetypeVisuals;
import com.sonorous.util.methods.Entities;
import com.sonorous.util.methods.Locations;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Causes a spherical shaped blast to be shot from the player
 */
public class SphereBlast extends CoreAbility {

    private final boolean directable;

    private Location loc;
    private final Location origin;
    private Vector dir;


    private final String name;

    private final ArchetypeVisuals.ArchetypeVisual archetypeVisual;

    private boolean shouldDamage;


    public SphereBlast(Player player, String name, boolean directable, ArchetypeVisuals.ArchetypeVisual archetypeVisual, boolean shoulDamage) {
        super(player, name);
        this.name = name;
        this.directable = directable;
        this.archetypeVisual = archetypeVisual;
        this.loc = player.getEyeLocation();
        this.origin = loc.clone();
        this.dir = loc.getDirection();
        this.abilityStatus = AbilityStatus.SHOT;
        start();
    }

    @Override
    public void progress() {

        if (loc.distance(origin) > range) {
            this.abilityStatus = AbilityStatus.COMPLETE;
        }

        if (directable) {
            dir = player.getEyeLocation().getDirection().normalize();
        }

        loc.add(dir.clone().multiply(speed));


        for (Location loc : Locations.getSphere(loc, radius, 12)) {
            archetypeVisual.playVisual(loc, size, 0, 1, 1, 1);
        }

        if (shouldDamage) {
            DamageHandler.damageEntity(Entities.getAffected(loc, hitbox, player), player, this, damage);
            if (!Entities.getAffected(loc, hitbox, player).isEmpty()) {
                Bukkit.broadcastMessage("should be damaging");
            }
        }

    }

    public Location getLoc() {
        return loc;
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }


    @Override
    public String getName() {
        return name;
    }
}
