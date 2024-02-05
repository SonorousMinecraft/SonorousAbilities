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

public class Blade extends CoreAbility {

    private final String name;

    private Location origin, loc1, loc2;

    private Particle particle;

    private Vector dir;

    public Blade(Player player, String name, Particle particle, Location loc1, Location loc2) {
        super(player, name);

        this.particle = particle;
        this.name = name;
        this.loc1 = loc1.clone();
        this.loc2 = loc2.clone();
        this.origin = Locations.getMidpoint(loc1, loc2).clone();

        this.dir = player.getEyeLocation().getDirection().clone().normalize();
        abilityStatus = AbilityStatus.SHOT;
        start();
    }

    @Override
    public void progress() {

        if (abilityStatus == AbilityStatus.SHOT) {
            loc1.add(dir.clone());
            loc2.add(dir.clone());

            Particles.playParticlesBetweenPoints(particle, loc1, loc2, 0.1, 5, 0.5, 0);

            if (Entities.getEntityBetweenPoints(loc1, loc2) != null) {
                DamageHandler.damageEntity(Entities.getEntityBetweenPoints(loc1, loc2), player, this, damage);
                abilityStatus = AbilityStatus.COMPLETE;
            }

            if (origin.distance(Locations.getMidpoint(loc1, loc2)) > range) {
                abilityStatus = AbilityStatus.COMPLETE;
            }
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
