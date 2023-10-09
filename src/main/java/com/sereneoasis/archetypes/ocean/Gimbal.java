package com.sereneoasis.archetypes.ocean;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.blocks.BlockRingAroundPlayer;
import com.sereneoasis.abilityuilities.blocks.ShootBlockFromPlayer;
import com.sereneoasis.abilityuilities.blocks.SourceBlockToPlayer;
import com.sereneoasis.util.SourceStatus;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Gimbal extends CoreAbility
{

    private SourceBlockToPlayer sourceBlockToPlayer;

    //1 is shot first
    private BlockRingAroundPlayer blockRingAroundPlayer1;

    private BlockRingAroundPlayer blockRingAroundPlayer2;

    private ShootBlockFromPlayer shootBlockFromPlayer1;

    private ShootBlockFromPlayer shootBlockFromPlayer2;


    private boolean hasShot1 = false;
    private boolean hasShot2 = false;

    private Location loc;

    public Gimbal(Player player) {
        super(player);

        sourceBlockToPlayer = new SourceBlockToPlayer(player, this, Material.WATER, 4);
        if (! (sourceBlockToPlayer.getSourceStatus() == SourceStatus.NO_SOURCE))
        {
            blockRingAroundPlayer1 = new BlockRingAroundPlayer(player, this, player.getEyeLocation(),
                    Material.WATER, 3, 45);
            blockRingAroundPlayer2 = new BlockRingAroundPlayer(player, this, player.getEyeLocation(),
                    Material.WATER, 3, -45);
            start();

        }
    }

    @Override
    public void progress() {
        if (blockRingAroundPlayer2 == null)
        {
            this.remove();
        }
    }

    public void setHasClicked()
    {
        if (!hasShot1)
        {
            hasShot1 = true;
            shootBlockFromPlayer1 = new ShootBlockFromPlayer(player, this, loc, Material.WATER, true);
            blockRingAroundPlayer1.remove();
        }
        else{
            if (!hasShot2)
            {
                hasShot2 = true;
                shootBlockFromPlayer2 = new ShootBlockFromPlayer(player, this, loc, Material.WATER, true);
                blockRingAroundPlayer2.remove();
            }
        }
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public String getName() {
        return "Gimbal";
    }
}
