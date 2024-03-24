package com.sereneoasis.abilityuilities.blocks;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.ability.superclasses.MasterAbility;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.Blocks;
import com.sereneoasis.util.methods.Constants;
import com.sereneoasis.util.methods.Vectors;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

public class BlockAbilities {

    public static void handleRaiseBlock(MasterAbility masterAbility, RaiseBlock raiseBlock){
        masterAbility.getHelpers().put(raiseBlock, status -> {
            switch (status){
                case SOURCE_SELECTED -> {
                    if (raiseBlock.getAbilityStatus() == AbilityStatus.SOURCED) {
                        masterAbility.setAbilityStatus(AbilityStatus.SOURCED);
                        raiseBlock.fall();
                    }
                } case SOURCED -> {
                    if (raiseBlock.getAbilityStatus() == AbilityStatus.COMPLETE){
                        raiseBlock.remove();
                    }
                } case SHOT -> {
                    raiseBlock.remove();
                }
            }
        });
    }

    public static void handleBouncingShootBlockFromLoc(MasterAbility masterAbility, ShootBlockFromLoc shootBlockFromLoc)
    {
        masterAbility.getHelpers().put(shootBlockFromLoc, status -> {
            switch (status){
                case SHOT -> {
                    shootBlockFromLoc.getBlock().rotate(Vectors.getYaw(shootBlockFromLoc.getDir(), masterAbility.getPlayer()), Vectors.getPitch(shootBlockFromLoc.getDir(), masterAbility.getPlayer()));
                    Vector dir = shootBlockFromLoc.getDir().clone();
                    double y = dir.getY();
                    y -= Constants.GRAVITY;
                    dir.setY(y);
                    shootBlockFromLoc.setDir(dir);
                    if (shootBlockFromLoc.getAbilityStatus() == AbilityStatus.COMPLETE || shootBlockFromLoc.getAbilityStatus() == AbilityStatus.DAMAGED) {
                        masterAbility.remove();
                        shootBlockFromLoc.remove();
                        masterAbility.getsPlayer().addCooldown(masterAbility.getName(), masterAbility.getCooldown());
                    } else if (shootBlockFromLoc.getAbilityStatus() == AbilityStatus.HIT_SOLID) {
                        Location oldLoc = shootBlockFromLoc.getLoc().clone().subtract(dir);
                        BlockFace blockFace = Blocks.getFacingBlockFace(oldLoc, dir, masterAbility.getSpeed() + 1);
                        if (blockFace != null) {
                            Vector normal = blockFace.getDirection();
                            Vector newVec = Vectors.getBounce(dir, normal);
                            shootBlockFromLoc.setDir(newVec);
                            shootBlockFromLoc.setAbilityStatus(AbilityStatus.SHOT);
                        }
                    }
                }
            }
        });
    }

    public static void handleShootBlockFromLoc(MasterAbility masterAbility, ShootBlockFromLoc shootBlockFromLoc)
    {
        masterAbility.getHelpers().put(shootBlockFromLoc, status -> {
            switch (status){
                case SHOT -> {
                    shootBlockFromLoc.getBlock().rotate(Vectors.getYaw(shootBlockFromLoc.getDir(), masterAbility.getPlayer()), Vectors.getPitch(shootBlockFromLoc.getDir(), masterAbility.getPlayer()));
                    if (shootBlockFromLoc.getAbilityStatus() == AbilityStatus.COMPLETE || shootBlockFromLoc.getAbilityStatus() == AbilityStatus.DAMAGED || shootBlockFromLoc.getAbilityStatus() == AbilityStatus.HIT_SOLID) {
                        masterAbility.remove();
                        shootBlockFromLoc.remove();
                        masterAbility.getsPlayer().addCooldown(masterAbility.getName(), masterAbility.getCooldown());
                    }
                }
            }
        });
    }
}
