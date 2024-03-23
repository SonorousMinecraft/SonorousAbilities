package com.sereneoasis.abilityuilities.particles;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.methods.*;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;

public class Sweep extends CoreAbility {

    private final String name;

    private Location origin, loc1, loc2;

    private ArchetypeVisuals.ArchetypeVisual archetypeVisual;

    private Vector dir1, dir2;

    public Sweep(Player player, String name, ArchetypeVisuals.ArchetypeVisual archetypeVisual, Location loc1, Location loc2) {
        super(player, name);

        this.archetypeVisual = archetypeVisual;
        this.name = name;
        this.loc1 = loc1.clone();
        this.loc2 = loc2.clone();
        this.origin = player.getEyeLocation().clone();

        this.dir1 = Vectors.getDirectionBetweenLocations(origin, loc1).normalize();
        this.dir2 = Vectors.getDirectionBetweenLocations(origin, loc2).normalize();
        abilityStatus = AbilityStatus.SHOT;
        start();
    }

    @Override
    public void progress() {

        if (abilityStatus == AbilityStatus.SHOT) {
            loc1.add(dir1.clone());
            loc2.add(dir2.clone());

            List<Location> locs = Locations.getArc(loc1, loc2, origin, 0.2);
            //List<Location> locs = List.of(new Location[]{loc1, loc2});
            for (Location loc : locs) {
                DamageHandler.damageEntity(Entities.getAffected(loc, hitbox, player), player, this, damage);
                archetypeVisual.playVisual(loc, size, 0.1, 10, 1, 5);
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
