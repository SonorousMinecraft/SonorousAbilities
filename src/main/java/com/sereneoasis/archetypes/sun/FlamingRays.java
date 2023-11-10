package com.sereneoasis.archetypes.sun;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.particles.Blast;
import com.sereneoasis.util.AbilityStatus;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class FlamingRays extends CoreAbility {

    private final String name = "FlamingRays";

    private int currentShots = 0, shots = 5;

    private HashMap<Integer, Blast>rays = new HashMap<>();

    public FlamingRays(Player player) {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(this.getName())) {
            return;
        }

        abilityStatus = AbilityStatus.CHARGING;
        start();
    }

    @Override
    public void progress() {
        if (abilityStatus == AbilityStatus.CHARGING)
        {
            if (!player.isSneaking()) {
                this.remove();
            }
            if (System.currentTimeMillis() > chargeTime + startTime)
            {
                abilityStatus = AbilityStatus.CHARGED;
            }
        }
        if (abilityStatus == AbilityStatus.CHARGED)
        {
            for (int i = 0 ; i < currentShots; i++)
            {
                Blast blast = rays.get(i);
                if (blast.getAbilityStatus() == AbilityStatus.COMPLETE)
                {
                    blast.remove();
                    if (i == 5)
                    {
                        this.remove();
                    }
                }
            }

        }

    }

    public void setHasClicked()
    {
        if (abilityStatus == AbilityStatus.CHARGED)
        {
            Blast blast = new Blast(player, name, false, Particle.FLAME);
            rays.put(currentShots, blast);
            currentShots++;
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
