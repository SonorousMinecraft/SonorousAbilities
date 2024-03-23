package com.sereneoasis.abilityuilities.particles;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.ArchetypeVisuals;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * @author Sakrajin
 * Basic particle based blast ability
 */
public class Blast extends CoreAbility {

    private boolean directable;

    private Location loc, origin;
    private Vector dir;


    private String name;

    private ArchetypeVisuals.ArchetypeVisual archetypeVisual;

    public Blast(Player player, String name, boolean directable, ArchetypeVisuals.ArchetypeVisual archetypeVisual) {
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
        archetypeVisual.playVisual(loc, size, radius, 10, 1, 5);

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
