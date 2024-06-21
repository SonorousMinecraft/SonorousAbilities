package com.sereneoasis.archetypes.ocean;

import com.sereneoasis.ability.superclasses.MasterAbility;
import com.sereneoasis.abilityuilities.particles.Blast;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.AbilityUtils;
import com.sereneoasis.util.methods.ArchetypeVisuals;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class SnowShuriken extends MasterAbility {

    private static final String name = "SnowShuriken";

    private int currentShots = 0, shots = 8, completeShots = 0;

    public SnowShuriken(Player player) {
        super(player, name);

        if (shouldStart()) {
            abilityStatus = AbilityStatus.CHARGING;
            start();
        }
    }

    @Override
    public void progress() {
        iterateHelpers(abilityStatus);

        if (abilityStatus == AbilityStatus.CHARGING) {
            if (!player.isSneaking()) {
                this.remove();
            }
            if (System.currentTimeMillis() > chargeTime + startTime) {
                abilityStatus = AbilityStatus.CHARGED;
            }
        }
        AbilityUtils.showCharged(this);
        AbilityUtils.showShots(this, currentShots, shots);

        if (completeShots == shots && helpers.isEmpty()) {
            this.remove();
        }

    }

    @Override
    public void remove() {
        super.remove();
        sPlayer.addCooldown(name, cooldown);
    }

    public void setHasClicked() {
        if (abilityStatus == AbilityStatus.CHARGED) {
            abilityStatus = AbilityStatus.SHOOTING;
        }
        if (abilityStatus == AbilityStatus.SHOOTING && currentShots < shots) {
            Blast blast = new Blast(player, name, false, new ArchetypeVisuals.SnowVisual(), true);
            addHelper(blast, (abilityStatus) -> {
                switch (abilityStatus) {
                    case SHOOTING -> {
                        if (blast.getLoc().getBlock().getType().equals(Material.WATER)) {
                            OceanUtils.freeze(blast.getLoc(), 3, sPlayer);
                        }
                        if (blast.getAbilityStatus() == AbilityStatus.DAMAGED || blast.getAbilityStatus() == AbilityStatus.COMPLETE) {
                            removeHelper(blast);
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