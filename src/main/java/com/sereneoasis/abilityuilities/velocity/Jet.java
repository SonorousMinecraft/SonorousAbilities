package com.sereneoasis.abilityuilities.velocity;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Velocity is in units of 1/8000 of a block per server tick (50ms); for example, -1343 would move (-1343 / 8000) = −0.167875 blocks per tick (or −3.3575 blocks per second).
 */
public class Jet extends CoreAbility {

    private final String name;

    public Jet(Player player, String name) {
        super(player, name);

        abilityStatus = AbilityStatus.MOVING;
        this.name = name;
        start();
    }

    @Override
    public void progress() {

        if (System.currentTimeMillis() > startTime + (duration)) {
            abilityStatus = AbilityStatus.COMPLETE;
        }


        Vector dir = player.getLocation().add(0, 1, 0).getDirection().normalize();
        player.setVelocity(dir.multiply(speed));

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
