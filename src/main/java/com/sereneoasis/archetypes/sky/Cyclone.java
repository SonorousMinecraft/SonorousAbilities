package com.sereneoasis.archetypes.sky;

import com.sereneoasis.ability.superclasses.CoreAbility;
import com.sereneoasis.abilityuilities.velocity.Skate;
import com.sereneoasis.util.AbilityStatus;
import com.sereneoasis.util.methods.Locations;
import com.sereneoasis.util.methods.Particles;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.List;

public class Cyclone extends CoreAbility {

    private final String name = "Cyclone";
    private Skate skate;

    public Cyclone(Player player) {
        super(player);

        if (CoreAbility.hasAbility(player, this.getClass()) || sPlayer.isOnCooldown(this.getName())) {
            return;
        }

        this.skate = new Skate(player, name, 3,3, true);
        start();
    }

    @Override
    public void progress() {
        if (skate.getAbilityStatus() == AbilityStatus.COMPLETE) {
            this.remove();
        }

        List<Location> locs = Locations.getCircle(player.getLocation().add(0, 1, 0), radius, 20, player.getEyeLocation().getDirection(), Math.toRadians(90));
        Particles.playLocParticles(locs, Particle.SPELL, 1, 0, 0);
    }

    public void setHasClicked() {
        this.remove();
    }

    @Override
    public void remove() {
        super.remove();
        if (skate != null) {
            skate.remove();
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
