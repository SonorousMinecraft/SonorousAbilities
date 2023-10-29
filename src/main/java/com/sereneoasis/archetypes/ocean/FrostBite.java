package com.sereneoasis.archetypes.ocean;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.blocks.ShootBlockFromLoc;
import com.sereneoasis.abilityuilities.blocks.SourceBlockToPlayer;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.methods.*;
import com.sereneoasis.util.temp.TempBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class FrostBite extends CoreAbility {

    private boolean hasSetSource2 = false, hasBeganSourcing = false, hasSourced = false, hasShot = false, hasSpawnedShots = false;

    private SourceBlockToPlayer sourceBlockToPlayer1, sourceBlockToPlayer2;
    
    private Location sourceLoc1, sourceLoc2;

    private ShootBlockFromLoc shootBlockFromLoc1, shootBlockFromLoc2;



    public FrostBite(Player player) {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()))
        {
            return;
        }

        sourceBlockToPlayer1 = new SourceBlockToPlayer(player, "FrostBite", Material.BLUE_STAINED_GLASS, 4);
        if (! (sourceBlockToPlayer1.getSourceStatus() == AbilityStatus.NO_SOURCE))
        {
            sourceBlockToPlayer1.setAbilityStatus(AbilityStatus.SOURCE_SELECTED);
            sourceLoc1 = sourceBlockToPlayer1.getLocation().clone();

            start();
        }
    }

    @Override
    public void progress() {

        if (hasSetSource2 & player.isSneaking() & !hasSourced)
        {
            if (!hasBeganSourcing) {

                sourceBlockToPlayer1.setAbilityStatus(AbilityStatus.SOURCING);
                sourceBlockToPlayer2.setAbilityStatus(AbilityStatus.SOURCING);
                hasBeganSourcing = true;
            }
            else {
                if (sourceBlockToPlayer1.getSourceStatus() == AbilityStatus.SOURCED && sourceBlockToPlayer2.getSourceStatus() == AbilityStatus.SOURCED )
                {
                    hasSourced = true;

                    sourceBlockToPlayer1.remove();
                    sourceBlockToPlayer2.remove();
                }
            }
        }

        if (hasShot) {
            if (!hasSpawnedShots) {
                hasSpawnedShots = true;

                shootBlockFromLoc1 = new ShootBlockFromLoc(player, "FrostBite", sourceLoc1
                        , Material.ICE, false);
                shootBlockFromLoc2 = new ShootBlockFromLoc(player, "FrostBite", sourceLoc2
                        , Material.ICE, false);
            } else {
                if (shootBlockFromLoc1.getAbilityStatus() == AbilityStatus.COMPLETE && shootBlockFromLoc2.getAbilityStatus() == AbilityStatus.COMPLETE)
                {
                    this.remove();
                }

                Location loc1 = shootBlockFromLoc1.getLoc();
                Location loc2 = shootBlockFromLoc2.getLoc();
                if (loc1.distance(loc2) < 5) {
                    Location explode = loc1.add(Vectors.getDirectionBetweenLocations(loc1, loc2).multiply(0.5));
                    for (Block b : Blocks.getBlocksAroundPoint(explode, radius)) {
                        TempBlock tb = new TempBlock(b, Material.ICE.createBlockData(), 5000);
                    }
                    DamageHandler.damageEntity(Entities.getAffected(explode, radius, player), player, this, damage);
                    this.remove();
                }
            }
        }
    }

    public void setHasClicked()
    {
        if (hasSourced)
        {
            if (!hasShot) {
                hasShot = true;
            }
            else{
                Location loc1 = shootBlockFromLoc1.getLoc();
                Location loc2 = shootBlockFromLoc2.getLoc();
                shootBlockFromLoc1.setDir(Vectors.getDirectionBetweenLocations(loc1, loc2).normalize());
                shootBlockFromLoc2.setDir(Vectors.getDirectionBetweenLocations(loc2, loc1).normalize());
            }
        }
    }

    public void setSourceBlock2()
    {
        if (!hasSetSource2)
        {
            sourceBlockToPlayer2 = new SourceBlockToPlayer(player, "FrostBite", Material.BLUE_STAINED_GLASS, 4);
            if (! (sourceBlockToPlayer2.getSourceStatus() == AbilityStatus.NO_SOURCE))
            {
                hasSetSource2 = true;
                sourceBlockToPlayer2.setAbilityStatus(AbilityStatus.SOURCE_SELECTED);
                sourceLoc2 = sourceBlockToPlayer2.getLocation().clone();
            }

        }

    }

    @Override
    public void remove()
    {
        super.remove();
        sPlayer.addCooldown("FrostBite", cooldown);

        if (shootBlockFromLoc1 != null && shootBlockFromLoc2 != null)
        {
            shootBlockFromLoc1.remove();
            shootBlockFromLoc2.remove();
        }

    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public String getName() {
        return "FrostBite";
    }
}
