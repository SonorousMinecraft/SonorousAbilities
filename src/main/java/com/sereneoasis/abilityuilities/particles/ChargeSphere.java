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

    public ChargeSphere(Player player, String name, double startRadius, ArchetypeVisuals.ArchetypeVisual archetypeVisual) {
        super(player, name);

        this.name = name;
        this.abilityStatus = AbilityStatus.SOURCING;

        this.startRadius = startRadius;
        this.startTime = System.currentTimeMillis();
        this.increment = ((radius - startRadius) / chargeTime) * 50;
        this.archetypeVisual = archetypeVisual;
        start();
    }

    @Override
    public void progress() {
        if (System.currentTimeMillis() > startTime + chargeTime) {
            this.abilityStatus = AbilityStatus.CHARGED;
        }

        for (Location loc : Locations.getSphere(Locations.getFacingLocation(player.getEyeLocation(), player.getEyeLocation().getDirection(), radius + 1),
                startRadius, 12))
        {
            archetypeVisual.playVisual(loc, size, 0, 1, 1, 1);
        }

        startRadius += increment;
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
