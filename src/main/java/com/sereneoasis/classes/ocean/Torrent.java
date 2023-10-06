package com.sereneoasis.classes.ocean;

import com.sereneoasis.Methods;
import com.sereneoasis.ability.CoreAbility;
import com.sereneoasis.abilityuilities.BlockRingAroundPlayer;
import com.sereneoasis.abilityuilities.ShootBlockFromPlayer;
import com.sereneoasis.abilityuilities.SourceBlockToPlayer;
import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.SourceStatus;
import com.sereneoasis.util.TempBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Torrent extends CoreAbility {

    private SourceBlockToPlayer sourceBlockToPlayer;

    private BlockRingAroundPlayer blockRingAroundPlayer;

    private ShootBlockFromPlayer shootBlockFromPlayer;

    private boolean hasSourced = false;

    private boolean hasShot = false;
    private Location loc;

    public Torrent(Player player) {
        super(player);

        sourceBlockToPlayer = new SourceBlockToPlayer(player, this, Material.WATER, 4);
        if (! (sourceBlockToPlayer.getSourceStatus() == SourceStatus.NO_SOURCE))
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

        if (sourceBlockToPlayer.getSourceStatus() == SourceStatus.SOURCED)
        {
            hasSourced = true;
            sourceBlockToPlayer.remove();
        }

        if (hasSourced) {
            if (hasShot) {
                loc = blockRingAroundPlayer.getLoc();
                blockRingAroundPlayer.remove();

                if (shootBlockFromPlayer == null)
                {
                    this.remove();
                }

            }
            else{
                if (blockRingAroundPlayer == null)
                {
                    blockRingAroundPlayer = new BlockRingAroundPlayer(player, this, loc, Material.WATER, 3, 0);
                }
            }
        }


    }

    public void setHasClicked()
    {
        if (hasSourced)
        {
            hasShot = true;
            shootBlockFromPlayer = new ShootBlockFromPlayer(player, this, loc, Material.WATER, true);
        }
    }

    @Override
    public void remove() {
        super.remove();
        blockRingAroundPlayer.remove();
        sourceBlockToPlayer.remove();
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
