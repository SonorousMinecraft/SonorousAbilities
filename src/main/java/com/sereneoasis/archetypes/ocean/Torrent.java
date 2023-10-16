package com.sereneoasis.archetypes.ocean;


import com.sereneoasis.ability.superclasses.CoreAbility;

import com.sereneoasis.abilityuilities.blocks.BlockRingAroundPlayer;
import com.sereneoasis.abilityuilities.blocks.ShootBlockFromLoc;
import com.sereneoasis.abilityuilities.blocks.SourceBlockToPlayer;
import com.sereneoasis.util.AbilityStatus;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author Sakrajin
 *
 */
public class Torrent extends CoreAbility {

    private SourceBlockToPlayer sourceBlockToPlayer;

    private BlockRingAroundPlayer blockRingAroundPlayer;

    private ShootBlockFromLoc shootBlockFromLoc;

    private boolean hasSourced = false;

    private boolean hasShot = false;
    private Location loc;

    public Torrent(Player player) {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()))
        {
            return;
        }

        sourceBlockToPlayer = new SourceBlockToPlayer(player, "Torrent", Material.BLUE_STAINED_GLASS, 4);
        if (! (sourceBlockToPlayer.getSourceStatus() == AbilityStatus.NO_SOURCE))
        {
            start();
        }
    }

    @Override
    public void progress() {
        if (!player.isSneaking())
        {
            this.remove();
        }

        if (!hasSourced && sourceBlockToPlayer.getSourceStatus() == AbilityStatus.SOURCED)
        {
            hasSourced = true;
            loc = sourceBlockToPlayer.getLocation();
            sourceBlockToPlayer.remove();
        }

        if (hasSourced) {
            if (hasShot) {
                if (shootBlockFromLoc.getAbilityStatus() == AbilityStatus.COMPLETE)
                {
                    shootBlockFromLoc.remove();
                    sPlayer.addCooldown(this.getName(), this.cooldown);
                    this.remove();
                }
            }
            else{
                if (blockRingAroundPlayer == null)
                {
                    blockRingAroundPlayer = new BlockRingAroundPlayer(player, "Torrent", loc, Material.BLUE_STAINED_GLASS, 2, 0, 10, true);
                }
            }
        }


    }

    public void setHasClicked()
    {
        if (hasSourced)
        {
            hasShot = true;
            shootBlockFromLoc = new ShootBlockFromLoc(player, "Torrent", blockRingAroundPlayer.getLocation(), Material.BLUE_STAINED_GLASS, true);
            blockRingAroundPlayer.remove();
        }
    }

    @Override
    public void remove() {
        super.remove();

        if (hasSourced) {
            blockRingAroundPlayer.remove();
        }
        else {
            sourceBlockToPlayer.remove();
        }
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public String getName() {
        return "Torrent";
    }
}
