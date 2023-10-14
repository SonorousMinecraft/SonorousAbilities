package com.sereneoasis.abilityuilities.blocks;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.Methods;
import com.sereneoasis.util.methods.Locations;
import com.sereneoasis.util.methods.Vectors;
import com.sereneoasis.util.temp.TempBlock;
import com.sereneoasis.util.temp.TempDisplayBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class BlockRingAroundPlayer extends CoreAbility {

    private Location loc;

    private String user;

    private double ringSize;

    private Material type;

    private int orientation;

    private Vector dir;

    private int rotation;

    private double rotatePerTick;

    public BlockRingAroundPlayer(Player player, String user, Location startLoc, Material type, double ringSize, int orientation, double rotatePerTick) {
        super(player, user);

        this.user = user;
        this.type = type;
        this.ringSize = ringSize;
        this.orientation = orientation;
        this.rotatePerTick = rotatePerTick;
        loc = startLoc;
        this.dir = Vectors.getDirectionBetweenLocations(loc, player.getEyeLocation()).setY(0).normalize();
        rotation = 0;
        start();
    }

    @Override
    public void progress() {
        loc = player.getEyeLocation().add(dir.clone().multiply(ringSize).rotateAroundY(Math.toRadians(rotation)).rotateAroundAxis(player.getEyeLocation().getDirection().setY(0).normalize(), orientation));
        if (orientation > 0) {
            rotation += rotatePerTick;
        }
        else{
            rotation -= rotatePerTick;

        }
        Vector playerToLoc = Vectors.getDirectionBetweenLocations(player.getEyeLocation(), loc);
        Locations.getPivotedLocations(Locations.getDisplayEntityLocs(loc, 2.0, 1),
                        player.getEyeLocation().add(playerToLoc), playerToLoc )
                .forEach(tempBlockLoc -> {
            new TempDisplayBlock(tempBlockLoc, type.createBlockData(), 500, 1);
        });

        //new TempDisplayBlock(loc, Material.LIGHT_BLUE_STAINED_GLASS_PANE.createBlockData(), 500, 1);
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
