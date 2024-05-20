package com.sereneoasis.abilityuilities.particles;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.ArchetypeVisuals;
import com.sereneoasis.util.methods.Locations;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author Sakrajin
 * Allows a player to charge a sphere that grows in size as time passes.
 */
public class ChargeSphere extends CoreAbility {

    private String name;


    private long startTime;

    private double startRadius, increment;
    private ArchetypeVisuals.ArchetypeVisual archetypeVisual;

    private Location loc;

    public ChargeSphere(Player player, String name, double startRadius, ArchetypeVisuals.ArchetypeVisual archetypeVisual) {
        super(player, name);

        this.name = name;
        this.abilityStatus = AbilityStatus.CHARGING;

        this.startRadius = startRadius;
        this.startTime = System.currentTimeMillis();
        this.increment = ((radius - startRadius) / chargeTime) * 50;
        this.archetypeVisual = archetypeVisual;
        loc = Locations.getFacingLocation(player.getEyeLocation(), player.getEyeLocation().getDirection(), sourceRange + 1);
        start();
    }

    @Override
    public void progress() {
        if (System.currentTimeMillis() > startTime + chargeTime) {
            this.abilityStatus = AbilityStatus.CHARGED;
        }

        loc = Locations.getFacingLocation(player.getEyeLocation(), player.getEyeLocation().getDirection(), sourceRange + 1);

        for (Location l : Locations.getSphere(loc,
                startRadius, 18))
        {
            if (l != null) {
                archetypeVisual.playVisual(l, size, 0, 1, 1, 1);
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
    public Player getPlayer() {
        return player;
    }

    @Override
    public String getName() {
        return name;
    }
}
