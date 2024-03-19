package com.sereneoasis.abilityuilities.blocks;

import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.AbilityDamage;
import com.sereneoasis.util.methods.Entities;
import com.sereneoasis.util.temp.TempBlock;
import com.sereneoasis.util.temp.TempDisplayBlock;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class RaiseBlockPillarDamage extends RaiseBlockPillar{

    private boolean hitOnce;

    private Set<LivingEntity> damagedSet = new HashSet<>();
    public RaiseBlockPillarDamage(Player player, String name, double height, boolean hitOnce) {
        super(player, name, height);
        this.hitOnce = hitOnce;
    }

    public RaiseBlockPillarDamage(Player player, String name, double height, Block targetBlock, boolean hitOnce) {
        super(player, name, height, targetBlock);
        this.hitOnce = hitOnce;
    }

    @Override
    public void progress() throws ReflectiveOperationException {
        if (abilityStatus != AbilityStatus.COMPLETE && !isFalling) {
            if (currentHeight >= height+0.1*speed) {
                for (TempDisplayBlock tdb : blocks) {
                    solidBlocks.add(new TempBlock(tdb.getBlockDisplay().getLocation().getBlock(), tdb.getBlockDisplay().getBlock().getMaterial(), duration, true));
                    tdb.revert();
                }
                abilityStatus = AbilityStatus.COMPLETE;
            } else {
                for (TempDisplayBlock tdb : blocks) {
                    tdb.moveTo(tdb.getBlockDisplay().getLocation().add(0, 0.1 * speed, 0));
                }
                currentHeight += 0.1 * speed;

                Entities.getEntitiesAroundPoint(origin.clone().add(0,currentHeight,0), hitbox).forEach(entity -> entity.setVelocity(new Vector(0, 0.1 * speed, 0)));
                if (hitOnce) {
                    boolean isFinished = AbilityDamage.damageSeveral(origin.clone().add(0, currentHeight, 0), this, player, true, new Vector(0, 0.1, 0));
                    if (isFinished) {
                        this.abilityStatus = AbilityStatus.COMPLETE;
                    }
                } else {
                    damagedSet.addAll(AbilityDamage.damageSeveralExceptReturnHit(loc, this, player, damagedSet, true, player.getEyeLocation().getDirection()));
                }
            }
        }

        if (isFalling && currentHeight > 0 && abilityStatus !=AbilityStatus.DROPPED) {
            for (TempDisplayBlock tdb : blocks) {
                tdb.moveTo(tdb.getBlockDisplay().getLocation().subtract(0, 0.1 * speed, 0));
            }
            currentHeight -= 0.1 * speed;
        }
        if (isFalling && currentHeight <= 0){
            abilityStatus = AbilityStatus.DROPPED;
        }
    }
}
