package com.sereneoasis.archetypes.sun;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.methods.Locations;
import org.bukkit.entity.Player;

/**
 * @author Sakrajin
 *
 */
public class SolarFlare extends CoreAbility {
    public SolarFlare(Player player) {
        super(player);


        start();
    }

    @Override
    public void progress() {

    }

    @Override
    public Player getPlayer() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }
}
