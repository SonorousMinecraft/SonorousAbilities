package com.sonorous.abilityuilities.particles;

import com.sonorous.ability.superclasses.CoreAbility;
import com.sonorous.util.AbilityStatus;
import com.sonorous.util.DamageHandler;
import com.sonorous.util.methods.ArchetypeVisuals;
import com.sonorous.util.methods.Entities;
import com.sonorous.util.methods.Locations;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;

public class Blade extends CoreAbility {

    private final String name;

    private final Location origin, loc1, loc2;

    private final ArchetypeVisuals.ArchetypeVisual archetypeVisual;

    private final Vector dir;

    public Blade(Player player, String name, ArchetypeVisuals.ArchetypeVisual archetypeVisual, Location loc1, Location loc2) {
        super(player, name);

        this.archetypeVisual = archetypeVisual;
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

            //Particles.playParticlesBetweenPoints(particle, loc1, loc2, 0.1, 5, 0.5, 0);

            for (Location loc : Locations.getPointsAlongLine(loc1, loc2, size)) {
                archetypeVisual.playVisual(loc, size, 0.1, 10, 1, 5);
            }

            if (Entities.getEntityBetweenPoints(loc1, loc2) != null) {
                DamageHandler.damageEntity(Entities.getEntityBetweenPoints(loc1, loc2), player, this, damage);
                abilityStatus = AbilityStatus.COMPLETE;
            }

            if (origin.distance(Locations.getMidpoint(loc1, loc2)) > range) {
                abilityStatus = AbilityStatus.COMPLETE;
            }
        }
    }

    public List<Location> getLocs() {
        return Locations.getPointsAlongLine(loc1, loc2, size);
    }

    @Override
    public String getName() {
        return name;
    }
}
