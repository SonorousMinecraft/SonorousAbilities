package com.sereneoasis.archetypes.earth;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.blocks.RaiseBlock;
import com.sereneoasis.abilityuilities.blocks.ShootBlockFromLoc;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.BlockDisplay;
import com.sereneoasis.util.methods.Blocks;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class RockKick extends CoreAbility {

    private RaiseBlock raiseBlock;

    private ShootBlockFromLoc shootBlockFromLoc;

    private final String name = "RockKick";

    private float previousYaw;

    public RockKick(Player player) {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(this.getName())) {
            return;
        }

        raiseBlock = new RaiseBlock(player, name, 2-size, true);
        if (raiseBlock.getAbilityStatus() == AbilityStatus.SOURCE_SELECTED) {
            abilityStatus = AbilityStatus.SOURCE_SELECTED;
            start();
        }
    }

    private Vector offsetAdjustment = new Vector(-size/2, 0, -size/2);


    @Override
    public void progress() throws ReflectiveOperationException {

        if (abilityStatus == AbilityStatus.SOURCE_SELECTED) {
            if (raiseBlock.getAbilityStatus() == AbilityStatus.SOURCED) {
                abilityStatus = AbilityStatus.SOURCED;
                this.previousYaw = player.getEyeLocation().getYaw();
            }

        }
        if (abilityStatus == AbilityStatus.SHOT) {
            if (shootBlockFromLoc.getAbilityStatus() == AbilityStatus.COMPLETE) {
                this.remove();
                shootBlockFromLoc.remove();
                sPlayer.addCooldown(name, cooldown);
            }
        }
        if (abilityStatus == AbilityStatus.SOURCED){
            float yawDiff = previousYaw - player.getEyeLocation().getYaw() ;
            BlockDisplay.rotateBlockDisplayProperlyDegs( raiseBlock.getBlock().getBlockDisplay(), size, 0 , yawDiff, 0);
            previousYaw = player.getEyeLocation().getYaw();
            raiseBlock.getBlock().moveTo(player.getEyeLocation().add(player.getEyeLocation().getDirection().clone().multiply(size/2)).add(offsetAdjustment));
            if (! sPlayer.getHeldAbility().equals(name)) {
                this.remove();
            }
        }
    }

    public void setHasClicked() {
        if (abilityStatus == AbilityStatus.SOURCED) {
            if (Blocks.playerLookingAtBlockDisplay(player, raiseBlock.getBlockEntity(), sourceRange, size)) {
                shootBlockFromLoc = new ShootBlockFromLoc(player, name, raiseBlock.getBlockEntity().getLocation(), raiseBlock.getBlockEntity().getBlock().getMaterial(), true, false);
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
