package com.sonorous.abilityuilities.blocks;

import com.sonorous.util.AbilityStatus;
import com.sonorous.util.methods.AbilityDamage;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class RaiseBlockPillarDamage extends RaiseBlockPillar {

    private final boolean hitOnce;

    private final Set<LivingEntity> damagedSet = new HashSet<>();

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
        super.progress();

        if (abilityStatus != AbilityStatus.COMPLETE && !isFalling) {
            if (currentHeight - size < height) {
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

    }
}
