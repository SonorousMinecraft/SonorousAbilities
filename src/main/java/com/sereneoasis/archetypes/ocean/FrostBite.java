package com.sereneoasis.archetypes.ocean;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.blocks.ShootBlocksFromLoc;
import com.sereneoasis.abilityuilities.blocks.SourceBlockToPlayer;
import com.sereneoasis.archetypes.DisplayBlock;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.methods.Blocks;
import com.sereneoasis.util.methods.Entities;
import com.sereneoasis.util.methods.Vectors;
import com.sereneoasis.util.temp.TempBlock;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class FrostBite extends CoreAbility {

    private boolean hasSetSource2 = false, hasBeganSourcing = false, hasSourced = false, hasShot = false, hasSpawnedShots = false;

    private SourceBlockToPlayer sourceBlockToPlayer1, sourceBlockToPlayer2;

    private Location sourceLoc1, sourceLoc2;

    private ShootBlocksFromLoc shootBlocksFromLoc1, shootBlocksFromLoc2;


    public FrostBite(Player player) {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(this.getName())) {
            return;
        }

        sourceBlockToPlayer1 = new SourceBlockToPlayer(player, "FrostBite", DisplayBlock.WATER, 4);
        if (!(sourceBlockToPlayer1.getSourceStatus() == AbilityStatus.NO_SOURCE)) {
            sourceBlockToPlayer1.setAbilityStatus(AbilityStatus.SOURCE_SELECTED);
            sourceLoc1 = sourceBlockToPlayer1.getLocation().clone();

            start();
        }
    }

    @Override
    public void progress() {

        if (hasSetSource2 & player.isSneaking() & !hasSourced) {
            if (!hasBeganSourcing) {

                sourceBlockToPlayer1.setAbilityStatus(AbilityStatus.SOURCING);
                sourceBlockToPlayer2.setAbilityStatus(AbilityStatus.SOURCING);
                hasBeganSourcing = true;
            } else {
                if (sourceBlockToPlayer1.getSourceStatus() == AbilityStatus.SOURCED && sourceBlockToPlayer2.getSourceStatus() == AbilityStatus.SOURCED) {
                    hasSourced = true;

                    sourceBlockToPlayer1.remove();
                    sourceBlockToPlayer2.remove();
                }
            }
        }

        if (hasShot) {
            if (!hasSpawnedShots) {
                hasSpawnedShots = true;

                shootBlocksFromLoc1 = new ShootBlocksFromLoc(player, "FrostBite", sourceLoc1
                        , DisplayBlock.ICE, false, false);
                shootBlocksFromLoc2 = new ShootBlocksFromLoc(player, "FrostBite", sourceLoc2
                        , DisplayBlock.ICE, false, false);
            } else {
                if (shootBlocksFromLoc1.getAbilityStatus() == AbilityStatus.COMPLETE && shootBlocksFromLoc2.getAbilityStatus() == AbilityStatus.COMPLETE) {
                    this.remove();
                }

                Location loc1 = shootBlocksFromLoc1.getLoc();
                Location loc2 = shootBlocksFromLoc2.getLoc();
                if (loc1.distance(loc2) < 3) {
                    Location explode = loc1.add(Vectors.getDirectionBetweenLocations(loc1, loc2).multiply(0.5));
                    for (Block b : Blocks.getBlocksAroundPoint(explode, radius)) {
                        TempBlock tb = new TempBlock(b, DisplayBlock.ICE, 5000);
                    }
                    DamageHandler.damageEntity(Entities.getAffected(explode, radius, player), player, this, damage);
                    this.remove();
                }
            }
        }
    }

    public void setHasClicked() {
        if (hasSourced) {
            if (!hasShot) {
                hasShot = true;
            } else if (hasSpawnedShots) {
                Location loc1 = shootBlocksFromLoc1.getLoc();
                Location loc2 = shootBlocksFromLoc2.getLoc();
                shootBlocksFromLoc1.setDir(Vectors.getDirectionBetweenLocations(loc1, loc2).normalize());
                shootBlocksFromLoc2.setDir(Vectors.getDirectionBetweenLocations(loc2, loc1).normalize());
            }
        }
    }

    public void setSourceBlock2() {
        if (!hasSetSource2) {
            sourceBlockToPlayer2 = new SourceBlockToPlayer(player, "FrostBite", DisplayBlock.WATER, 4);
            if (!(sourceBlockToPlayer2.getSourceStatus() == AbilityStatus.NO_SOURCE)) {
                hasSetSource2 = true;
                sourceBlockToPlayer2.setAbilityStatus(AbilityStatus.SOURCE_SELECTED);
                sourceLoc2 = sourceBlockToPlayer2.getLocation().clone();
            }

        }

    }

    @Override
    public void remove() {
        super.remove();
        sPlayer.addCooldown("FrostBite", cooldown);

        if (shootBlocksFromLoc1 != null && shootBlocksFromLoc2 != null) {
            shootBlocksFromLoc1.remove();
            shootBlocksFromLoc2.remove();
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
