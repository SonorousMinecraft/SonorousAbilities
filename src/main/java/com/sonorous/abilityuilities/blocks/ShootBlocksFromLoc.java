package com.sonorous.abilityuilities.blocks;

import com.sonorous.ability.superclasses.CoreAbility;
import com.sonorous.util.AbilityStatus;
import com.sonorous.util.DamageHandler;
import com.sonorous.util.methods.Entities;
import com.sonorous.util.methods.Locations;
import com.sonorous.util.temp.TempDisplayBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Allows the shooting of a block from a player
 */
public class ShootBlocksFromLoc extends CoreAbility {

    private final Location loc;
    private final String user;

    private Material type;

    private final boolean directable;
    private final boolean autoRemove;


    private Vector dir;

    private final LinkedHashMap<Vector, Double> directions = new LinkedHashMap<>();

    private final long timeBetweenCurves = 150;
    private long lastCurveTime = System.currentTimeMillis();

    private final int amount;

    private final List<TempDisplayBlock> blocks = new ArrayList<>();

    public ShootBlocksFromLoc(Player player, String user, Location startLoc, Material type, boolean directable, boolean autoRemove, int amount) {
        super(player, user);
        this.user = user;
        this.type = type;
        this.loc = startLoc;
        this.directable = directable;
        this.autoRemove = autoRemove;
        this.dir = player.getEyeLocation().getDirection().normalize();
        this.abilityStatus = AbilityStatus.SHOT;
        this.amount = amount;


        List<Location> locs = Locations.getShotLocations(loc, amount, dir, speed);

//        if (directable){
//            locs = Locations.getBezierCurveLocations(loc, amount, directions, speed);
//            directions.put(dir, speed);
//        }
        directions.put(dir, speed);
        for (Location point : locs) {
            TempDisplayBlock tdb = new TempDisplayBlock(point, type, 60000, size);
            blocks.add(tdb);
        }


        start();
    }

    @Override
    public void progress() {


        if (loc.distance(player.getEyeLocation()) > range) {
            abilityStatus = AbilityStatus.COMPLETE;
            if (autoRemove) {
                this.remove();
            }

            return;
        }

        List<Location> locs = null;

        if (directable) {
            dir = player.getEyeLocation().getDirection().normalize();
            if (System.currentTimeMillis() > lastCurveTime + timeBetweenCurves) {
                directions.put(dir, speed);
                lastCurveTime = System.currentTimeMillis();
            }
            locs = Locations.getBezierCurveLocations(loc, amount, directions, speed);

        } else {
            locs = Locations.getShotLocations(loc, amount, dir, speed);
        }


        for (int i = 0; i < amount; i++) {
            if (!directable || locs.size() == amount) {
                blocks.get(i).moveTo(locs.get(i).clone().add(Math.random(), Math.random(), Math.random()));
            }
        }

        DamageHandler.damageEntity(Entities.getAffected(loc, hitbox, player), player, this, damage);
        loc.add(dir.clone().multiply(speed));

    }

    @Override
    public void remove() {
        super.remove();
        blocks.forEach(TempDisplayBlock::revert);

    }

    public void setDir(Vector dir) {
        this.dir = dir;
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
        return user;
    }
}
