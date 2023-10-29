package com.sereneoasis.archetypes.ocean;

import com.sereneoasis.ability.superclasses.CoreAbility;

import com.sereneoasis.abilityuilities.blocks.BlockRingAroundPlayer;
import com.sereneoasis.abilityuilities.blocks.ShootBlockFromLoc;
import com.sereneoasis.abilityuilities.blocks.SourceBlockToPlayer;
import com.sereneoasis.util.AbilityStatus;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author Sakrajin
 *
 */
public class Gimbal extends CoreAbility
{

    private final String name = "Gimbal";

    private SourceBlockToPlayer sourceBlockToPlayer;

    //1 is shot first
    private BlockRingAroundPlayer blockRingAroundPlayer1;

    private BlockRingAroundPlayer blockRingAroundPlayer2;

    private ShootBlockFromLoc shootBlockFromLoc1;

    private ShootBlockFromLoc shootBlockFromLoc2;

    private boolean hasSourced = false;
    private boolean hasShot1 = false;
    private boolean hasShot2 = false;


    public Gimbal(Player player) {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(name))
        {
            return;
        }

        sourceBlockToPlayer = new SourceBlockToPlayer(player, name, Material.BLUE_STAINED_GLASS, 4);
        if (! (sourceBlockToPlayer.getSourceStatus() == AbilityStatus.NO_SOURCE))
        {
            sourceBlockToPlayer.setAbilityStatus(AbilityStatus.SOURCE_SELECTED);
            start();
        }
    }

    public void setHasSourced() {
        sourceBlockToPlayer.setAbilityStatus(AbilityStatus.SOURCING);
    }

    @Override
    public void progress() {

        if (!hasSourced && sourceBlockToPlayer.getSourceStatus() == AbilityStatus.SOURCED)
        {

            hasSourced = true;
            blockRingAroundPlayer1 = new BlockRingAroundPlayer(player, name, sourceBlockToPlayer.getLocation(),
                    Material.BLUE_STAINED_GLASS, radius, 45, 30, true);
            blockRingAroundPlayer2 = new BlockRingAroundPlayer(player, name, sourceBlockToPlayer.getLocation(),
                    Material.BLUE_STAINED_GLASS, radius, -45, 30, false);
            sourceBlockToPlayer.remove();
        }


        if (hasShot1)
        {

            if (shootBlockFromLoc1 !=null && shootBlockFromLoc1.getAbilityStatus() == AbilityStatus.COMPLETE)
            {

                shootBlockFromLoc1.remove();
            }
            if (hasShot2)
            {
                if (shootBlockFromLoc2 !=null && shootBlockFromLoc2.getAbilityStatus() == AbilityStatus.COMPLETE)
                {

                    shootBlockFromLoc2.remove();
                    this.remove();
                }
            }
        }
    }

    public void setHasClicked()
    {
        if (hasSourced) {

            if (!hasShot1) {
                hasShot1 = true;
                shootBlockFromLoc1 = new ShootBlockFromLoc(player, name, blockRingAroundPlayer1.getLocation(), Material.BLUE_STAINED_GLASS, true);
                blockRingAroundPlayer1.remove();
            } else {
                if (!hasShot2) {
                    hasShot2 = true;
                    shootBlockFromLoc2 = new ShootBlockFromLoc(player, name, blockRingAroundPlayer2.getLocation(), Material.BLUE_STAINED_GLASS, true);
                    blockRingAroundPlayer2.remove();
                }
            }
        }
    }

    @Override
    public void remove() {
        super.remove();
        if (blockRingAroundPlayer1 != null)
        {
            blockRingAroundPlayer1.remove();
        }
        if (blockRingAroundPlayer2 != null)
        {
            blockRingAroundPlayer2.remove();
        }
        sPlayer.addCooldown(this.getName(),cooldown);
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
