package com.sonorous.abilityuilities.blocks;

import com.sonorous.ability.superclasses.CoreAbility;
import com.sonorous.util.AbilityStatus;
import com.sonorous.util.methods.AbilityDamage;
import com.sonorous.util.methods.Blocks;
import com.sonorous.util.temp.TempDisplayBlock;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Set;


public class ShootBlockShapeFromLoc extends CoreAbility {

    private Location loc;
    private String user;

    private boolean directable, autoRemove;
    private Vector dir;
    private Set<TempDisplayBlock> blocks;

    public ShootBlockShapeFromLoc(Player player, String user, Location loc, Set<TempDisplayBlock> blocks, double radius, boolean autoRemove, Vector dir) {
        super(player, user);
        this.user = user;

        this.blocks = blocks;

        this.directable = false;
        this.autoRemove = autoRemove;
        this.dir = dir.clone();
        this.loc = loc.clone();
        this.hitbox = radius;


        abilityStatus = AbilityStatus.SHOT;
        start();
    }

    public boolean isDirectable() {
        return directable;
    }

    public void setDirectable(boolean directable) {
        this.directable = directable;
    }


//    public ShootBlockShapeFromLoc(Player player, String user, Location startLoc, Material type, boolean directable, boolean autoRemove) {
//        super(player, user);
//        this.user = user;
//        this.loc = startLoc;
//        this.directable = directable;
//        this.autoRemove = autoRemove;
//        this.dir = player.getEyeLocation().getDirection().normalize();
//        this.abilityStatus = AbilityStatus.SHOT;
//
//        block = new TempDisplayBlock(loc, type, 60000, size);
//        abilityStatus = AbilityStatus.SHOT;
//        start();
//    }

    public void setRange(double newRange) {
        this.range = newRange;
    }

    public void setGlowing(Color color) {
        blocks.forEach(block -> {
            block.getBlockDisplay().setGlowColorOverride(color);
            block.getBlockDisplay().setGlowing(true);
        });

    }

    @Override
    public void progress() {


        if (abilityStatus == AbilityStatus.SHOT || abilityStatus == AbilityStatus.HIT_SOLID) {

            loc.add(dir.clone().multiply(speed));
            blocks.forEach(block -> {
                block.moveToAndMaintainFacing(block.getLoc().add(dir.clone().multiply(speed)));

            });

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

    public Vector getDir() {
        return dir;
    }

    public void setDir(Vector dir) {
        this.dir = dir;
    }

    @Override
    public void remove() {
        super.remove();
        blocks.forEach(block -> {
            block.revert();
        });
    }

    public Location getLoc() {
        return loc;
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }

    @Override
    public String getName() {
        return user;
    }

}
