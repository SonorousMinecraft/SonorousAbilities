package com.sonorous.archetypes.earth;

import com.sonorous.ability.superclasses.MasterAbility;
import com.sonorous.ability.superclasses.RedirectAbility;
import com.sonorous.abilityuilities.blocks.BlockAbilities;
import com.sonorous.abilityuilities.blocks.RaiseBlock;
import com.sonorous.abilityuilities.blocks.ShootBlockFromLoc;
import com.sonorous.util.AbilityStatus;
import com.sonorous.util.methods.Blocks;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class RockKick extends MasterAbility implements RedirectAbility {

    private static final String name = "RockKick";
    private RaiseBlock raiseBlock;
    private ShootBlockFromLoc shootBlockFromLoc;
    private Location loc;

    private boolean isLooking = false;

    private long sinceLastLooked = System.currentTimeMillis();

    public RockKick(Player player) {
        super(player, name);

        if (!shouldStartCanHaveMultiple()) {
            return;
        }

        raiseBlock = new RaiseBlock(player, name, 2, true, true);

        if (raiseBlock.getAbilityStatus() == AbilityStatus.SOURCE_SELECTED) {

            BlockAbilities.handleRaiseBlock(this, raiseBlock);

            abilityStatus = AbilityStatus.SOURCE_SELECTED;
            this.loc = raiseBlock.getBlock().getLoc();
            this.setRedirectable();
            sPlayer.addCooldown(name, cooldown);
            start();
        }
    }

    @Override
    public void progress() throws ReflectiveOperationException {


        raiseBlock.getBlock().rotate(player.getEyeLocation().getYaw(), 0);

        iterateHelpers(abilityStatus);
        if (abilityStatus != AbilityStatus.SHOT) {
            if (Blocks.playerLookingAtBlockDisplay(player, raiseBlock.getBlockEntity(), sourceRange, size)) {
                raiseBlock.getBlockEntity().setGlowColorOverride(Color.RED);
                raiseBlock.getBlockEntity().setGlowing(true);
                isLooking = true;
                sinceLastLooked = System.currentTimeMillis();
            } else if (System.currentTimeMillis() - sinceLastLooked > 200) {
                raiseBlock.getBlockEntity().setGlowing(false);
                isLooking = false;
            }
        }

        if (abilityStatus == AbilityStatus.SHOT) {
            this.loc = shootBlockFromLoc.getBlock().getLoc();
        }

        if (abilityStatus != AbilityStatus.SHOT && raiseBlock.getAbilityStatus() == AbilityStatus.COMPLETE) {
            this.remove();
        }


    }

    public void setHasClicked() {
        if (abilityStatus != AbilityStatus.SHOT && isLooking) {
            shootBlockFromLoc = new ShootBlockFromLoc(player, name, raiseBlock.getBlockEntity().getLocation(), raiseBlock.getBlockEntity().getBlock().getMaterial(), false, false);
            BlockAbilities.handleBouncingShootBlockFromLoc(this, shootBlockFromLoc);

            abilityStatus = AbilityStatus.SHOT;

        }
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
        locs.put(loc, size / 2);
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
            if (shootBlockFromLoc.isDirectable()) {
                shootBlockFromLoc.setDirectable(false);
            }
            shootBlockFromLoc.setDir(dir.clone().multiply(speed));
        }
    }
}