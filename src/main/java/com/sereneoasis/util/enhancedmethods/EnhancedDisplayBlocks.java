package com.sereneoasis.util.enhancedmethods;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.util.DamageHandler;
import com.sereneoasis.util.methods.Entities;
import com.sereneoasis.util.methods.Vectors;
import com.sereneoasis.util.temp.TempDisplayBlock;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class EnhancedDisplayBlocks {

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
            entry.getKey().moveTo(currentLoc.add(entry.getValue().clone().multiply(coreAbility.getSpeed())));
        }

    }
}
