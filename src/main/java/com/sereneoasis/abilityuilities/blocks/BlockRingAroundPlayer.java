package com.sereneoasis.abilityuilities.blocks;

import com.sereneoasis.util.Methods;
import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.TempBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class BlockRingAroundPlayer extends CoreAbility {

    private Location loc;

    private CoreAbility user;

    private double ringSize;

    private Material type;

    private int orientation;

    private Vector dir;

    public BlockRingAroundPlayer(Player player, CoreAbility user, Location startLoc, Material type, double ringSize, int orientation) {
        super(player);

        this.user = user;
        this.type = type;
        this.ringSize = ringSize;
        this.orientation = orientation;
        loc = startLoc;
        this.dir = Methods.getDirectionBetweenLocations(loc, player.getEyeLocation()).setY(0).normalize();
        start();
    }

    @Override
    public void progress() {

        loc = player.getEyeLocation().add(dir.clone().multiply(ringSize).
                rotateAroundY(Math.toRadians(15)).rotateAroundAxis(player.getEyeLocation().getDirection(), orientation));
        new TempBlock(loc.getBlock(), type.createBlockData(), 2000);
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
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
