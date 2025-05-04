package com.sonorous.archetypes.sky;

import com.sonorous.ability.superclasses.MasterAbility;
import com.sonorous.abilityuilities.particles.SourcedBlast;
import com.sonorous.util.AbilityStatus;
import com.sonorous.util.methods.AbilityUtils;
import com.sonorous.util.methods.ArchetypeVisuals;
import com.sonorous.util.methods.Blocks;
import com.sonorous.util.methods.Entities;
import org.bukkit.entity.Player;

public class LightningBolts extends MasterAbility {

    private static final String name = "LightningBolts";

    private int currentShots = 0, shots = 5, completeShots = 0;

    public LightningBolts(Player player) {
        super(player, name);

        if (shouldStart()) {
            abilityStatus = AbilityStatus.SHOOTING;
            setHasSneaked();
            start();
        }
    }

    @Override
    public void progress() {
        iterateHelpers(abilityStatus);

        AbilityUtils.showShots(this, currentShots, shots);

        if (completeShots == shots) {
            this.remove();
        }

    }

    @Override
    public void remove() {
        super.remove();
        sPlayer.addCooldown(name, cooldown);
    }

    public void setHasClicked() {
        iterateHelpers(AbilityStatus.SHOT);
    }

    public void setHasSneaked() {
        if (abilityStatus == AbilityStatus.SHOOTING && currentShots < shots) {
            SourcedBlast sourcedBlast = new SourcedBlast(player, name, false, new ArchetypeVisuals.LightningVisual(), false, true);
            helpers.put(sourcedBlast, (abilityStatus) -> {
                switch (abilityStatus) {
                    case SHOT -> {
                        sourcedBlast.setHasClicked();
                    }
                    case SHOOTING -> {
                        if (Blocks.isSolid(sourcedBlast.getLoc())) {
                            SkyUtils.lightningStrikeFloorCircle(this, sourcedBlast.getLoc(), 3);
                            sourcedBlast.remove();
                            removeHelper(sourcedBlast);
                            completeShots++;
                        }
                        if (sourcedBlast.getAbilityStatus() == AbilityStatus.DAMAGED) {
                            if (Entities.getAffected(sourcedBlast.getLoc(), hitbox, player) != null) {
                                SkyUtils.lightningStrike(this, (Entities.getAffected(sourcedBlast.getLoc(), hitbox, player).getLocation()));
                            }
                            sourcedBlast.remove();
                            removeHelper(sourcedBlast);
                            completeShots++;
                        }
                        if (sourcedBlast.getAbilityStatus() == AbilityStatus.COMPLETE) {
                            sourcedBlast.remove();
                            removeHelper(sourcedBlast);

                            completeShots++;
                        }
                    }
                }
            });
            currentShots++;
        }

    }

    @Override
    public String getName() {
        return name;
    }
}