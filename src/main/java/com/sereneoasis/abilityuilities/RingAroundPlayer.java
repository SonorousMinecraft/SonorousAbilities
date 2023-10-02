package com.sereneoasis.abilityuilities;

import com.sereneoasis.Methods;
import com.sereneoasis.ability.CoreAbility;
import com.sereneoasis.util.SourceStatus;
import com.sereneoasis.util.TempBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class RingAroundPlayer extends CoreAbility {

    private Location loc;

    private CoreAbility user;

    private double ringSize;

    private Material type;

    public RingAroundPlayer(Player player, CoreAbility user, Location startLoc, Material type, double ringSize) {
        super(player);

        this.user = user;
        this.type = type;
        this.ringSize = ringSize;
        loc = startLoc;
        start();
    }

    @Override
    public void progress() {

        Vector dir = Methods.getDirectionBetweenLocations(loc, player.getEyeLocation());
        loc = player.getEyeLocation().add(dir.clone().multiply(ringSize));
        new TempBlock(loc.getBlock(), type.createBlockData(), 2000);
    }

    public Location getLoc() {
        return loc;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public String getName() {
        return user.getName();
    }
}
