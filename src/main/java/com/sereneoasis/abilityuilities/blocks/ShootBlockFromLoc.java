package com.sereneoasis.abilityuilities.blocks;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.methods.Entities;
import com.sereneoasis.util.methods.Locations;
import com.sereneoasis.util.temp.TempDisplayBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Sakrajin
 * Allows the shooting of a block from a player
 */
public class ShootBlockFromLoc extends CoreAbility {

    private Location loc;
    private String user;

    private Material type;

    private boolean directable;


    private Vector dir;

    private AbilityStatus abilityStatus;

    private LinkedHashMap<Vector, Double> directions = new LinkedHashMap<>();

    private long timeBetweenCurves = 150, lastCurveTime = System.currentTimeMillis();

    public ShootBlockFromLoc(Player player, String user, Location startLoc, Material type, boolean directable) {
        super(player, user);
        this.user = user;
        this.type = type;
        this.loc = startLoc;
        this.directable = directable;
        this.dir = player.getEyeLocation().getDirection().normalize();
        this.abilityStatus = AbilityStatus.SHOT;
        start();
    }

    @Override
    public void progress() {


        if (loc.distance(player.getEyeLocation()) > range) {
            abilityStatus = AbilityStatus.COMPLETE;
            return;
        }

        List<Location> locs = null;

        if (directable) {
            dir = player.getEyeLocation().getDirection().normalize();
            if (System.currentTimeMillis() > lastCurveTime+timeBetweenCurves) {
                directions.put(dir, speed);
                lastCurveTime = System.currentTimeMillis();
            }
            locs = Locations.getBezierCurveLocations(loc, 20, directions, speed);

        }
        else{
            locs = Locations.getShotLocations(loc, 20, dir, speed);
        }


        for (Location point : locs)
        {
            new TempDisplayBlock(point, type.createBlockData(), 200, Math.random());
        }

        DamageHandler.damageEntity(Entities.getAffected(loc, hitbox, player), player, this, damage);
        loc.add(dir.clone().multiply(speed));

    }

    public void setDir(Vector dir) {
        this.dir = dir;
    }

    public Location getLoc() {
        return loc;
    }

    public AbilityStatus getAbilityStatus() {
        return abilityStatus;
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
