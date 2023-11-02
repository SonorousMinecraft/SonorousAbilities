package com.sereneoasis.archetypes.ocean;


import com.sereneoasis.ability.superclasses.CoreAbility;

import com.sereneoasis.abilityuilities.blocks.BlockRingAroundPlayer;
import com.sereneoasis.abilityuilities.blocks.ShootBlockFromLoc;
import com.sereneoasis.abilityuilities.blocks.SourceBlockToPlayer;
import com.sereneoasis.archetypes.DisplayBlock;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.Blocks;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author Sakrajin
 *
 */
public class Torrent extends CoreAbility {

    private final String name = "Torrent";

    private AbilityStatus abilityStatus;

    private SourceBlockToPlayer sourceBlockToPlayer;

    private BlockRingAroundPlayer blockRingAroundPlayer;

    private ShootBlockFromLoc shootBlockFromLoc;



    public Torrent(Player player) {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(name))
        {
            return;
        }

        sourceBlockToPlayer = new SourceBlockToPlayer(player, name, DisplayBlock.WATER, 4);
        if (! (sourceBlockToPlayer.getSourceStatus() == AbilityStatus.NO_SOURCE))
        {
            abilityStatus = AbilityStatus.SOURCE_SELECTED;
            sourceBlockToPlayer.setAbilityStatus(AbilityStatus.SOURCE_SELECTED);
            start();
        }
    }

    public void setHasSourced()
    {
        sourceBlockToPlayer.setAbilityStatus(AbilityStatus.SOURCING);
        abilityStatus = AbilityStatus.SOURCING;
    }


    @Override
    public void progress() {

            if (abilityStatus == AbilityStatus.SOURCING)
            {
                if (!player.isSneaking())
                {
                    sourceBlockToPlayer.remove();
                    this.remove();
                }

                if (sourceBlockToPlayer.getSourceStatus() == AbilityStatus.SOURCED) {
                    abilityStatus = AbilityStatus.SOURCED;
                    blockRingAroundPlayer = new BlockRingAroundPlayer(player, name, sourceBlockToPlayer.getLocation(), DisplayBlock.WATER,
                            radius, 0, 30, true);
                    sourceBlockToPlayer.remove();
                }
            }


        if (abilityStatus == AbilityStatus.SHOT) {
                if (shootBlockFromLoc.getAbilityStatus() == AbilityStatus.COMPLETE)
                {
                    shootBlockFromLoc.remove();

                    this.remove();
                }
            }

        }


    public void setHasClicked()
    {
        if (abilityStatus == AbilityStatus.SOURCED)
        {
            abilityStatus = AbilityStatus.SHOT;
            shootBlockFromLoc = new ShootBlockFromLoc(player, name, blockRingAroundPlayer.getLocation(), DisplayBlock.WATER, true, true);
            blockRingAroundPlayer.remove();
        }
    }

    @Override
    public void remove() {
        super.remove();
        sPlayer.addCooldown(this.getName(), this.cooldown);
        if (sourceBlockToPlayer != null)
        {
            sourceBlockToPlayer.remove();
        }
        if (blockRingAroundPlayer != null)
        {
            blockRingAroundPlayer.remove();
        }
        if (shootBlockFromLoc != null)
        {
            shootBlockFromLoc.remove();
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
