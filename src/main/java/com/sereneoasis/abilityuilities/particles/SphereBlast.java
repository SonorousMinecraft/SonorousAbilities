package com.sereneoasis.abilityuilities.particles;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.methods.Entities;
import com.sereneoasis.util.methods.Locations;
import com.sereneoasis.util.methods.Particles;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * @author Sakrajin
 * Causes a spherical shaped blast to be shot from the player
 */
public class SphereBlast extends CoreAbility {

    private boolean directable;

    private Location loc, origin;
    private Vector dir;

    private AbilityStatus abilityStatus;

    private String name;

    private Particle particle;
    public SphereBlast(Player player, String name, boolean directable, Particle particle) {
        super(player, name);

        this.name = name;
        this.directable = directable;
        this.particle = particle;

        this.origin = player.getEyeLocation();
        this.dir = origin.getDirection();
        this.loc = origin.clone().add(dir.clone().multiply(radius));
        this.abilityStatus = AbilityStatus.SHOT;
        start();
    }


    @Override
    public void progress() {
        if (loc.distance(origin) > range)
        {
            this.abilityStatus = AbilityStatus.COMPLETE;
        }

        if (directable) {
            dir = player.getEyeLocation().getDirection().normalize();
        }

        loc.add(dir.clone().multiply(speed));
        Particles.playSphere(Locations.getFacingLocation(player.getEyeLocation(), player.getEyeLocation().getDirection(), 1),
                radius, 1, particle);

        DamageHandler.damageEntity(Entities.getAffected(loc, radius, player), player, this, damage);

    }

    public AbilityStatus getAbilityStatus() {
        return abilityStatus;
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
