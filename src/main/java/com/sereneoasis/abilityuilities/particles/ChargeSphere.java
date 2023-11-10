package com.sereneoasis.abilityuilities.particles;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.Locations;
import com.sereneoasis.util.methods.Particles;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

/**
 * @author Sakrajin
 * Allows a player to charge a sphere that grows in size as time passes.
 */
public class ChargeSphere extends CoreAbility {

    private String name;

    
    private long startTime;

    private double startRadius, increment;
    private Particle particle;
    public ChargeSphere(Player player, String name, double startRadius, Particle particle) {
        super(player, name);

        this.name = name;
        this.abilityStatus = AbilityStatus.SOURCING;

        this.startRadius = startRadius;
        this.startTime = System.currentTimeMillis();
        this.increment = ((radius - startRadius)/ chargeTime) * 50;
        this.particle = particle;
        start();
    }

    @Override
    public void progress() {
        if (System.currentTimeMillis() > startTime+ chargeTime)
        {
            this.abilityStatus = AbilityStatus.CHARGED;
        }
        Particles.playSphere(Locations.getFacingLocation(player.getEyeLocation(), player.getEyeLocation().getDirection(), radius+1),
                startRadius, 12, particle);
        startRadius += increment;
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
