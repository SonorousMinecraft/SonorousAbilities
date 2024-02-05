package com.sereneoasis.abilityuilities.blocks;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.archetypes.DisplayBlock;
import com.sereneoasis.util.methods.Locations;
import com.sereneoasis.util.methods.Vectors;
import com.sereneoasis.util.temp.TempDisplayBlock;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;

/**
 * @author Sakrajin
 * Creates a ring of blocks around a player
 */
public class BlockRingAroundPlayer extends CoreAbility {

    private Location loc;

    private String user;

    private double ringSize;

    private DisplayBlock type;

    private int orientation;

    private Vector dir;

    private int rotation;

    private int rotatePerTick;

    private boolean clockwise;

    public BlockRingAroundPlayer(Player player, String user, Location startLoc, DisplayBlock type, double ringSize, int orientation, int rotatePerTick, boolean clockwise) {
        super(player, user);

        this.user = user;
        this.type = type;
        this.ringSize = ringSize;
        this.orientation = orientation;
        this.rotatePerTick = rotatePerTick;
        this.clockwise = clockwise;
        loc = startLoc;
        this.dir = Vectors.getDirectionBetweenLocations(startLoc, player.getEyeLocation()).setY(0).normalize();
        rotation = Math.round(player.getEyeLocation().getYaw());
        start();
    }

    @Override
    public void progress() {

        dir = player.getEyeLocation().getDirection().setY(0).normalize();

        List<Location> locs = Locations.getArcFromTrig(player.getEyeLocation(), ringSize, rotatePerTick, dir, orientation,
                rotation, rotation + rotatePerTick, clockwise);
        loc = locs.get(locs.size() - 1);

        for (Location point : locs) {
            new TempDisplayBlock(point, type, 200, Math.random() * hitbox);
        }

        rotation += rotatePerTick;
//        List<Location> currentLocs = Locations.getCircle(player.getEyeLocation(), radius, 360)
//                .subList(Math.floorMod(Math.abs(rotation) + Math.abs(rotatePerTick), 360), Math.floorMod(Math.abs(rotation), 360));
//        for (Location point : currentLocs)
//        {
//            new TempDisplayBlock(point, type.createBlockData(), 100, 0.05);
//        }


        //new TempDisplayBlock(loc, type.createBlockData(), 500, 1);
        //new TempBlock(loc.getBlock(), type.createBlockData(), 500, false);
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public Location getLocation() {
        return loc;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public String getName() {
        return user;
    }
}
