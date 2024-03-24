package com.sereneoasis.archetypes.earth;

import com.sereneoasis.ability.superclasses.CollisionAbility;
import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.ability.superclasses.MasterAbility;
import com.sereneoasis.ability.superclasses.RedirectAbility;
import com.sereneoasis.abilityuilities.blocks.RaiseBlock;
import com.sereneoasis.abilityuilities.blocks.ShootBlockFromLoc;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.BlockDisplay;
import com.sereneoasis.util.methods.Blocks;
import com.sereneoasis.util.methods.Constants;
import com.sereneoasis.util.methods.Vectors;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class RockKick extends MasterAbility implements RedirectAbility {

    private RaiseBlock raiseBlock;

    private ShootBlockFromLoc shootBlockFromLoc;


    private static final String name = "RockKick";

    private Location loc;


    public RockKick(Player player) {
        super(player, name);

        if (!shouldStart()) {
            return;
        }

        raiseBlock = new RaiseBlock(player, name, 2, true, true);

        if (raiseBlock.getAbilityStatus() == AbilityStatus.SOURCE_SELECTED) {
            helpers.put(raiseBlock, status -> {
                switch (status){
                    case SOURCE_SELECTED -> {
                        if (raiseBlock.getAbilityStatus() == AbilityStatus.SOURCED) {
                            abilityStatus = AbilityStatus.SOURCED;
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
            abilityStatus = AbilityStatus.SOURCE_SELECTED;
            raiseBlock.getBlock().getBlockDisplay().setGlowColorOverride(Color.GREEN);
            this.loc = raiseBlock.getBlock().getLoc();
            this.setRedirectable();
            start();
        }
    }



    @Override
    public void progress() throws ReflectiveOperationException {


        raiseBlock.getBlock().rotate(player.getEyeLocation().getYaw(), 0);

        iterateHelpers(abilityStatus);

    }

    public void setHasClicked() {
        if (abilityStatus != AbilityStatus.SHOT) {
            if (Blocks.playerLookingAtBlockDisplay(player, raiseBlock.getBlockEntity(), sourceRange, size)) {
                shootBlockFromLoc = new ShootBlockFromLoc(player, name, raiseBlock.getBlockEntity().getLocation(), raiseBlock.getBlockEntity().getBlock().getMaterial(), false, false);
                shootBlockFromLoc.getBlock().getBlockDisplay().setGlowColorOverride(Color.GREEN);
                shootBlockFromLoc.getBlock().getBlockDisplay().setGlowing(true);
                helpers.put(shootBlockFromLoc, status -> {
                    switch (status){
                        case SHOT -> {
                            this.loc = shootBlockFromLoc.getBlock().getLoc();
                            shootBlockFromLoc.getBlock().rotate(Vectors.getYaw(shootBlockFromLoc.getDir(), player), Vectors.getPitch(shootBlockFromLoc.getDir(), player));
                            Vector dir = shootBlockFromLoc.getDir().clone();
                            double y = dir.getY();
                            y -= Constants.GRAVITY;
                            dir.setY(y);
                            shootBlockFromLoc.setDir(dir);
                            if (shootBlockFromLoc.getAbilityStatus() == AbilityStatus.COMPLETE || shootBlockFromLoc.getAbilityStatus() == AbilityStatus.DAMAGED) {
                                this.remove();
                                shootBlockFromLoc.remove();
                                sPlayer.addCooldown(name, cooldown);
                            } else if (shootBlockFromLoc.getAbilityStatus() == AbilityStatus.HIT_SOLID) {
                                Location oldLoc = shootBlockFromLoc.getLoc().clone().subtract(dir);
                                BlockFace blockFace = Blocks.getFacingBlockFace(oldLoc, dir, speed + 1);
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

                abilityStatus = AbilityStatus.SHOT;
            }
        }
    }


    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public String getName() {
        return name;
    }


    @Override
    public boolean hasCustomRedirect() {
        return false;
    }

    @Override
    public HashMap<Location, Double> getLocs() {
        HashMap<Location, Double> locs = new HashMap<>();
        locs.put(loc, size/2);
        return locs;
    }

    @Override
    public void handleRedirects(Player redirectingPlayer, ClickType clickType) {

    }

    @Override
    public Vector getDir() {
        return null;
    }

    @Override
    public void setDir(Vector dir) {

        if (shootBlockFromLoc != null) {
            if (shootBlockFromLoc.isDirectable()){
                shootBlockFromLoc.setDirectable(false);
            }
            shootBlockFromLoc.setDir(dir.clone().multiply(speed));
        }
    }
}