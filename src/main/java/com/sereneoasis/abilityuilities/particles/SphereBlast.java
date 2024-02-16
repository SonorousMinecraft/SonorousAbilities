package com.sereneoasis.abilityuilities.particles;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.methods.ArchetypeVisuals;
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


    private String name;

    private ArchetypeVisuals.ArchetypeVisual archetypeVisual;

    public SphereBlast(Player player, String name, boolean directable, ArchetypeVisuals.ArchetypeVisual archetypeVisual) {
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


        for (Location loc : Locations.getSphere(loc, radius, 12))
        {
            archetypeVisual.playVisual(loc, size, 0, 1, 1, 1);
        }

        DamageHandler.damageEntity(Entities.getAffected(loc, hitbox, player), player, this, damage);

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
