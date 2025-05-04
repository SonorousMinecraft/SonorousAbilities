package com.sonorous.archetypes.sky;

import com.sonorous.ability.superclasses.CoreAbility;
import com.sonorous.abilityuilities.particles.Sweep;
import com.sonorous.util.AbilityStatus;
import com.sonorous.util.methods.ArchetypeVisuals;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class HeavenSlash extends CoreAbility {

    private static final String name = "HeavenSlash";
    private Sweep sweep;

    public HeavenSlash(Player player) {
        super(player, name);
        if (shouldStart()) {
            Location forwardLoc = player.getEyeLocation().clone().add(player.getEyeLocation().getDirection().clone().normalize().multiply(0.5));
            this.sweep = new Sweep(player, name, new ArchetypeVisuals.AirVisual(), forwardLoc.clone().subtract(0, 0.3, 0), forwardLoc.clone().add(0, 0.3, 0));
            start();
        }


    }

    @Override
    public void progress() {
        if (sweep.getAbilityStatus() == AbilityStatus.COMPLETE) {
            this.remove();
        }
    }

    @Override
    public void remove() {
        super.remove();
        if (sweep != null) {
            sweep.remove();
        }
        sPlayer.addCooldown(name, cooldown);
    }

    @Override
    public String getName() {
        return name;
    }
}