package com.sereneoasis.archetypes.ocean;

import com.sereneoasis.ability.superclasses.CoreAbility;

import com.sereneoasis.abilityuilities.blocks.BlockRingAroundPlayer;
import com.sereneoasis.abilityuilities.blocks.ShootBlockFromPlayer;
import com.sereneoasis.abilityuilities.blocks.SourceBlockToPlayer;
import com.sereneoasis.util.AbilityStatus;
import org.bukkit.Bukkit;
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

    private boolean hasSourced = false;
    private boolean hasShot1 = false;
    private boolean hasShot2 = false;


    public Gimbal(Player player) {
        super(player);

        sourceBlockToPlayer = new SourceBlockToPlayer(player, this.getName(), Material.WATER, 4);
        if (! (sourceBlockToPlayer.getSourceStatus() == AbilityStatus.NO_SOURCE))
        {
            start();

        }
    }

    @Override
    public void progress() {

        if (!hasSourced && sourceBlockToPlayer.getSourceStatus() == AbilityStatus.SOURCED)
        {
            hasSourced = true;
            blockRingAroundPlayer1 = new BlockRingAroundPlayer(player, "Gimbal", sourceBlockToPlayer.getLocation(),
                    Material.WATER, 2, 30);
            blockRingAroundPlayer2 = new BlockRingAroundPlayer(player, "Gimbal", sourceBlockToPlayer.getLocation(),
                    Material.WATER, 2, -30);
            sourceBlockToPlayer.remove();
        }


        if (hasShot1)
        {
            if (shootBlockFromPlayer1 !=null && shootBlockFromPlayer1.getAbilityStatus() == AbilityStatus.COMPLETE)
            {
                shootBlockFromPlayer1.remove();
            }
            if (hasShot2)
            {
                if (shootBlockFromPlayer2 !=null && shootBlockFromPlayer2.getAbilityStatus() == AbilityStatus.COMPLETE)
                {
                    shootBlockFromPlayer2.remove();
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
                shootBlockFromPlayer1 = new ShootBlockFromPlayer(player, "Gimbal", blockRingAroundPlayer1.getLocation(), Material.WATER, true);
                blockRingAroundPlayer1.remove();
            } else {
                if (!hasShot2) {
                    hasShot2 = true;
                    shootBlockFromPlayer2 = new ShootBlockFromPlayer(player, "Gimbal", blockRingAroundPlayer2.getLocation(), Material.WATER, true);
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
        return "Gimbal";
    }
}
