package com.sereneoasis.archetypes.earth;

import com.sereneoasis.ability.superclasses.CollisionAbility;
import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.ability.superclasses.MasterAbility;
import com.sereneoasis.ability.superclasses.RedirectAbility;
import com.sereneoasis.abilityuilities.blocks.BlockAbilities;
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
import org.bukkit.block.Block;
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

            BlockAbilities.handleRaiseBlock(this, raiseBlock);

            abilityStatus = AbilityStatus.SOURCE_SELECTED;
            this.loc = raiseBlock.getBlock().getLoc();
            this.setRedirectable();
            start();
        }
    }

    @Override
    public void progress() throws ReflectiveOperationException {


        raiseBlock.getBlock().rotate(player.getEyeLocation().getYaw(), 0);

        iterateHelpers(abilityStatus);
        if (abilityStatus == AbilityStatus.SHOT) {
            this.loc = shootBlockFromLoc.getBlock().getLoc();
        }

        if (abilityStatus != AbilityStatus.SHOT && raiseBlock.getAbilityStatus() == AbilityStatus.COMPLETE) {
            this.remove();
        }


    }

    public void setHasClicked() {
        if (abilityStatus != AbilityStatus.SHOT) {
            if (Blocks.playerLookingAtBlockDisplay(player, raiseBlock.getBlockEntity(), sourceRange, size)) {
                shootBlockFromLoc = new ShootBlockFromLoc(player, name, raiseBlock.getBlockEntity().getLocation(), raiseBlock.getBlockEntity().getBlock().getMaterial(), false, false);
                BlockAbilities.handleShootBlockFromLoc(this, shootBlockFromLoc);

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