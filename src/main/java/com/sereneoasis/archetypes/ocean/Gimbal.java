package com.sereneoasis.archetypes.ocean;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.blocks.BlockRingAroundPlayer;
import com.sereneoasis.abilityuilities.blocks.ShootBlocksFromLoc;
import com.sereneoasis.abilityuilities.blocks.SourceBlockToPlayer;
import com.sereneoasis.archetypes.DisplayBlock;
import com.sereneoasis.util.AbilityStatus;
import org.bukkit.entity.Player;

/**
 * @author Sakrajin
 */
public class Gimbal extends CoreAbility {

    private final String name = "Gimbal";

    private SourceBlockToPlayer sourceBlockToPlayer;

    //1 is shot first
    private BlockRingAroundPlayer blockRingAroundPlayer1;

    private BlockRingAroundPlayer blockRingAroundPlayer2;

    private ShootBlocksFromLoc shootBlocksFromLoc1;

    private ShootBlocksFromLoc shootBlocksFromLoc2;

    private boolean hasSourced = false;
    private boolean hasShot1 = false;
    private boolean hasShot2 = false;


    public Gimbal(Player player) {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(name)) {
            return;
        }

        sourceBlockToPlayer = new SourceBlockToPlayer(player, name, DisplayBlock.WATER, 4);
        if (!(sourceBlockToPlayer.getSourceStatus() == AbilityStatus.NO_SOURCE)) {
            sourceBlockToPlayer.setAbilityStatus(AbilityStatus.SOURCE_SELECTED);
            start();
        }
    }

    public void setHasSourced() {
        sourceBlockToPlayer.setAbilityStatus(AbilityStatus.SOURCING);
    }

    @Override
    public void progress() {

        if (!hasSourced && sourceBlockToPlayer.getSourceStatus() == AbilityStatus.SOURCED) {

            hasSourced = true;
            blockRingAroundPlayer1 = new BlockRingAroundPlayer(player, name, sourceBlockToPlayer.getLocation(),
                    DisplayBlock.WATER, radius, 45, 30, true);
            blockRingAroundPlayer2 = new BlockRingAroundPlayer(player, name, sourceBlockToPlayer.getLocation(),
                    DisplayBlock.WATER, radius, -45, 30, false);
            sourceBlockToPlayer.remove();
        }


        if (hasShot1) {

            if (shootBlocksFromLoc1 != null && shootBlocksFromLoc1.getAbilityStatus() == AbilityStatus.COMPLETE) {

                shootBlocksFromLoc1.remove();
            }
            if (hasShot2) {
                if (shootBlocksFromLoc2 != null && shootBlocksFromLoc2.getAbilityStatus() == AbilityStatus.COMPLETE) {

                    shootBlocksFromLoc2.remove();
                    this.remove();
                }
            }
        }
    }

    public void setHasClicked() {
        if (hasSourced) {

            if (!hasShot1) {
                hasShot1 = true;
                shootBlocksFromLoc1 = new ShootBlocksFromLoc(player, name, blockRingAroundPlayer1.getLocation(), DisplayBlock.WATER, true, false);
                blockRingAroundPlayer1.remove();
            } else {
                if (!hasShot2) {
                    hasShot2 = true;
                    shootBlocksFromLoc2 = new ShootBlocksFromLoc(player, name, blockRingAroundPlayer2.getLocation(), DisplayBlock.WATER, true, false);
                    blockRingAroundPlayer2.remove();
                }
            }
        }
    }

    @Override
    public void remove() {
        super.remove();
        if (blockRingAroundPlayer1 != null) {
            blockRingAroundPlayer1.remove();
        }
        if (blockRingAroundPlayer2 != null) {
            blockRingAroundPlayer2.remove();
        }
        sPlayer.addCooldown(this.getName(), cooldown);
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
