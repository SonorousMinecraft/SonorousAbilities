package com.sonorous.abilityuilities.blocks.forcetype;

import com.sonorous.ability.superclasses.CoreAbility;
import com.sonorous.archetypes.DisplayBlock;
import com.sonorous.util.AbilityStatus;
import com.sonorous.util.methods.AbilityDamage;
import com.sonorous.util.methods.Blocks;
import com.sonorous.util.methods.Locations;
import com.sonorous.util.methods.Vectors;
import com.sonorous.util.temp.TempDisplayBlock;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class BlockSweepGivenType extends CoreAbility {

    private final String name;
    protected TempDisplayBlock glowingSource;
    private Location origin, loc1, loc2;
    private Vector dir1, dir2, dir;
    private Set<Location> oldLocs = new HashSet<>();

    private Set<LivingEntity> damagedSet = new HashSet<>();

    private Set<TempDisplayBlock> tempDisplayBlocks = new HashSet<>();

    private DisplayBlock displayBlock;


    public BlockSweepGivenType(Player player, String name, Color color, DisplayBlock displayBlock) {
        super(player, name);

        this.name = name;
        this.displayBlock = displayBlock;
        abilityStatus = AbilityStatus.NO_SOURCE;
        Block source = Blocks.getFacingBlockOrLiquid(player, sourceRange);
        if (source != null && Blocks.getArchetypeBlocks(sPlayer).contains(source.getType())) {
            this.origin = Blocks.getFacingBlockOrLiquidLoc(player, sourceRange).subtract(0, size, 0);

            glowingSource = Blocks.selectSourceAnimationManual(origin, color, size);

            abilityStatus = AbilityStatus.SOURCE_SELECTED;
            start();
        }

    }

    @Override
    public void progress() {

        if (abilityStatus == AbilityStatus.SHOT) {

            Set<Location> newLocs = new HashSet<>(oldLocs);
            oldLocs.forEach(location -> {
                Location newLoc = Locations.getNextLocLiquid(location, dir, speed);
                if (newLoc != null && Blocks.getArchetypeBlocks(sPlayer).contains(newLoc.getBlock().getType())) {

                    newLocs.add(newLoc);
                }
            });

            loc1.add(dir.clone().multiply(speed).add(dir1));
            newLocs.add(loc1);
            loc2.add(dir.clone().multiply(speed).add(dir2));
            newLocs.add(loc2);

            newLocs.stream()
                    .filter(location -> !oldLocs.contains(location))
                    .map(Location::getBlock)
                    .forEach(block -> {
                        TempDisplayBlock tdb = new TempDisplayBlock(block, displayBlock, 1000, 1);
//                            tdb.getBlockDisplay().setGlowing(true);
                        tempDisplayBlocks.add(tdb);
//                            tdb.moveToAndMaintainFacing(tdb.getLoc().add(0, 10, 0));
                        damagedSet.addAll(AbilityDamage.damageSeveralExceptReturnHit(tdb.getLoc(), this, player, damagedSet, true, player.getEyeLocation().getDirection()));

//                        DamageHandler.damageEntity(Entities.getAffected(tdb.getLoc(), hitbox, player), player, this, damage);
                    });


            oldLocs = newLocs;

//            Locations.getArc(loc1, loc2, origin.clone(), 0.5).stream()
//                    .map(Location::getBlock)
//                    .forEach(block -> {
//                            TempDisplayBlock tdb = new TempDisplayBlock(block, block.getType(), 2000, 1);
//                            tdb.getBlockDisplay().setGlowing(true);
//                            tdb.moveToAndMaintainFacing(tdb.getLoc().add(0, 1, 0));
//                            DamageHandler.damageEntity(Entities.getAffected(tdb.getLoc(), hitbox, player), player, this, damage);
//                    });


            if (origin.distance(Locations.getMidpoint(loc1, loc2)) > range) {
                abilityStatus = AbilityStatus.COMPLETE;
            }
        }
    }

    public Set<TempDisplayBlock> getTempDisplayBlocks() {
        return this.tempDisplayBlocks;
    }


    public void setHasClicked() {
        if (abilityStatus == AbilityStatus.SOURCE_SELECTED) {
            abilityStatus = AbilityStatus.SHOT;

//            this.loc1 = origin.clone().add(Vectors.getLeftSide(player, radius/2));
            this.loc1 = origin.clone();
//            this.loc2 = origin.clone().add(Vectors.getRightSide(player, radius/2));
            this.loc2 = origin.clone();

            this.dir1 = Vectors.getLeftSide(player, 1);
            this.dir2 = Vectors.getRightSide(player, 1);

            this.dir = player.getEyeLocation().getDirection().setY(0).normalize();

            oldLocs.add(origin.clone().add(0, size, 0));
            glowingSource.revert();
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
