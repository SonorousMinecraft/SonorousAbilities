package com.sereneoasis.util.enhancedmethods;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.blocks.RaiseBlockPillar;
import com.sereneoasis.archetypes.DisplayBlock;
import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.methods.Entities;
import com.sereneoasis.util.methods.Scheduler;
import com.sereneoasis.util.methods.Vectors;
import com.sereneoasis.util.temp.TempBlock;
import com.sereneoasis.util.temp.TempDisplayBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EnhancedDisplayBlocks {

    public static void selectSphereDBs(CoreAbility coreAbility, Location loc, long revertTime, Vector moveVector){
        EnhancedBlocks.getFacingSphereBlocks(coreAbility, loc).forEach(block -> {
            if (TempBlock.isTempBlock(block)){
                TempBlock.getTempBlock(block).revert();
                TempDisplayBlock tdb = new TempDisplayBlock(block.getLocation().getBlock(), block.getType(), revertTime, 1);
                tdb.moveToAndMaintainFacing(tdb.getLoc().add(moveVector));

            } else {
                TempDisplayBlock tdb = new TempDisplayBlock(block, block.getType(), revertTime, 1);
                tdb.moveToAndMaintainFacing(tdb.getLoc().add(moveVector));
            }

        });
    }

    public static void selectCylinderDBs(CoreAbility coreAbility, double height, long revertTime, Vector moveVector){
        EnhancedBlocks.getTopCylinderBlocks(coreAbility, height).forEach(block -> {
            if (TempBlock.isTempBlock(block)){
                TempBlock.getTempBlock(block).revert();
                TempDisplayBlock tdb = new TempDisplayBlock(block.getLocation().getBlock(), block.getType(), revertTime, 1);
                tdb.getBlockDisplay().setGlowing(true);
                tdb.moveToAndMaintainFacing(tdb.getLoc().add(moveVector));
            } else {
                TempDisplayBlock tdb = new TempDisplayBlock(block, block.getType(), revertTime, 1);
                tdb.getBlockDisplay().setGlowing(true);
                tdb.moveToAndMaintainFacing(tdb.getLoc().add(moveVector));
            }
        });
    }

    public static void orientOrganisedDBs(HashMap<TempDisplayBlock, Vector> displayBlocks, Vector previousDir, Vector newDir, Player player, double displayBlockDistance){
        Location center = player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(displayBlockDistance));

        for (Map.Entry<TempDisplayBlock, Vector> entry : displayBlocks.entrySet()) {
            double pitchDiff = Vectors.getPitchDiff(previousDir, newDir, player);
            double yawDiff = Vectors.getYawDiff(previousDir, newDir, player);
            entry.getValue().rotateAroundY(-yawDiff);
            entry.getValue().rotateAroundAxis(Vectors.getRightSideNormalisedVector(player), -pitchDiff);
            entry.getKey().moveToAndMaintainFacing(center.clone().add(entry.getValue()));
            Vector facingDir = Vectors.getRightSideNormalisedVector(player);
            entry.getKey().rotate(Vectors.getYaw(newDir, player), Vectors.getPitch(newDir, player));
        }
    }

    public static void orientOrganisedDBsGivenCenter(HashMap<TempDisplayBlock, Vector> displayBlocks, Vector previousDir, Vector newDir, Player player, Location center){
        for (Map.Entry<TempDisplayBlock, Vector> entry : displayBlocks.entrySet()) {
            double pitchDiff = Vectors.getPitchDiff(previousDir, newDir, player);
            double yawDiff = Vectors.getYawDiff(previousDir, newDir, player);
            entry.getValue().rotateAroundY(-yawDiff);
            entry.getValue().rotateAroundAxis(Vectors.getRightSideNormalisedVector(player), -pitchDiff);
            entry.getKey().moveToAndMaintainFacing(center.clone().add(entry.getValue()));
            Vector facingDir = Vectors.getRightSideNormalisedVector(player);
            entry.getKey().rotate(Vectors.getYaw(newDir, player), Vectors.getPitch(newDir, player));
        }
    }

    public static void handleMoveOrganisedDBsAndHit(HashMap<TempDisplayBlock, Vector> displayBlocks, Player player, CoreAbility coreAbility) {
        for (Map.Entry<TempDisplayBlock, Vector> entry : displayBlocks.entrySet()) {
            TempDisplayBlock tempDisplayBlock = entry.getKey();
            Location currentLoc = tempDisplayBlock.getBlockDisplay().getLocation();
            //entry.getValue().add(player.getEyeLocation().getDirection().add(Vector.getRandom().multiply(0.1)).multiply(0.1)).normalize();
            Entity target = Entities.getAffected(currentLoc, coreAbility.getHitbox(), player);
            if (target instanceof LivingEntity livingEntity){
                DamageHandler.damageEntity(livingEntity, player, coreAbility, coreAbility.getDamage());
                coreAbility.remove();
            }
            if (coreAbility.getPlayer().getLocation().distance(currentLoc) > coreAbility.getRange()){
                coreAbility.remove();
            }
            entry.getKey().moveToAndMaintainFacing(currentLoc.add(entry.getValue().clone().multiply(coreAbility.getSpeed())));
        }
    }

    public static Set<TempDisplayBlock> createTopCircleTempBlocks(CoreAbility coreAbility, Material material){
        Set<TempDisplayBlock> tempDisplayBlocks = new HashSet<>();
        EnhancedBlocksArchetypeLess.getTopCircleBlocks(coreAbility).forEach(b -> {
            TempBlock tb = new TempBlock(b, material, coreAbility.getDuration(), true);
            TempDisplayBlock tdb = new TempDisplayBlock(b.getLocation(), material, coreAbility.getDuration(), coreAbility.getSize());
            tempDisplayBlocks.add(tdb);
        });

        return tempDisplayBlocks;
    }

    public static Set<TempDisplayBlock> createTopCircleTempBlocks(CoreAbility coreAbility, DisplayBlock displayBlock){
        Set<TempDisplayBlock> tempDisplayBlocks = new HashSet<>();
        EnhancedBlocksArchetypeLess.getTopCircleBlocks(coreAbility).forEach(b -> {
            TempBlock tb = new TempBlock(b, displayBlock, coreAbility.getDuration(), true);
            TempDisplayBlock tdb = new TempDisplayBlock(b.getLocation(), b.getType(), coreAbility.getDuration(), coreAbility.getSize());
            tempDisplayBlocks.add(tdb);
        });

        return tempDisplayBlocks;
    }

}
