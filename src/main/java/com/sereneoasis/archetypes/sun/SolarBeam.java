package com.sereneoasis.archetypes.sun;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.particles.Blast;
import com.sereneoasis.util.AbilityStatus;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class SolarBeam extends CoreAbility {

    private final String name = "SolarBeam";

    private Blast blast;
    public SolarBeam(Player player) {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(this.getName())) {
            return;
        }

        abilityStatus = AbilityStatus.CHARGING;
        start();
    }

    @Override
    public void progress() {
        if (abilityStatus == AbilityStatus.CHARGING) {
            if (!player.isSneaking()) {
                this.remove();
            }
            if (System.currentTimeMillis() > chargeTime + startTime) {
                abilityStatus = AbilityStatus.CHARGED;
            }
        }

        if (abilityStatus == AbilityStatus.SHOT) {
            if (blast.getAbilityStatus() == AbilityStatus.COMPLETE) {
                blast.remove();
            }
        }
    }

    public void setHasClicked()
    {
        if (abilityStatus == AbilityStatus.CHARGED)
        {
            blast = new Blast(player, name, false, Particle.FLAME);
            abilityStatus = AbilityStatus.SHOT;
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
