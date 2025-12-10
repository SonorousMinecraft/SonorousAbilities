package com.sonorous.abilityuilities.particles;

import com.sonorous.ability.superclasses.CoreAbility;
import com.sonorous.util.AbilityStatus;
import com.sonorous.util.methods.ArchetypeVisuals;
import com.sonorous.util.methods.Locations;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Allows a player to charge a sphere that grows in size as time passes.
 */
public class ChargeSphere extends CoreAbility {

    private final String name;


    private final long startTime;

    private double startRadius;
    private final double increment;
    private final ArchetypeVisuals.ArchetypeVisual archetypeVisual;

    private Location loc;

    public ChargeSphere(Player player, String name, double startRadius, ArchetypeVisuals.ArchetypeVisual archetypeVisual) {
        super(player, name);

        this.name = name;
        this.abilityStatus = AbilityStatus.CHARGING;

        this.startRadius = startRadius;
        this.startTime = System.currentTimeMillis();
        this.increment = ((radius - startRadius) / chargeTime) * 50;
        this.archetypeVisual = archetypeVisual;
        loc = Locations.getFacingLocation(player.getEyeLocation(), player.getEyeLocation().getDirection(), startRadius + 2);
        start();
    }

    @Override
    public void progress() {
        if (System.currentTimeMillis() > startTime + chargeTime) {
            this.abilityStatus = AbilityStatus.CHARGED;
        }

        loc = Locations.getFacingLocation(player.getEyeLocation(), player.getEyeLocation().getDirection(), startRadius + 2);

        for (Location l : Locations.getSphere(loc,
                startRadius, 6)) {
            if (l != null) {
                archetypeVisual.playVisual(l, size, 0.5, 1, 1, 1);
            }
        }

        if (abilityStatus == AbilityStatus.CHARGING) {
            startRadius += increment;
        }
    }

    public double getCurrentRadius() {
        return startRadius;
    }

    public Location getLoc() {
        return loc.clone();
    }



    @Override
    public String getName() {
        return name;
    }
}
