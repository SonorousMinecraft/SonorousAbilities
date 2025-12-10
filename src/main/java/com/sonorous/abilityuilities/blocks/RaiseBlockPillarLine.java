package com.sonorous.abilityuilities.blocks;

import com.sonorous.ability.superclasses.CoreAbility;
import com.sonorous.util.AbilityStatus;
import com.sonorous.util.methods.AbilityDamage;
import org.bukkit.Color;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class RaiseBlockPillarLine extends BlockLine {

    private final Set<RaiseBlockPillar> pillars = new HashSet<>();

    public RaiseBlockPillarLine(Player player, String name, Color color, boolean directable) {
        super(player, name, color, directable);
    }

    @Override
    public void progress() throws ReflectiveOperationException {
        if (abilityStatus == AbilityStatus.SHOT) {
            getNextLoc();
            if (loc != null) {
                RaiseBlockPillar pillar = new RaiseBlockPillar(player, name, 5, loc.clone().getBlock());
                pillars.add(pillar);
                boolean isFinished = AbilityDamage.damageOne(loc.clone().add(0, size / 2, 0), this, player, true, dir);

                if (isFinished) {
                    abilityStatus = AbilityStatus.COMPLETE;
                }
                if (loc.distanceSquared(origin) > range * range) {
                    abilityStatus = AbilityStatus.COMPLETE;
                }
            } else {
                abilityStatus = AbilityStatus.COMPLETE;
            }
        }
    }

    @Override
    public void remove() {
        super.remove();
        pillars.forEach(CoreAbility::remove);
    }
}
