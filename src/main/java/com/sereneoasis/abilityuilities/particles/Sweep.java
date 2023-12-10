package com.sereneoasis.abilityuilities.particles;

import com.sereneoasis.ability.superclasses.CoreAbility;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Sweep extends CoreAbility {

    private final String name;

    private Location origin, loc1, loc2;

    private Vector dir;
    public Sweep(Player player, String name, Location loc1, Location loc2, Location origin, Vector dir) {
        super(player, name);

        this.name = name;
        this.origin = origin;
        this.loc1 = loc1;
        this.loc2 = loc2;
        this.dir = dir;
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
