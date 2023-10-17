package com.sereneoasis.abilityuilities.blocks;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.methods.Entities;
import com.sereneoasis.util.methods.Locations;
import com.sereneoasis.util.methods.Vectors;
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
public class ShootBlockToLoc extends CoreAbility {

    private Location loc;
    private String user;

    private Material type;

    private Vector dir;

    private AbilityStatus abilityStatus;

    private Location endLoc;


    public ShootBlockToLoc(Player player, String user, Location startLoc, Material type, Location endLoc) {
        super(player, user);
        this.user = user;
        this.type = type;
        this.loc = startLoc;
        this.endLoc = endLoc;
        this.dir = Vectors.getDirectionBetweenLocations(startLoc,endLoc).normalize();
        this.abilityStatus = AbilityStatus.SHOT;
        start();
    }

    @Override
    public void progress() {


        if (loc.distance(endLoc) < 3) {
            abilityStatus = AbilityStatus.COMPLETE;
            return;
        }

        List<Location> locs = null;

        locs = Locations.getShotLocations(loc, 20, dir, speed);


        for (Location point : locs)
        {
            new TempDisplayBlock(point, type.createBlockData(), 200, Math.random());
        }

        DamageHandler.damageEntity(Entities.getAffected(loc, radius, player), player, this, damage);
        loc.add(dir.clone().multiply(speed));

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
