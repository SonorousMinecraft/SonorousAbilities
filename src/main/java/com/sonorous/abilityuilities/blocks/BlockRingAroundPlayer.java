package com.sonorous.abilityuilities.blocks;

import com.sonorous.ability.superclasses.CoreAbility;
import com.sonorous.util.methods.Locations;
import com.sonorous.util.methods.Vectors;
import com.sonorous.util.temp.TempDisplayBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates a ring of blocks around a player
 */
public class BlockRingAroundPlayer extends CoreAbility {

    private Location loc;

    private final String user;

    private final double ringSize;

    private final Material type;

    private int orientation;

    private Vector dir;

    private int rotation;

    private final int rotatePerTick;

    private final boolean clockwise;

    private final List<TempDisplayBlock> blocks = new ArrayList<>();

    public BlockRingAroundPlayer(Player player, String user, Location startLoc, Material type, double ringSize, int orientation, int rotatePerTick, boolean clockwise) {
        super(player, user);

        this.user = user;
        this.type = type;
        this.ringSize = ringSize;
        this.orientation = orientation;
        this.rotatePerTick = rotatePerTick;
        this.clockwise = clockwise;
        loc = startLoc.clone();
        this.dir = Vectors.getDirectionBetweenLocations(startLoc, player.getEyeLocation()).setY(0).normalize();
        rotation = Math.round(player.getEyeLocation().getYaw());

        for (int i = 0; i < rotatePerTick; i++) {
            blocks.add(new TempDisplayBlock(startLoc, type, 60000, size));
        }

        start();
    }

    @Override
    public void progress() {

        dir = player.getEyeLocation().getDirection().setY(0).normalize();
        int arcDegrees = (int) ((rotatePerTick * size * 360) / (2 * Math.PI * ringSize));
        List<Location> locs = Locations.getArcFromTrig(player.getEyeLocation(), ringSize, rotatePerTick, dir, orientation,
                rotation, rotation + arcDegrees, clockwise);
        loc = locs.get(locs.size() - 1);

        for (int i = 0; i < rotatePerTick; i++) {
            blocks.get(i).moveTo(locs.get(i).clone().add(Math.random(), Math.random(), Math.random()));
        }


        rotation += rotatePerTick;

    }


    public Material getType() {
        return type;
    }

    @Override
    public void remove() {
        super.remove();
        blocks.forEach(TempDisplayBlock::revert);
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public Location getLocation() {
        return loc;
    }


    @Override
    public String getName() {
        return user;
    }
}
