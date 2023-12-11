package com.sereneoasis.archetypes.sky;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.particles.Sweep;
import com.sereneoasis.util.AbilityStatus;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class HeavenSlash extends CoreAbility {

    private final String name = "HeavenSlash";
    private Sweep sweep;

    public HeavenSlash(Player player) {
        super(player);
        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(this.getName())) {
            return;
        }

        Location forwardLoc = player.getEyeLocation().clone().add(player.getEyeLocation().getDirection().clone().normalize().multiply(0.5));
        this.sweep = new Sweep(player,name,Particle.SPELL, forwardLoc.clone().subtract(0,0.3,0), forwardLoc.clone().add(0,0.3,0));
        start();

    }

    @Override
    public void progress() {
        if (sweep.getAbilityStatus() == AbilityStatus.COMPLETE)
        {
            this.remove();
        }
    }

    @Override
    public void remove() {
        super.remove();
        if (sweep != null)
        {
            sweep.remove();
        }
        sPlayer.addCooldown(name, cooldown);
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
