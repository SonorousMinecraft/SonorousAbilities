package com.sereneoasis.archetypes.earth;

import com.sereneoasis.ability.superclasses.CoreAbility;
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
import org.bukkit.util.Vector;

public class RockKick extends CoreAbility {

    private RaiseBlock raiseBlock;

    private ShootBlockFromLoc shootBlockFromLoc;

    private static final String name = "RockKick";


    public RockKick(Player player) {
        super(player, name);

        if (!shouldStart()) {
            return;
        }

        raiseBlock = new RaiseBlock(player, name, 2, true, true);
        if (raiseBlock.getAbilityStatus() == AbilityStatus.SOURCE_SELECTED) {
            abilityStatus = AbilityStatus.SOURCE_SELECTED;
            raiseBlock.getBlock().getBlockDisplay().setGlowColorOverride(Color.GREEN);
            start();
        }
    }



    @Override
    public void progress() throws ReflectiveOperationException {
        raiseBlock.getBlock().rotate(player.getEyeLocation().getYaw(), 0);

        if (abilityStatus == AbilityStatus.SOURCE_SELECTED) {

            if (raiseBlock.getAbilityStatus() == AbilityStatus.SOURCED) {
                abilityStatus = AbilityStatus.SOURCED;
                raiseBlock.fall();
            }
        }
        if (abilityStatus == AbilityStatus.SOURCED){

            if (raiseBlock.getAbilityStatus() == AbilityStatus.COMPLETE){
                raiseBlock.remove();
                this.remove();
            }
        }
        if (abilityStatus == AbilityStatus.SHOT) {
            shootBlockFromLoc.getBlock().rotate(Vectors.getYaw(shootBlockFromLoc.getDir(), player), Vectors.getPitch(shootBlockFromLoc.getDir(), player));
            Vector dir = shootBlockFromLoc.getDir().clone();
            double y = dir.getY();
            y-=Constants.GRAVITY;
            dir.setY(y);
            shootBlockFromLoc.setDir(dir);
            if (shootBlockFromLoc.getAbilityStatus() == AbilityStatus.COMPLETE || shootBlockFromLoc.getAbilityStatus() == AbilityStatus.DAMAGED ) {
                this.remove();
                shootBlockFromLoc.remove();
                sPlayer.addCooldown(name, cooldown);
            } else if (shootBlockFromLoc.getAbilityStatus() == AbilityStatus.HIT_SOLID) {
                Location oldLoc = shootBlockFromLoc.getLoc().clone().subtract(dir);
                BlockFace blockFace = Blocks.getFacingBlockFace(oldLoc, dir, speed + 1);
                if (blockFace != null) {
                    Vector normal = blockFace.getDirection();
                    double dotProduct = normal.dot(dir);
                    Vector newVec = normal.clone().subtract(dir.clone()).multiply(-2 * dotProduct).normalize().multiply(dir.length());
                    shootBlockFromLoc.setDir(newVec);
                    shootBlockFromLoc.setAbilityStatus(AbilityStatus.SHOT);
                }
            }
        } else {
            if (Blocks.playerLookingAtBlockDisplay(player, raiseBlock.getBlockEntity(), sourceRange, size)) {
                raiseBlock.getBlock().getBlockDisplay().setGlowing(true);
            } else {
                raiseBlock.getBlock().getBlockDisplay().setGlowing(false);
            }
        }

    }

    public void setHasClicked() {
        if (abilityStatus != AbilityStatus.SHOT) {
            if (Blocks.playerLookingAtBlockDisplay(player, raiseBlock.getBlockEntity(), sourceRange, size)) {
                shootBlockFromLoc = new ShootBlockFromLoc(player, name, raiseBlock.getBlockEntity().getLocation(), raiseBlock.getBlockEntity().getBlock().getMaterial(), false, false);
                shootBlockFromLoc.getBlock().getBlockDisplay().setGlowColorOverride(Color.GREEN);
                shootBlockFromLoc.getBlock().getBlockDisplay().setGlowing(true);
                raiseBlock.remove();
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
}