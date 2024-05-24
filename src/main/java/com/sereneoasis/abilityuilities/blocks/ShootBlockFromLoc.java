package com.sereneoasis.abilityuilities.blocks;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.methods.AbilityDamage;
import com.sereneoasis.util.methods.Blocks;
import com.sereneoasis.util.methods.Entities;
import com.sereneoasis.util.temp.TempDisplayBlock;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * @author Sakrajin
 * Allows the shooting of a block from a player
 */
public class ShootBlockFromLoc extends CoreAbility {

    private Location loc;
    private String user;

    private boolean directable, autoRemove;

    public boolean isDirectable() {
        return directable;
    }

    public void setDirectable(boolean directable) {
        this.directable = directable;
    }

    private Vector dir;

    private TempDisplayBlock block;

    public void setRange(double newRange){
        this.range = newRange;
    }


    public ShootBlockFromLoc(Player player, String user, Location startLoc, Material type, boolean directable, boolean autoRemove) {
        super(player, user);
        this.user = user;
        this.loc = startLoc;
        this.directable = directable;
        this.autoRemove = autoRemove;
        this.dir = player.getEyeLocation().getDirection().normalize();
        this.abilityStatus = AbilityStatus.SHOT;

        block = new TempDisplayBlock(loc, type, 60000, size);
        abilityStatus = AbilityStatus.SHOT;
        start();
    }


    public ShootBlockFromLoc(Player player, String user, Location startLoc, Material type, boolean directable, boolean autoRemove, double size) {
        super(player, user);
        this.user = user;
        this.loc = startLoc;
        this.directable = directable;
        this.autoRemove = autoRemove;
        this.dir = player.getEyeLocation().getDirection().normalize();
        this.abilityStatus = AbilityStatus.SHOT;

        block = new TempDisplayBlock(loc, type, 60000, size);
        abilityStatus = AbilityStatus.SHOT;
        start();
    }

    public ShootBlockFromLoc(Player player, String user, Location startLoc, Material type, boolean autoRemove, Vector dir) {
        super(player, user);
        this.user = user;
        this.loc = startLoc;
        this.directable = false;
        this.autoRemove = autoRemove;
        this.dir = dir.clone();
        this.abilityStatus = AbilityStatus.SHOT;

        block = new TempDisplayBlock(loc, type, 60000, size);
        abilityStatus = AbilityStatus.SHOT;
        start();
    }

    public void setGlowing(Color color  ){
        block.getBlockDisplay().setGlowColorOverride(color);
        block.getBlockDisplay().setGlowing(true);
    }

    @Override
    public void progress() {


        if (abilityStatus == AbilityStatus.SHOT || abilityStatus == AbilityStatus.HIT_SOLID) {

            loc.add(dir.clone().multiply(speed));
            block.moveToAndMaintainFacing(loc);

            if (directable) {
                dir = player.getEyeLocation().getDirection().normalize();
            }


            if (Blocks.isSolid(loc)) {
                abilityStatus = AbilityStatus.HIT_SOLID;
                if (autoRemove) {
                    this.remove();
                }
            } else if (AbilityDamage.damageOne(loc, this, player, true, dir)) {
                abilityStatus = AbilityStatus.DAMAGED;
                if (autoRemove) {
                    this.remove();
                }
            } else if (loc.distance(player.getEyeLocation()) > range) {
                abilityStatus = AbilityStatus.COMPLETE;
                if (autoRemove) {
                    this.remove();
                }
            }

        }

    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }

    public void setDir(Vector dir) {
        this.dir = dir;
    }

    public Vector getDir() {
        return dir;
    }

    @Override
    public void remove() {
        super.remove();
        block.revert();
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

    public TempDisplayBlock getBlock() {
        return block;
    }
}
