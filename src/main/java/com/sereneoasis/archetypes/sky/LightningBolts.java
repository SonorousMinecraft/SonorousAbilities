package com.sereneoasis.archetypes.sky;

import com.sereneoasis.ability.superclasses.MasterAbility;
import com.sereneoasis.abilityuilities.particles.SourcedBlast;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.AbilityUtils;
import com.sereneoasis.util.methods.ArchetypeVisuals;
import com.sereneoasis.util.methods.Blocks;
import com.sereneoasis.util.methods.Entities;
import org.bukkit.entity.Player;

public class LightningBolts extends MasterAbility {

    private static final String name = "LightningBolts";

    private int currentShots = 0, shots = 5, completeShots = 0;

    public LightningBolts(Player player) {
        super(player, name);

        if (shouldStart()) {
            abilityStatus = AbilityStatus.SHOOTING;
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

    public void setHasSneaked(){
        if (abilityStatus == AbilityStatus.SHOOTING && currentShots<shots){
            SourcedBlast sourcedBlast = new SourcedBlast(player, name, false, new ArchetypeVisuals.LightningVisual(), false, true);
            helpers.put(sourcedBlast, (abilityStatus) -> {
                switch (abilityStatus){
                    case SHOT -> {
                        sourcedBlast.setHasClicked();
                    }
                    case SHOOTING -> {
                        if (Blocks.isSolid(sourcedBlast.getLoc())){
                            SkyUtils.lightningStrikeFloorCircle(this, sourcedBlast.getLoc(), 3);
                        }
                        if (sourcedBlast.getAbilityStatus() == AbilityStatus.DAMAGED) {
                            SkyUtils.lightningStrike(this, Entities.getAffected(sourcedBlast.getLoc(), hitbox, player).getLocation());
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