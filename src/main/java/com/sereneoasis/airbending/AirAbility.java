package com.sereneoasis.airbending;

import com.sereneoasis.CoreAbility;
import com.sereneoasis.Element;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public abstract class AirAbility extends CoreAbility {
    public AirAbility(Player player) {
        super(player);
        this.element = Element.AIR;
    }

    protected void playAirParticles(Location loc)
    {
        loc.getWorld().spawnParticle(Particle.SNOWBALL,loc.getX(),loc.getY(),loc.getZ(),5);
    }
}
